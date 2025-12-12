# admin-panel-backend

Backend Spring Boot para Panel de Administración.

## Requisitos
- Java 17+
- Maven

## Variables de entorno
Copia `.example.env` a `.env` (localmente) y rellena valores:
- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- JWT_SECRET
- FRONTEND_URL

En Railway configura esas variables en Settings del proyecto.

## Ejecutar local
./mvnw spring-boot:run

## Endpoints principales
- POST /api/auth/register
- POST /api/auth/login
- POST /api/auth/refresh
- POST /api/auth/logout
- GET /api/users (ADMIN)
- GET /api/users/{id} (ADMIN)
- GET /api/me (authenticated)