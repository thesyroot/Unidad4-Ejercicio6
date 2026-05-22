# Unidad 4 - Ejercicio 6

API REST para gestion de insumos con sincronizacion automatica de precios via cronjob.

## Funcionalidades

- CRUD de insumos (crear, obtener, eliminar soft-delete, recuperar)
- Sincronizacion programada de precios: cada hora de lunes a viernes actualiza `valorDolarReferencia` y recalcula `precioEnPesos` segun el dolar oficial
- Base de datos H2 (memoria en desarrollo, archivo persistente en produccion)

## Endpoints

| Metodo | URL | Descripcion |
|--------|-----|-------------|
| POST | `/insumos` | Crear insumo |
| GET | `/insumos/{id}` | Obtener insumo activo |
| DELETE | `/insumos/{id}` | Soft-delete (desactiva) |
| PUT | `/insumos/{id}/recovery` | Recuperar insumo desactivado |

## Tecnologias

- Java 21, Spring Boot 4.0.3
- Spring Data JPA, H2 Database
- Spring Security, Scheduler
- JUnit 5, Mockito
- Docker

## Docker

```bash
docker build -t unidad4-ej6 .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=production unidad4-ej6
```

## Despliegue en Render

Conectar el repositorio de GitHub y seleccionar Docker como entorno de ejecucion.
