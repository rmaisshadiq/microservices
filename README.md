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
| **Elasticsearch** | `9200` | Log Indexing & Storage |

---

## 📂 Project Structure (Monorepo)

```bash
├── anggota-service/       # Source code for Member Service
├── buku-service/          # Source code for Book Service
├── api-gateway/           # Source code for Gateway
├── peminjaman-service/    # Source code for Peminjaman Service
├── pengembalian-service/  # Source code for Pengembalian Service
├── jenkins-setup/         # Jenkins Configuration
│   ├── jenkins_data/      # Persisted Jenkins data
│   ├── Dockerfile
│   └── docker-compose.yml
├── elk-stack/             # ELK Configuration
│   ├── pipeline/          # Logstash config
│   └── docker-compose-elk.yml
├── monitoring/            # Prometheus & Grafana Configuration
│   ├── prometheus.yml
│   └── docker-compose-monitoring.yml
├── docker-compose.yml # Database & Broker (Mongo, Kafka, RabbitMQ)
└── README.md
```

---

## 🛠️ Installation & Setup

### Prerequisites

* Docker & Docker Compose installed.
* Minimum 16GB RAM recommended (for full stack running).

### Step 1: Start Infrastructure (Database & Brokers)

First, spin up the foundation services (MongoDB, Kafka, RabbitMQ, Eureka).

```bash
docker-compose -f docker-compose.yml up -d
```

### Step 2: Start Jenkins (CI/CD)

Run the Jenkins server to handle the build process.

```bash
cd jenkins-setup
docker-compose -f docker-compose.yml up -d
```

* **Initial Setup:** Access localhost:9090, retrieve the initial admin password from container logs, and install recommended plugins + Docker Pipeline plugin.
* **Pipeline Setup:** Create a new "Pipeline" job and point it to the Service Git Repository.

### Step 3: Start Observability Stack (Optional but Recommended)

To enable Logging (ELK) and Monitoring (Prometheus/Grafana).

```bash
# Start ELK (Logging)
cd elk-stack
docker-compose -f docker-compose-elk.yml up -d

# Start Monitoring (Metrics)
cd monitoring
docker-compose -f docker-compose-monitoring.yml up -d
```

---

## ⚙️ Configuration Snippets

### Jenkins Pipeline Example (Jenkinsfile)

The pipeline automatically builds the JAR, creates a Docker Image, and deploys the container with dynamic Environment Variables.

```groovy
pipeline {
    agent any
    environment {
        IMAGE_NAME = "buku-service"
    }
    stages {
        stage('Build JAR') {
            steps { sh 'mvn clean package -DskipTests' }
        }
        stage('Build & Deploy') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:latest ."
                sh """
                    docker run -d --name ${IMAGE_NAME} \
                    --network microservices-net \
                    -e SPRING_PROFILES_ACTIVE=prod \
                    -v volume_data:/data \
                    ${IMAGE_NAME}:latest
                """
            }
        }
    }
}
```

### Prometheus Config (prometheus.yml)

Configured to scrape metrics from Spring Boot Actuator endpoints.

```yaml
scrape_configs:
  - job_name: 'spring-boot-services'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets:
        - 'buku-service:8085'
        - 'anggota-service:8084'
        - 'peminjaman-service:8083'
        - 'pengembalian-service:8086'
        - 'eureka-server:8761'
        - 'API-GATEWAY:9000'
```

---

## 👨‍💻 Author

### Rafi Maisshadiq

* Mahasiswa Program Studi D4-Teknologi Rekayasa Perangkat Lunak di Politeknik Negeri Padang
