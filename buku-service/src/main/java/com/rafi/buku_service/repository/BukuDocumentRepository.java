package com.rafi.buku_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rafi.buku_service.model.read.BukuDocument;

@Repository
public interface BukuDocumentRepository extends MongoRepository<BukuDocument, Long> {

}
