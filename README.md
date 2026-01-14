# 📚 Perpustakaan Microservices System
> **DevOps & Observability Edition**

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=spring-boot)
![Docker](https://img.shields.io/badge/Docker-Container-blue?style=for-the-badge&logo=docker)
![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-red?style=for-the-badge&logo=jenkins)
![ELK Stack](https://img.shields.io/badge/ELK-Logging-yellow?style=for-the-badge&logo=elastic)
![Grafana](https://img.shields.io/badge/Grafana-Monitoring-orange?style=for-the-badge&logo=grafana)

## 📖 Overview

This project is a comprehensive implementation of a **Library Management System** using **Microservices Architecture**. Beyond basic functionality, this repository demonstrates a full **DevOps Lifecycle** implementation, including automated CI/CD pipelines, centralized logging, and real-time infrastructure monitoring.

### 🏗 Architecture
The system is composed of loose-coupled services communicating via REST APIs and Message Brokers (Kafka/RabbitMQ), fully containerized using Docker.

* **Service Discovery:** Netflix Eureka
* **API Gateway:** Spring Cloud Gateway
* **Services:** Buku (Book), Anggota (Member), Peminjaman (Loan), Pengembalian (Return)
* **Databases:** MongoDB & H2
* **Messaging:** Apache Kafka & RabbitMQ

---

## 🚀 Tech Stack & Infrastructure

### Application Layer
| Service | Port | Description | DB / Tech |
| :--- | :--- | :--- | :--- |
| **API Gateway** | `9000` | Entry point for all requests | Spring Cloud Gateway |
| **Eureka Server** | `8761` | Service Registry & Discovery | Netflix Eureka |
| **Buku Service** | `8085` | Manage Books Data | MongoDB |
| **Anggota Service** | `8084` | Manage Members Data | H2 Database |
| **Other Services** | `808x` | Loan, Return, Auth, etc. | MySQL/PostgreSQL |

### DevOps & Observability Layer
| Tool | Access URL | Purpose |
| :--- | :--- | :--- |
| **Jenkins** | `http://localhost:9090` | **CI/CD Pipeline** (Automated Build & Deploy) |
| **Grafana** | `http://localhost:3000` | **Monitoring Dashboard** (Metrics Visualization) |
| **Prometheus** | `http://localhost:9091` | **Metrics Collector** (Scraping Spring Actuator) |
| **Kibana** | `http://localhost:5601` | **Centralized Logging** (Log Visualization) |
| **Logstash** | `TCP: 5000` | Log Processing Pipeline |
| **Elasticsearch**| `9200` | Log Indexing & Storage |

---

## 📂 Project Structure (Monorepo)

```bash
├── anggota-service/       # Source code for Member Service
├── buku-service/          # Source code for Book Service
├── api-gateway/           # Source code for Gateway
├── peminjaman-service/    # Source code for Peminjaman Service
├── pengembalian-service/  # Source code for Pengembalian Service
├── jenkins-data/          # Persisted Jenkins data
├── elk-stack/             # ELK Configuration
│   ├── pipeline/          # Logstash config
│   └── docker-compose-elk.yml
├── monitoring/            # Prometheus & Grafana Configuration
│   ├── prometheus.yml
│   └── docker-compose-monitoring.yml
├── docker-compose-infra.yml # Database & Broker (Mongo, Kafka, RabbitMQ)
├── jenkins-compose.yml      # Jenkins Server Setup
└── README.md

