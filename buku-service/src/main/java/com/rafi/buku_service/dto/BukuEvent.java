package com.rafi.buku_service.dto;

import com.rafi.buku_service.model.Buku;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BukuEvent {

    private String eventType;
    private Buku buku;

}
