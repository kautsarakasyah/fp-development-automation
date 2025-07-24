-- =================================================================
-- SKEMA DATABASE UNTUK APLIKASI SIMULASI BNI (VERSI FINAL)
-- =================================================================
-- Jalankan skrip ini untuk membuat semua tabel yang diperlukan dengan benar.
-- Ini adalah versi terbaru yang kompatibel dengan kode aplikasi.
-- =================================================================

-- Menghapus tabel jika sudah ada (untuk reset yang bersih)
-- Urutan penting karena adanya foreign key constraints.
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;

-- Tabel untuk menyimpan data pengguna
-- Tabel ini akan menyimpan informasi dasar untuk login dan identifikasi.
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabel untuk menyimpan informasi akun dan saldo pengguna
-- Terhubung ke tabel 'users' melalui user_id.
CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    balance NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabel untuk menyimpan riwayat semua transaksi
-- Terhubung ke tabel 'users' untuk mengetahui siapa yang melakukan transaksi.
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    nominal NUMERIC(15, 2) NOT NULL,
    status VARCHAR(20) NOT NULL, -- e.g., 'Success', 'Failed'
    message VARCHAR(255) -- << KUNCI PERBAIKAN: Kolom untuk menyimpan pesan error dari mitra
);

-- Memberi tahu bahwa skrip telah selesai
SELECT 'Skema database (vFinal) berhasil dibuat!' as "Status";
