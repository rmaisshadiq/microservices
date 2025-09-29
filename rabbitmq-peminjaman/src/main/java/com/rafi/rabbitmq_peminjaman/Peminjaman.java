package com.rafi.rabbitmq_peminjaman;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Peminjaman {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "Nama Buku diperlukan")
    @Column(nullable = false)
    private String namaBuku;
    
    @NotBlank(message = "Nama Anggota diperlukan")
    @Column(nullable = false)
    private String namaAnggota;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Customer email is required")
    @Column(nullable = false)
    private String emailPeminjam;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPinjam status;
    
    @Column(updatable = false)
    private LocalDateTime tanggal_pinjam;
    
    private LocalDateTime processedAt;
    
    // Enums
    public enum StatusPinjam {
        PENDING, PROCESSING, COMPLETED, FAILED
    }
    
    // Constructors
    public Peminjaman() {
        this.status = StatusPinjam.PENDING;
        this.tanggal_pinjam = LocalDateTime.now();
    }
    
    public Peminjaman(String namaBuku, String namaAnggota, String emailPeminjam) {
        this();
        this.namaBuku = namaBuku;
        this.namaAnggota = namaAnggota;
        this.emailPeminjam = emailPeminjam;
    }
    
    // PrePersist callback
    @PrePersist
    protected void onCreate() {
        this.tanggal_pinjam = LocalDateTime.now();
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getNamaBuku() { return namaBuku; }
    public void setNamaBuku(String namaBuku) { this.namaBuku = namaBuku; }
    
    public String getNamaAnggota() { return namaAnggota; }
    public void setNamaAnggota(String namaAnggota) { this.namaAnggota = namaAnggota; }
    
    public String getEmailPeminjam() { return emailPeminjam; }
    public void setEmailPeminjam(String emailPeminjam) { this.emailPeminjam = emailPeminjam; }
    
    public StatusPinjam getStatus() { return status; }
    public void setStatus(StatusPinjam status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return tanggal_pinjam; }
    public void setCreatedAt(LocalDateTime tanggal_pinjam) { this.tanggal_pinjam = tanggal_pinjam; }
    
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
    
    @Override
    public String toString() {
        return "Peminjaman{" +
                "id=" + id +
                ", namaBuku='" + namaBuku + '\'' +
                ", namaAnggota=" + namaAnggota + '\'' +
                ", emailPeminjam='" + emailPeminjam + '\'' +
                ", status=" + status +
                ", tanggal_pinjam=" + tanggal_pinjam +
                ", processedAt=" + processedAt +
                '}';
    }
}