package com.rafi.anggota_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafi.anggota_service.model.Anggota;

@Repository
public interface AnggotaRepository extends JpaRepository<Anggota, Long>{

}
