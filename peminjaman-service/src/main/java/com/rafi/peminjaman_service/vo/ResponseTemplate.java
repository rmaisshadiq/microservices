package com.rafi.peminjaman_service.vo;

import com.rafi.peminjaman_service.model.PeminjamanQuery;

public class ResponseTemplate {
    PeminjamanQuery peminjaman;
    Anggota anggota;
    Buku buku;

    public ResponseTemplate() {}

    public ResponseTemplate(PeminjamanQuery peminjaman, Anggota anggota, Buku buku) {
        this.peminjaman = peminjaman;
        this.anggota = anggota;
        this.buku = buku;
    }

    public PeminjamanQuery getPeminjaman() {
        return peminjaman;
    }

    public void setPeminjaman(PeminjamanQuery peminjaman) {
        this.peminjaman = peminjaman;
    }

    public Anggota getAnggota() {
        return anggota;
    }

    public void setAnggota(Anggota anggota) {
        this.anggota = anggota;
    }

    public Buku getBuku() {
        return buku;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    
}
