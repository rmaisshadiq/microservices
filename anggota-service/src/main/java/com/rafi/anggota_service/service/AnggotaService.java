package com.rafi.anggota_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafi.anggota_service.model.Anggota;
import com.rafi.anggota_service.repository.AnggotaRepository;


@Service
public class AnggotaService {
    @Autowired
    private AnggotaRepository anggotaRepository;

    public List<Anggota> getAllAnggotas() {
        return anggotaRepository.findAll();
    }

    public Anggota getAnggotaById(Long id) {
        return anggotaRepository.findById(id).orElse(null);
    }

    public Anggota createAnggota(Anggota anggota) {
        return anggotaRepository.save(anggota);
    }

    public void deleteAnggota(Long id) {
        anggotaRepository.deleteById(id);
    }

}
