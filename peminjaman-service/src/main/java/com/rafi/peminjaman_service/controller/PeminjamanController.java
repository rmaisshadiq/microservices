package com.rafi.peminjaman_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafi.peminjaman_service.model.Peminjaman;
import com.rafi.peminjaman_service.service.PeminjamanService;
import com.rafi.peminjaman_service.vo.ResponseTemplate;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/peminjaman")
public class PeminjamanController {
    @Autowired
    private PeminjamanService peminjamanService;

    @GetMapping
    public List<Peminjaman> getAllPeminjamans() {
        return peminjamanService.getAllPeminjamans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peminjaman> getPeminjamanById(@PathVariable Long id) {
        Peminjaman peminjaman = peminjamanService.getPeminjamanById(id);
        return peminjaman != null ? ResponseEntity.ok(peminjaman) : ResponseEntity.notFound().build();
    }

    @GetMapping("/anggota/{id}")
    public ResponseEntity<List<ResponseTemplate>> getPeminjamanWithAnggotaById(@PathVariable Long id) {
        List<ResponseTemplate> responseTemplate = peminjamanService.getAllPeminjamanWithAnggotaAndBuku(id);
        return responseTemplate != null ? ResponseEntity.ok(responseTemplate): ResponseEntity.notFound().build();
    }

    @PostMapping
    public Peminjaman createPeminjaman(@RequestBody Peminjaman peminjaman) {
        return peminjamanService.createPeminjaman(peminjaman);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePeminjaman(@PathVariable long id) {
        peminjamanService.deletePeminjaman(id);
        return ResponseEntity.ok().build();
    }
    
}
