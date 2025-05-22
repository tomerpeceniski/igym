# frontend/Dockerfile

# 1) Build stage: install deps & produce static assets
FROM node:18-alpine AS builder
WORKDIR /app
# copy only package.json, install deps
COPY package.json package-lock.json* ./
RUN npm install
COPY . .
RUN npm run build

# 2) Runtime stage: serve with nginx
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
