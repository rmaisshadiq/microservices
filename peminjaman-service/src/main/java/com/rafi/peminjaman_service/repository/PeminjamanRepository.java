package com.rafi.peminjaman_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafi.peminjaman_service.model.Peminjaman;

@Repository
public interface PeminjamanRepository extends JpaRepository<Peminjaman, Long> {
    List<Peminjaman> findByAnggotaId(Long anggotaId);
    List<Peminjaman> findByBukuId(Long bukuId);
}
