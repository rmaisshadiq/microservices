package com.rafi.buku_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafi.buku_service.model.Buku;
import com.rafi.buku_service.service.BukuService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/buku")
public class BukuController {
    @Autowired
    private BukuService bukuService;

    @GetMapping
    public List<Buku> getAllBukus() {
        return bukuService.getAllBukus();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buku> getBukuById(@PathVariable Long id) {
        Buku buku = bukuService.getBukuById(id);
        return buku != null ? ResponseEntity.ok(buku) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Buku createBuku(@RequestBody Buku buku) {
        return bukuService.createBuku(buku);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBuku(@PathVariable long id) {
        bukuService.deleteBuku(id);
        return ResponseEntity.ok().build();
    }
    
}
