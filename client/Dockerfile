# client/Dockerfile

# Only runtime: serve your already-built static files
FROM nginx:alpine

# Copy output of `npm run build` (dist/) into nginx
COPY dist/ /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
