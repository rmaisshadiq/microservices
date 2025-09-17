package com.rafi.pengembalian_service.vo;

public class Peminjaman {
    private Long id;
    private Long anggotaId;
    private Long bukuId;
    private String tanggal_pinjam;
    private String tanggal_kembali;

    private Peminjaman() {}

    private Peminjaman(Long id, Long anggotaId, Long bukuId, String tanggal_pinjam, String tanggal_kembali) {
        this.id = id;
        this.anggotaId = anggotaId;
        this.bukuId = bukuId;
        this.tanggal_pinjam = tanggal_pinjam;
        this.tanggal_kembali = tanggal_kembali;
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

    public String getTanggal_pinjam() {
        return tanggal_pinjam;
    }

    public void setTanggal_pinjam(String tanggal_pinjam) {
        this.tanggal_pinjam = tanggal_pinjam;
    }

    public String getTanggal_kembali() {
        return tanggal_kembali;
    }

    public void setTanggal_kembali(String tanggal_kembali) {
        this.tanggal_kembali = tanggal_kembali;
    }

    // @Override
    // public String toString() {
    //     // TODO Auto-generated method stub
    //     return super.toString();
    // }
}
