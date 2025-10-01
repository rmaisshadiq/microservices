package com.rafi.buku_service.service.queries;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafi.buku_service.model.read.BukuDocument;
import com.rafi.buku_service.repository.BukuDocumentRepository;

@Service
public class BukuQueryHandler {

    @Autowired
    private BukuDocumentRepository bukuDocumentRepository;

    public List<BukuDocument> handleGetAllBuku() {
        return bukuDocumentRepository.findAll();
    }

    public Optional<BukuDocument> handleGetBukuById(Long id) {
        return bukuDocumentRepository.findById(id);
    }
}
