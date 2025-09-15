package com.rafi.anggota_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafi.anggota_service.model.Anggota;
import com.rafi.anggota_service.service.AnggotaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/anggota")
public class AnggotaController {
    @Autowired
    private AnggotaService anggotaService;

    @GetMapping
    public List<Anggota> getAllAnggotas() {
        return anggotaService.getAllAnggotas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anggota> getAnggotaById(@PathVariable Long id) {
        Anggota anggota = anggotaService.getAnggotaById(id);
        return anggota != null ? ResponseEntity.ok(anggota) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Anggota createAnggota(@RequestBody Anggota anggota) {
        return anggotaService.createAnggota(anggota);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAnggota(@PathVariable long id) {
        anggotaService.deleteAnggota(id);
        return ResponseEntity.ok().build();
    }
    
}
