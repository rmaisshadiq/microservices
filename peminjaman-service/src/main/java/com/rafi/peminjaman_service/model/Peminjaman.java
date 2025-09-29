package com.rafi.peminjaman_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Data
@Entity
public class Peminjaman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long anggotaId;
    private Long bukuId;
    private LocalDateTime tanggal_pinjam;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PinjamStatus status;

    public enum PinjamStatus {
        PENDING, PROCESSING, COMPLETED, FAILED
    }

    public Peminjaman() {
        this.status = PinjamStatus.PENDING;
        this.tanggal_pinjam = LocalDateTime.now();
    }

    public Peminjaman(Long anggotaId, Long bukuId) {
        this();
        this.anggotaId = anggotaId;
        this.bukuId = bukuId;
    }

    @PrePersist
    protected void onCreate() {
        this.tanggal_pinjam = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Peminjaman{" +
                "id=" + id +
                ", anggotaId=" + anggotaId +
                ", bukuId=" + bukuId +
                ", tanggal_pinjam=" + tanggal_pinjam +
                ", status=" + status +
                '}';
    }

}
