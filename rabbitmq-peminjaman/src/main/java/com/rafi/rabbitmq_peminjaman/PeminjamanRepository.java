package com.rafi.rabbitmq_peminjaman;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeminjamanRepository extends JpaRepository<Peminjaman, UUID> {
    List<Peminjaman> findByStatus(Peminjaman.StatusPinjam status);
    List<Peminjaman> findByEmailPeminjam(String emailPeminjam);
}
