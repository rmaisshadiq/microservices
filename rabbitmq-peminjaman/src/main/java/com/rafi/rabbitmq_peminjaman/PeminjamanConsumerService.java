package com.rafi.rabbitmq_peminjaman;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PeminjamanConsumerService {
    private final PeminjamanRepository peminjamanRepository;

    @Autowired
    private MailSender mailSender;

    @Value("${spring.mail.username}") private String sender;
    

    public PeminjamanConsumerService(PeminjamanRepository peminjamanRepository) {
        this.peminjamanRepository = peminjamanRepository;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    @Transactional
    public void receivePeminjaman(@Payload Peminjaman peminjaman) {
        try {
            System.out.println("Peminjaman received from RabbitMQ: " + peminjaman);
            
            // Update status peminjaman
            peminjaman.setStatus(Peminjaman.StatusPinjam.PROCESSING);
            peminjamanRepository.save(peminjaman);
            
            // Simulasi proses bisnis
            processPeminjaman(peminjaman);
            
            // Update status setelah selesai diproses
            peminjaman.setStatus(Peminjaman.StatusPinjam.COMPLETED);
            peminjaman.setProcessedAt(java.time.LocalDateTime.now());
            peminjamanRepository.save(peminjaman);
            
            System.out.println("Peminjaman processed successfully: " + peminjaman.getId());
            
        } catch (Exception e) {
            System.err.println("Error processing peminjaman: " + peminjaman.getId() + ", Error: " + e.getMessage());
            
            // Update status jika gagal
            peminjaman.setStatus(Peminjaman.StatusPinjam.FAILED);
            peminjamanRepository.save(peminjaman);
            
            // Bisa ditambahkan logic untuk retry atau dead letter queue
            throw new RuntimeException("Failed to process peminjaman", e);
        }
    }

    private void processPeminjaman(Peminjaman peminjaman) {
        // Simulasi proses bisnis
        System.out.println("Processing peminjaman: " + peminjaman.getId());
        System.out.println("Sending confirmation email to: " + peminjaman.getEmailPeminjam());

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(sender);
        msg.setTo(peminjaman.getEmailPeminjam());
        msg.setSubject("Konfirmasi Peminjaman ID: " + peminjaman.getId());
        msg.setText(
            "Halo, " + peminjaman.getNamaAnggota() + " dengan email " + peminjaman.getEmailPeminjam() + " telah berhasil membeli produk " + peminjaman.getNamaBuku() + ".\n\n"
            + "\tDengan ID Peminjaman: " + peminjaman.getId() + ".\n\n"
            + "Terima kasih atas peminjaman Anda. :)");
        
        try {
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            System.err.println("Error sending email: " + ex.getMessage());
        }
        
        // Simulasi delay processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Peminjaman processing completed: " + peminjaman.getId());
    }
}