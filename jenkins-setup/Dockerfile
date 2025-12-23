# Kita mulai dari image Jenkins resmi
FROM jenkins/jenkins:lts-jdk17

# Ganti user ke ROOT dulu biar bisa install aplikasi (sudo)
USER root

# Update Linux dan Install Docker CLI
RUN apt-get update && \
    apt-get install -y docker.io

# (Opsional tapi bagus) Bersihkan cache biar image enteng
RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# PENTING: Kembalikan user ke 'jenkins' agar aman
# TAPI, karena di docker-compose kita set user: root, baris ini opsional.
# Namun best practice-nya begini.
# USER jenkins