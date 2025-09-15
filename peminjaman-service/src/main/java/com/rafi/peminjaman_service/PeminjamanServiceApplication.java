package com.rafi.peminjaman_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class PeminjamanServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeminjamanServiceApplication.class, args);
	}

	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
