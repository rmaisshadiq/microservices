package com.rafi.rabbitmq_peminjaman;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PeminjamanProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final PeminjamanRepository peminjamanRepository;
    
    @Value("${app.rabbitmq.exchange}")
    private String exchange;
    
    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public PeminjamanProducerService(RabbitTemplate rabbitTemplate, PeminjamanRepository peminjamanRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.peminjamanRepository = peminjamanRepository;
    }


    @Transactional
    public Peminjaman createAndSendPeminjaman(Peminjaman peminjaman) {
        // Simpan peminjaman ke database
        Peminjaman savedPeminjaman = peminjamanRepository.save(peminjaman);
        System.out.println("Peminjaman saved to database: " + savedPeminjaman);
        
        // Kirim ke RabbitMQ
        rabbitTemplate.convertAndSend(exchange, routingKey, savedPeminjaman);
        System.out.println("Peminjaman sent to RabbitMQ: " + savedPeminjaman);
        
        return savedPeminjaman;
    }
}