package com.rafi.peminjaman_service.service;

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
import com.rafi.peminjaman_service.repository.PeminjamanRepository;
import com.rafi.peminjaman_service.vo.Anggota;
import com.rafi.peminjaman_service.vo.Buku;

import jakarta.transaction.Transactional;

@Service
public class PeminjamanConsumerService {
    private final PeminjamanRepository peminjamanRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public PeminjamanConsumerService(PeminjamanRepository peminjamanRepository) {
        this.peminjamanRepository = peminjamanRepository;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    @Transactional
    public void receivePeminjaman(Peminjaman peminjaman) {
        try {
            System.out.println("Peminjaman received from RabbitMQ: " + peminjaman);

            peminjaman.setStatus(Peminjaman.PinjamStatus.PROCESSING);
            peminjamanRepository.save(peminjaman);

            // Simulasi proses bisnis
            processPeminjaman(peminjaman);

            // Update status setelah selesai diproses
            peminjaman.setStatus(Peminjaman.PinjamStatus.COMPLETED);
            peminjamanRepository.save(peminjaman);

            System.out.println("Peminjaman processed successfully: " + peminjaman.getId());
        }
        catch (Exception e) {
            System.err.println("Error processing peminjaman: " + peminjaman.getId() + ", Error: " + e.getMessage());

            // Update status jika gagal
            peminjaman.setStatus(Peminjaman.PinjamStatus.FAILED);
            peminjamanRepository.save(peminjaman);
        }
    }

    private void processPeminjaman(Peminjaman peminjaman) {
        //Simulasi proses bisnis
        System.out.println("Memproses peminjaman: " + peminjaman.getId());
        Long anggotaId = peminjaman.getAnggotaId();

        ServiceInstance serviceInstance = discoveryClient.getInstances("anggota-service").get(0);
        Anggota anggota = restTemplate.getForObject(serviceInstance.getUri() + "/api/anggota/" + anggotaId, Anggota.class);

        serviceInstance = discoveryClient.getInstances("buku-service").get(0);
        Buku buku = restTemplate.getForObject(serviceInstance.getUri() + "/api/buku/" + peminjaman.getBukuId(), Buku.class);

        if (anggota != null && anggota.getEmail() != null) {
            System.out.println("Sending confirmation email to: " + anggota.getEmail());

            if (buku != null) {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom(sender);
                msg.setTo(anggota.getEmail());
                msg.setSubject("Konfirmasi Peminjaman ID: " + peminjaman.getId());
                msg.setText("Halo, " + anggota.getNama() + " dengan email " + anggota.getEmail() + " telah berhasil meminjam buku " + buku.getJudul() + ".\n\n"
                        + "\tDengan ID Peminjaman: " + peminjaman.getId() + ".\n\n"
                        + "Terima kasih atas peminjaman Anda. :)");
                
                try {
                    this.mailSender.send(msg);
                }
                catch (MailException ex) {
                    System.err.println("Error sending email: " + ex.getMessage());
                }
                
                System.out.println("Peminjaman processed successfully: " + peminjaman.getId());
            } else {
                System.out.println("Buku not found for peminjaman ID: " + peminjaman.getId());
            }
        }
        else {
            System.out.println("Anggota not found for peminjaman ID: " + peminjaman.getId());
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Peminjaman processed successfully: " + peminjaman.getId());
    }
}
