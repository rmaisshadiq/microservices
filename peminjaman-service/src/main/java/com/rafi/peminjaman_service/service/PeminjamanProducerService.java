package com.rafi.peminjaman_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rafi.peminjaman_service.model.Peminjaman;
import com.rafi.peminjaman_service.model.PeminjamanQuery;
import com.rafi.peminjaman_service.repository.PeminjamanQueryRepository;
import com.rafi.peminjaman_service.repository.PeminjamanRepository;
import com.rafi.peminjaman_service.vo.Anggota;
import com.rafi.peminjaman_service.vo.Buku;
import com.rafi.peminjaman_service.vo.ResponseTemplate;

import jakarta.transaction.Transactional;


@Service
public class PeminjamanProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final PeminjamanRepository peminjamanRepository;
    private final PeminjamanQueryRepository peminjamanQueryRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public PeminjamanProducerService(RabbitTemplate rabbitTemplate, PeminjamanRepository peminjamanRepository, PeminjamanQueryRepository peminjamanQueryRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.peminjamanRepository = peminjamanRepository;
        this.peminjamanQueryRepository = peminjamanQueryRepository;
    }

    public PeminjamanQuery getPeminjamanById(Long id) {
        return peminjamanQueryRepository.findById(id).orElse(null);
    }

    @Transactional
    public Peminjaman createPeminjaman(Peminjaman peminjaman) {
        Peminjaman savedPeminjaman = peminjamanRepository.save(peminjaman);
        System.out.println("Peminjaman tersimpan ke database: " + savedPeminjaman);

        // Kirim ke RabbitMQ
        rabbitTemplate.convertAndSend(exchange, routingKey, savedPeminjaman);
        System.out.println("Peminjaman dikirim ke RabbitMQ: " + savedPeminjaman);
        return savedPeminjaman;
    }

    public void deletePeminjaman(Long id) {
        peminjamanRepository.deleteById(id);
    }

    public List<ResponseTemplate> getAllPeminjamanWithAnggotaAndBuku(Long id) {
        List<ResponseTemplate> responseList = new ArrayList<>();
        PeminjamanQuery pinjam = getPeminjamanById(id);
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

    public List<ResponseTemplate> getAllPeminjamans() {
        List<PeminjamanQuery> semuaPeminjaman = peminjamanQueryRepository.findAll();
        if (semuaPeminjaman.isEmpty()) {
            return new ArrayList<>();
        }

        List<ResponseTemplate> responseList = new ArrayList<>();

        for (PeminjamanQuery pinjam : semuaPeminjaman) {
            // Panggil service lain menggunakan ID dari objek 'pinjam' yang sedang di-loop
            ServiceInstance serviceInstanceAnggota = discoveryClient.getInstances("anggota-service").get(0);
            Anggota anggota = restTemplate.getForObject(serviceInstanceAnggota.getUri() + "/api/anggota/" 
                + pinjam.getAnggotaId(), Anggota.class);
                
            ServiceInstance serviceInstanceBuku = discoveryClient.getInstances("buku-service").get(0);
            Buku buku = restTemplate.getForObject(serviceInstanceBuku.getUri() + "/api/buku/" 
                + pinjam.getBukuId(), Buku.class);
                
            // Buat ResponseTemplate untuk setiap peminjaman
            ResponseTemplate vo = new ResponseTemplate();
            vo.setPeminjaman(pinjam);
            vo.setAnggota(anggota);
            vo.setBuku(buku);
            
            // Tambahkan ke daftar respons
            responseList.add(vo);
        }
        
        return responseList;
    }

}
