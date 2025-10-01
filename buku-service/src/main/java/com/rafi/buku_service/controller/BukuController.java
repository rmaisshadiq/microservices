package com.rafi.buku_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafi.buku_service.model.Buku;
import com.rafi.buku_service.model.read.BukuDocument;
import com.rafi.buku_service.service.commands.BukuCommandHandler;
import com.rafi.buku_service.service.queries.BukuQueryHandler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/buku")
public class BukuController {
    @Autowired
    private BukuCommandHandler commandHandler;

    @Autowired
    private BukuQueryHandler queryHandler;

    @GetMapping
    public ResponseEntity<List<BukuDocument>> getAllBukus() {
        return ResponseEntity.ok(queryHandler.handleGetAllBuku());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BukuDocument> getBukuById(@PathVariable Long id) {
        return queryHandler.handleGetBukuById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Buku> createBuku(@RequestBody Buku buku) {
        Buku createdBuku = commandHandler.handleCreateBuku(buku);
        return new ResponseEntity<>(createdBuku, HttpStatus.CREATED);
    }
    
}
