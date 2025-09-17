package com.rafi.peminjaman_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rafi.peminjaman_service.model.Peminjaman;
import com.rafi.peminjaman_service.repository.PeminjamanRepository;
import com.rafi.peminjaman_service.vo.Anggota;
import com.rafi.peminjaman_service.vo.Buku;
import com.rafi.peminjaman_service.vo.ResponseTemplate;


@Service
public class PeminjamanService {
    @Autowired
    private PeminjamanRepository peminjamanRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    public List<Peminjaman> getAllPeminjamans() {
        return peminjamanRepository.findAll();
    }

    public Peminjaman getPeminjamanById(Long id) {
        return peminjamanRepository.findById(id).orElse(null);
    }

    public Peminjaman createPeminjaman(Peminjaman peminjaman) {
        return peminjamanRepository.save(peminjaman);
    }

    public void deletePeminjaman(Long id) {
        peminjamanRepository.deleteById(id);
    }

    public List<ResponseTemplate> getAllPeminjamanWithAnggotaAndBuku(Long id) {
        List<ResponseTemplate> responseList = new ArrayList<>();
        Peminjaman pinjam = getPeminjamanById(id);
        if (pinjam == null) {
            return null;
        }
        ServiceInstance serviceInstance = discoveryClient.getInstances("anggota-service").get(0);
        Anggota anggota = restTemplate.getForObject(serviceInstance.getUri() + "/api/anggota/"
            + pinjam.getAnggotaId(), Anggota.class);
            serviceInstance = discoveryClient.getInstances("buku-service").get(0);
        Buku buku = restTemplate.getForObject(serviceInstance.getUri() + "/api/buku/"
            + pinjam.getBukuId(), Buku.class);
        ResponseTemplate vo = new ResponseTemplate();
        vo.setPeminjaman(pinjam);
        vo.setAnggota(anggota);
        vo.setBuku(buku);
        responseList.add(vo);
        return responseList;
    }

}
