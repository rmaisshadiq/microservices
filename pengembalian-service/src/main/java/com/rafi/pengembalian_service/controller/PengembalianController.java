package com.rafi.pengembalian_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafi.pengembalian_service.model.Pengembalian;
import com.rafi.pengembalian_service.service.PengembalianService;
import com.rafi.pengembalian_service.vo.ResponseTemplate;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/pengembalian")
public class PengembalianController {
    @Autowired
    private PengembalianService pengembalianService;

    @GetMapping
    public List<Pengembalian> getAllPengembalians() {
        return pengembalianService.getAllPengembalians();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pengembalian> getPengembalianById(@PathVariable Long id) {
        Pengembalian pengembalian = pengembalianService.getPengembalianById(id);
        return pengembalian != null ? ResponseEntity.ok(pengembalian) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/peminjaman/{id}")
    public ResponseEntity<List<ResponseTemplate>> getPengembalianWithPeminjamanById(@PathVariable Long id) {
        List<ResponseTemplate> responseTemplate = pengembalianService.getAllPengembalianWithPeminjaman(id);
        return responseTemplate != null ? ResponseEntity.ok(responseTemplate): ResponseEntity.notFound().build();
    }

    @PostMapping
    public Pengembalian createPengembalian(@RequestBody Pengembalian pengembalian) {
        return pengembalianService.createPengembalian(pengembalian);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePengembalian(@PathVariable long id) {
        pengembalianService.deletePengembalian(id);
        return ResponseEntity.ok().build();
    }
    
}
