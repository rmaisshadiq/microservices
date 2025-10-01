package com.rafi.buku_service.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.rafi.buku_service.model.read.BukuDocument;
import com.rafi.buku_service.repository.BukuDocumentRepository;

@Service
public class BukuEventListener {

    @Autowired
    private BukuDocumentRepository bukuDocumentRepository;

    @KafkaListener(topics = "buku-events", groupId = "group_id")
    public void handleBukuEvent(BukuCreatedEvent bukuEvent) {
        System.out.println("Listener menerima pesan BukuCreatedEvent untuk ID" + bukuEvent.getId());

        // Buat objek Document baru dari data Event
        BukuDocument bukuDocument = new BukuDocument();
        bukuDocument.setId(bukuEvent.getId());
        bukuDocument.setJudul(bukuEvent.getJudul());
        bukuDocument.setPengarang(bukuEvent.getPengarang());
        bukuDocument.setPenerbit(bukuEvent.getPenerbit());
        bukuDocument.setTahun_terbit(bukuEvent.getTahun_terbit());

        // Simpan objek Document ke MongoDB
        bukuDocumentRepository.save(bukuDocument);
    }
}
