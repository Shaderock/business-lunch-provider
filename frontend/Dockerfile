FROM node:16.17.0
ARG BACKEND_URL
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
EXPOSE 4173
ENV BACKEND_URL=${BACKEND_URL}
CMD ["npm", "run", "preview", "--", "--host"]
