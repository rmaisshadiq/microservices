package com.rafi.pengembalian_service.vo;

import java.time.LocalDate;

public class Peminjaman {
    private Long id;
    private Long anggotaId;
    private Long bukuId;
    private LocalDate tanggal_pinjam;
    private LocalDate tanggal_kenbali;

    private Peminjaman() {}

    private Peminjaman(Long id, Long anggotaId, Long bukuId, LocalDate tanggal_pinjam, LocalDate tanggal_kembali) {
        this.id = id;
        this.anggotaId = anggotaId;
        this.bukuId = bukuId;
        this.tanggal_pinjam = tanggal_pinjam;
        this.tanggal_kenbali = tanggal_kembali;
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
        return tanggal_kenbali;
    }

    public void setTanggal_kembali(LocalDate tanggal_kembali) {
        this.tanggal_kenbali = tanggal_kembali;
    }

    // @Override
    // public String toString() {
    //     // TODO Auto-generated method stub
    //     return super.toString();
    // }
}
