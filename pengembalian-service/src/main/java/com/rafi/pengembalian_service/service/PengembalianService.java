package com.rafi.pengembalian_service.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rafi.pengembalian_service.model.Pengembalian;
import com.rafi.pengembalian_service.repository.PengembalianRepository;
import com.rafi.pengembalian_service.vo.Anggota;
import com.rafi.pengembalian_service.vo.Buku;
import com.rafi.pengembalian_service.vo.Peminjaman;
import com.rafi.pengembalian_service.vo.ResponseTemplate;

import jakarta.transaction.Transactional;


@Service
public class PengembalianService {
    @Autowired
    private PengembalianRepository pengembalianRepository;
    
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    private static double dendaPerHari = 1000;

    public List<Pengembalian> getAllPengembalians() {
        return pengembalianRepository.findAll();
    }


    public Pengembalian getPengembalianById(Long id) {
        return pengembalianRepository.findById(id).orElse(null);
    }

    @Transactional
    public Pengembalian createPengembalian(Pengembalian pengembalian) {
        Long peminjamanId = pengembalian.getPeminjamanId();
        ServiceInstance serviceInstance = discoveryClient.getInstances("peminjaman-service").get(0);
        Peminjaman peminjaman = restTemplate.getForObject(serviceInstance.getUri() + "/api/peminjaman/"
            + peminjamanId, Peminjaman.class);

        if (peminjaman == null) {
            throw new RuntimeException("ID Peminjaman tidak ditemukan dari service peminjaman: " + pengembalian.getPeminjamanId());
        }

        LocalDate tanggalDikembalikan = pengembalian.getTanggal_dikembalikan();
        LocalDate tanggalHarusKembali = peminjaman.getTanggal_kembali();

        long selisihHari = 0;
        if (tanggalDikembalikan.isAfter(tanggalHarusKembali)) {
            selisihHari = ChronoUnit.DAYS.between(tanggalHarusKembali, tanggalDikembalikan);
        }

        double denda = selisihHari * dendaPerHari;

        pengembalian.setTerlambat(selisihHari);
        pengembalian.setDenda(denda);
        return pengembalianRepository.save(pengembalian);
    }

    public void deletePengembalian(Long id) {
        pengembalianRepository.deleteById(id);
    }

    public List<ResponseTemplate> getAllPengembalianWithPeminjaman(Long id) {
        List<ResponseTemplate> responseList = new ArrayList<>();
        Pengembalian pengembalian = getPengembalianById(id);
        if (pengembalian == null) {
            return responseList;
        }
        ServiceInstance serviceInstance = discoveryClient.getInstances("peminjaman-service").get(0);
        Peminjaman peminjaman = restTemplate.getForObject(serviceInstance.getUri() + "/api/peminjaman/"
            + pengembalian.getPeminjamanId(), Peminjaman.class);

        if (peminjaman != null) {
            serviceInstance = discoveryClient.getInstances("anggota-service").get(0);
            Anggota anggota = restTemplate.getForObject(serviceInstance.getUri() + "/api/anggota/" 
            + peminjaman.getAnggotaId(), Anggota.class);

            serviceInstance = discoveryClient.getInstances("buku-service").get(0);
            Buku buku = restTemplate.getForObject(serviceInstance.getUri() + "/api/buku/"
            + peminjaman.getBukuId(), Buku.class);
            
            ResponseTemplate vo = new ResponseTemplate();
            vo.setAnggota(anggota);
            vo.setBuku(buku);
            vo.setPeminjaman(peminjaman);
            vo.setPengembalian(pengembalian);
            responseList.add(vo);
        };
        
        return responseList;
    }

}
