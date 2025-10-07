package com.rafi.peminjaman_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafi.peminjaman_service.model.Peminjaman;
import com.rafi.peminjaman_service.model.PeminjamanQuery;
import com.rafi.peminjaman_service.repository.PeminjamanQueryRepository;
import com.rafi.peminjaman_service.service.PeminjamanProducerService;
import com.rafi.peminjaman_service.vo.ResponseTemplate;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/peminjaman")
public class PeminjamanController {

    private final PeminjamanProducerService peminjamanService;
    private final PeminjamanQueryRepository peminjamanQueryRepository;

    public PeminjamanController(PeminjamanProducerService peminjamanService, PeminjamanQueryRepository peminjamanQueryRepository) {
        this.peminjamanService = peminjamanService;
        this.peminjamanQueryRepository = peminjamanQueryRepository;
    }

    @GetMapping
    public ResponseEntity<List<ResponseTemplate>> getAllPeminjamans() {
        List<ResponseTemplate> responseTemplate = peminjamanService.getAllPeminjamans();
        return responseTemplate != null ? ResponseEntity.ok(responseTemplate): ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeminjamanQuery> getPeminjamanById(@PathVariable Long id) {
        return peminjamanQueryRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/anggota/{id}")
    public ResponseEntity<List<ResponseTemplate>> getPeminjamanWithAnggotaById(@PathVariable Long id) {
        List<ResponseTemplate> responseTemplate = peminjamanService.getAllPeminjamanWithAnggotaAndBuku(id);
        return responseTemplate != null ? ResponseEntity.ok(responseTemplate): ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Peminjaman> createPeminjaman(@RequestBody Peminjaman peminjaman) {
        Peminjaman savedPeminjaman = peminjamanService.createPeminjaman(peminjaman);
        return ResponseEntity.ok(savedPeminjaman);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePeminjaman(@PathVariable long id) {
        peminjamanService.deletePeminjaman(id);
        return ResponseEntity.ok().build();
    }
    
}
