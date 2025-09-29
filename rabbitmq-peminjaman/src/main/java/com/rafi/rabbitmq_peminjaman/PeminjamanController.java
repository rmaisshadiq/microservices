package com.rafi.rabbitmq_peminjaman;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/peminjaman")
public class PeminjamanController {

    private final PeminjamanProducerService peminjamanProducerService;
    private final PeminjamanRepository peminjamanRepository;

    public PeminjamanController(PeminjamanProducerService peminjamanProducerService, PeminjamanRepository peminjamanRepository) {
        this.peminjamanProducerService = peminjamanProducerService;
        this.peminjamanRepository = peminjamanRepository;
    }

    @PostMapping
    public ResponseEntity<Peminjaman> createPeminjaman(@Valid @RequestBody PeminjamanRequest peminjamanRequest) {
        Peminjaman peminjaman = new Peminjaman(
            peminjamanRequest.getNamaAnggota(),
            peminjamanRequest.getNamaBuku(),
            peminjamanRequest.getEmailPeminjam()
        );
        
        Peminjaman savedPeminjaman = peminjamanProducerService.createAndSendPeminjaman(peminjaman);
        return ResponseEntity.ok(savedPeminjaman);
    }

    @GetMapping
    public ResponseEntity<List<Peminjaman>> getAllPeminjamans() {
        return ResponseEntity.ok(peminjamanRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peminjaman> getPeminjamanById(@PathVariable UUID id) {
        return peminjamanRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Peminjaman>> getPeminjamansByStatus(@PathVariable Peminjaman.StatusPinjam status) {
        return ResponseEntity.ok(peminjamanRepository.findByStatus(status));
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<Peminjaman>> getPeminjamansByCustomer(@PathVariable String email) {
        return ResponseEntity.ok(peminjamanRepository.findByEmailPeminjam(email));
    }
}

// DTO untuk request
class PeminjamanRequest {
    
    @NotBlank(message = "Anggota name is required")
    private String namaAnggota;
    
    @NotBlank(message = "Buku name is required")
    private String namaBuku;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Customer email is required")
    private String emailPeminjam;
    
    // Getters and Setters
    public String getNamaAnggota() { return namaAnggota; }
    public void setNamaAnggota(String namaAnggota) { this.namaAnggota = namaAnggota; }
    
    public String getNamaBuku() { return namaBuku; }
    public void setNamaBuku(String namaBuku) { this.namaBuku = namaBuku; }
    
    public String getEmailPeminjam() { return emailPeminjam; }
    public void setEmailPeminjam(String emailPeminjam) { this.emailPeminjam = emailPeminjam; }
}