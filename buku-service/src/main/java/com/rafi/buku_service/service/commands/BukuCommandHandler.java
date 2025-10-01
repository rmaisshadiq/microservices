package com.rafi.buku_service.service.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.rafi.buku_service.dto.BukuCreatedEvent;
import com.rafi.buku_service.model.Buku;
import com.rafi.buku_service.repository.BukuRepository;

@Service
public class BukuCommandHandler {
    private static final String TOPIC_BUKU_EVENTS = "buku-events";

    @Autowired
    private BukuRepository bukuRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public Buku handleCreateBuku(Buku buku) {
        // Simpan ke database
        Buku savedBuku = bukuRepository.save(buku);

        // Buat event
        BukuCreatedEvent event = new BukuCreatedEvent(
            savedBuku.getId(),
            savedBuku.getJudul(),
            savedBuku.getPengarang(),
            savedBuku.getPenerbit(),
            savedBuku.getTahun_terbit()
        );

        // Kirim event ke Kafka
        kafkaTemplate.send(TOPIC_BUKU_EVENTS, event);

        return savedBuku;
    }
}
