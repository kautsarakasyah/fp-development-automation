FROM postgres:14

# Atur environment variable PGDATA ke lokasi yang *sudah* di-support oleh base image
ENV PGDATA=/var/lib/postgresql/data

# Jangan lakukan mkdir atau chmod manual
# Tidak perlu entrypoint tambahan — biarkan base image PostgreSQL yang handle semuanya
