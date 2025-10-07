package com.rafi.peminjaman_service.model;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import com.rafi.peminjaman_service.model.Peminjaman.PinjamStatus;

import jakarta.persistence.Id;

@Document(collection = "peminjaman_view")
public class PeminjamanQuery {
    @Id
    private Long id;
    private Long anggotaId;
    private Long bukuId;
    private LocalDate tanggal_pinjam;
    private LocalDate tanggal_kembali;
    private PinjamStatus status;

    public PeminjamanQuery() {}

    public PeminjamanQuery(Long id, Long anggotaId, Long bukuId, LocalDate tanggal_pinjam, LocalDate tanggal_kembali, PinjamStatus status) {
        this.id = id;
        this.anggotaId = anggotaId;
        this.bukuId = bukuId;
        this.tanggal_pinjam = tanggal_pinjam;
        this.tanggal_kembali = tanggal_kembali;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnggotaId() {
        return anggotaId;
    }

    public void setAnggotaId(Long anggotaId) {
        this.anggotaId = anggotaId;
    }

    public Long getBukuId() {
        return bukuId;
    }

    public void setBukuId(Long bukuId) {
        this.bukuId = bukuId;
    }

    public LocalDate getTanggal_pinjam() {
        return tanggal_pinjam;
    }

    public void setTanggal_pinjam(LocalDate tanggal_pinjam) {
        this.tanggal_pinjam = tanggal_pinjam;
    }

    public LocalDate getTanggal_kembali() {
        return tanggal_kembali;
    }

    public void setTanggal_kembali(LocalDate tanggal_kembali) {
        this.tanggal_kembali = tanggal_kembali;
    }

    public PinjamStatus getStatus() {
        return status;
    }

    public void setStatus(PinjamStatus status) {
        this.status = status;
    }

    
}
