package com.rafi.rabbitmq_peminjaman;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PeminjamanProducerService peminjamanProducerService;

    public DataInitializer(PeminjamanProducerService peminjamanProducerService) {
        this.peminjamanProducerService = peminjamanProducerService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Data sample untuk testing
        Peminjaman pinjam1 = new Peminjaman("Bumi", "Rafi Maisshadiq", "rmaisshadiq2@gmail.com");
        Peminjaman pinjam2 = new Peminjaman("Tenggelamnya Kapar Van der Wijck", "Bapak Ervan", "ervan@pnp.ac.id");
        
        peminjamanProducerService.createAndSendPeminjaman(pinjam1);
        peminjamanProducerService.createAndSendPeminjaman(pinjam2);
        
        System.out.println("Sample data initialized");
    }
}