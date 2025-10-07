package com.rafi.peminjaman_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rafi.peminjaman_service.model.PeminjamanQuery;

public interface PeminjamanQueryRepository extends MongoRepository<PeminjamanQuery, Long> {
    List<PeminjamanQuery> findByAnggotaId(Long anggotaId);
    List<PeminjamanQuery> findByBukuId(Long bukuId);
}
