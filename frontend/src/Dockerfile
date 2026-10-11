FROM node:18-alpine AS build

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY . .

RUN npm run build -- --configuration production

# ────────── STAGE 2: Nginx ──────────
FROM nginx:alpine


COPY --from=build /app/dist/jigsawsudoku/browser /usr/share/nginx/html

COPY nginx.conf /etc/nginx/nginx.conf


EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
