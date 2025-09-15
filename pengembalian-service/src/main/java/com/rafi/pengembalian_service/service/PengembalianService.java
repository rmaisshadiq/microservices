package com.rafi.pengembalian_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rafi.pengembalian_service.model.Pengembalian;
import com.rafi.pengembalian_service.repository.PengembalianRepository;
import com.rafi.pengembalian_service.vo.Peminjaman;
import com.rafi.pengembalian_service.vo.ResponseTemplate;


@Service
public class PengembalianService {
    @Autowired
    private PengembalianRepository pengembalianRepository;
    
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    public List<Pengembalian> getAllPengembalians() {
        return pengembalianRepository.findAll();
    }


    public Pengembalian getPengembalianById(Long id) {
        return pengembalianRepository.findById(id).orElse(null);
    }

    public Pengembalian createPengembalian(Pengembalian pengembalian) {
        return pengembalianRepository.save(pengembalian);
    }

    public void deletePengembalian(Long id) {
        pengembalianRepository.deleteById(id);
    }

    public List<ResponseTemplate> getAllPengembalianWithPeminjaman(Long id) {
        List<ResponseTemplate> responseList = new ArrayList<>();
        Pengembalian pengembalian = getPengembalianById(id);
        if (pengembalian == null) {
            return null;
        }
        ServiceInstance serviceInstance = discoveryClient.getInstances("pengembalian-service").get(0);
        Peminjaman peminjaman = restTemplate.getForObject(serviceInstance.getUri() + "/api/peminjaman/"
            + pengembalian.getPeminjamanId(), Peminjaman.class);
        ResponseTemplate vo = new ResponseTemplate();
        vo.setPeminjaman(peminjaman);
        vo.setPengembalian(pengembalian);
        responseList.add(vo);
        return responseList;
    }

}
