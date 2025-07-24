# Stage 1: Builder
FROM node:20-alpine AS builder

WORKDIR /app

COPY package*.json ./
COPY tsconfig.json ./
COPY next.config.js ./
COPY . .

RUN npm install
RUN npm run build

# Stage 2: Final image
FROM node:20-alpine AS runner

WORKDIR /app
ENV NODE_ENV=production

# Salin hanya file yang dibutuhkan
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package*.json ./
COPY --from=builder /app/next.config.js ./
COPY --from=builder /app/tsconfig.json ./

EXPOSE 3000

# Jalankan aplikasi Next.js dalam mode production
CMD ["npm", "start"]
