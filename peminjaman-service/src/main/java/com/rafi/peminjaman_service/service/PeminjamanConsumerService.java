package com.rafi.peminjaman_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rafi.peminjaman_service.model.Peminjaman;
import com.rafi.peminjaman_service.model.PeminjamanQuery;
import com.rafi.peminjaman_service.repository.PeminjamanQueryRepository;
import com.rafi.peminjaman_service.repository.PeminjamanRepository;
import com.rafi.peminjaman_service.vo.Anggota;
import com.rafi.peminjaman_service.vo.Buku;

import jakarta.transaction.Transactional;

@Service
public class PeminjamanConsumerService {

    private static final Logger log = LoggerFactory.getLogger(PeminjamanConsumerService.class);

    private final PeminjamanRepository peminjamanRepository;
    private final PeminjamanQueryRepository peminjamanQueryRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public PeminjamanConsumerService(PeminjamanRepository peminjamanRepository,
            PeminjamanQueryRepository peminjamanQueryRepository) {
        this.peminjamanRepository = peminjamanRepository;
        this.peminjamanQueryRepository = peminjamanQueryRepository;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    @Transactional
    public void receivePeminjaman(PeminjamanQuery peminjamanMessage) {
        log.info("Peminjaman received from RabbitMQ for Anggota ID: {}", peminjamanMessage.getAnggotaId());

        Long peminjamanId = peminjamanMessage.getId();

        Peminjaman peminjamanCommand = peminjamanRepository.findById(peminjamanId)
                .orElseGet(() -> createPeminjamanFromMessage(peminjamanMessage));

        PeminjamanQuery peminjamanQuery = peminjamanQueryRepository.findById(peminjamanId)
                .orElseGet(() -> createPeminjamanQueryFromMessage(peminjamanMessage));

        try {
            // 1. Simpan state awal sebagai PROCESSING dan ambil hasil save
            peminjamanCommand.setStatus(Peminjaman.PinjamStatus.PROCESSING);
            peminjamanQuery.setStatus(Peminjaman.PinjamStatus.PROCESSING);

            peminjamanCommand = peminjamanRepository.save(peminjamanCommand);
            peminjamanQuery.setId(peminjamanCommand.getId());
            peminjamanQuery = peminjamanQueryRepository.save(peminjamanQuery);

            log.info("Peminjaman with ID {} saved with PROCESSING status.", peminjamanCommand.getId());

            // 2. Jalankan proses bisnis utama
            processPeminjaman(peminjamanQuery); // Gunakan query yang sudah punya ID
            log.info("Business logic for Peminjaman ID {} processed successfully.", peminjamanCommand.getId());

            // 3. Update status menjadi COMPLETED - gunakan objek yang sudah punya ID
            peminjamanCommand.setStatus(Peminjaman.PinjamStatus.COMPLETED);
            peminjamanQuery.setStatus(Peminjaman.PinjamStatus.COMPLETED);

            peminjamanRepository.save(peminjamanCommand);
            peminjamanQueryRepository.save(peminjamanQuery);

            log.info("Peminjaman ID {} status updated to COMPLETED.", peminjamanCommand.getId());

        } catch (Exception e) {
            log.error("Error processing Peminjaman for Anggota ID: {}. Error: {}",
                    peminjamanMessage.getAnggotaId(), e.getMessage(), e);

            // 4. Update status menjadi FAILED
            if (peminjamanCommand.getId() != null) {
                peminjamanCommand.setStatus(Peminjaman.PinjamStatus.FAILED);
                peminjamanQuery.setStatus(Peminjaman.PinjamStatus.FAILED);

                peminjamanRepository.save(peminjamanCommand);
                peminjamanQueryRepository.save(peminjamanQuery);

                log.warn("Peminjaman ID {} status updated to FAILED.", peminjamanCommand.getId());
            }
        }
    }

    private void processPeminjaman(PeminjamanQuery peminjaman) {
        // Simulasi proses bisnis
        System.out.println("Memproses peminjaman: " + peminjaman.getId());
        Long anggotaId = peminjaman.getAnggotaId();

        ServiceInstance serviceInstance = discoveryClient.getInstances("anggota-service").get(0);
        Anggota anggota = restTemplate.getForObject(serviceInstance.getUri() + "/api/anggota/" + anggotaId,
                Anggota.class);

        serviceInstance = discoveryClient.getInstances("buku-service").get(0);
        Buku buku = restTemplate.getForObject(serviceInstance.getUri() + "/api/buku/" + peminjaman.getBukuId(),
                Buku.class);

        if (anggota != null && anggota.getEmail() != null) {
            System.out.println("Sending confirmation email to: " + anggota.getEmail());

            if (buku != null) {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom(sender);
                msg.setTo(anggota.getEmail());
                msg.setSubject("Konfirmasi Peminjaman ID: " + peminjaman.getId());
                msg.setText("Halo, " + anggota.getNama() + " dengan email " + anggota.getEmail()
                        + " telah berhasil meminjam buku " + buku.getJudul() + ".\n\n"
                        + "\tDengan ID Peminjaman: " + peminjaman.getId() + ".\n\n"
                        + "Terima kasih atas peminjaman Anda. :)");

                try {
                    this.mailSender.send(msg);
                } catch (MailException ex) {
                    System.err.println("Error sending email: " + ex.getMessage());
                }

                System.out.println("Peminjaman processed successfully: " + peminjaman.getId());
            } else {
                System.out.println("Buku not found for peminjaman ID: " + peminjaman.getId());
            }
        } else {
            System.out.println("Anggota not found for peminjaman ID: " + peminjaman.getId());
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Peminjaman processed successfully: " + peminjaman.getId());
    }

    /**
     * Factory method untuk membuat entitas Peminjaman (Command).
     */
    private Peminjaman createPeminjamanFromMessage(PeminjamanQuery message) {
        Peminjaman peminjaman = new Peminjaman();
        peminjaman.setAnggotaId(message.getAnggotaId());
        peminjaman.setBukuId(message.getBukuId());
        peminjaman.setTanggal_pinjam(message.getTanggal_pinjam());
        peminjaman.setTanggal_kembali(message.getTanggal_kembali());
        return peminjaman;
    }

    /**
     * Factory method untuk membuat entitas PeminjamanQuery (Query).
     */
    private PeminjamanQuery createPeminjamanQueryFromMessage(PeminjamanQuery message) {
        PeminjamanQuery peminjaman = new PeminjamanQuery();
        peminjaman.setAnggotaId(message.getAnggotaId());
        peminjaman.setBukuId(message.getBukuId());
        peminjaman.setTanggal_pinjam(message.getTanggal_pinjam());
        peminjaman.setTanggal_kembali(message.getTanggal_kembali());
        return peminjaman;
    }
}
