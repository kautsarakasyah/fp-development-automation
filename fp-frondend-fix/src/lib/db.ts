
import { Pool } from 'pg';

if (!process.env.POSTGRES_URL) {
  throw new Error('Variabel lingkungan POSTGRES_URL tidak ditemukan.');
}

export const pool = new Pool({
  connectionString: process.env.POSTGRES_URL,
});
