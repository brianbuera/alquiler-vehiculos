# AutoFlow — Frontend del sistema de alquiler de vehículos

Interfaz web en **HTML, CSS y JavaScript puro**, diseñada para la API REST Java / Spring Boot que compartiste. No utiliza React, Node ni un proceso de compilación.

## Funcionalidades

- **Dashboard:** cantidad de vehículos, disponibles, clientes y reservas; últimas reservas y estado general de la flota.
- **Vehículos:** listar, buscar, filtrar por categoría, crear, editar y eliminar.
- **Clientes:** listar, buscar, crear, editar y eliminar.
- **Reservas:** listar, buscar, consultar precio de una reserva, eliminar, buscar vehículos disponibles por fechas y crear una reserva (incluido telepase ilimitado).
- **Interfaz adaptable:** escritorio y celular.
- **Docker + Nginx:** el navegador utiliza `/api/...`; Nginx reenvía a `http://alquiler-api:8080/...` dentro de la red de Docker Compose. Así no es necesario activar CORS en Spring Boot para este uso.

## Cómo usarlo con TU proyecto

### 1. Descomprimir

Ubicá esta carpeta **`autoflow-frontend`** al mismo nivel que el `compose.yaml` de tu proyecto Java. No hace falta mover tu código Spring Boot.

```text
alquiler-vehiculos/
├── compose.yaml                 <- ya existe
├── Dockerfile                   <- backend Java, ya existe
├── pom.xml                      <- ya existe
├── src/                         <- ya existe
├── .env                         <- ya existe
└── autoflow-frontend/           <- carpeta descargada
    ├── index.html
    ├── css/styles.css
    ├── js/app.js
    ├── nginx.conf
    ├── Dockerfile
    └── README.md
```

### 2. Agregar un tercer servicio al `compose.yaml` EXISTENTE

**Dentro de `services:`**, junto a `alquiler-api:` y `postgres:`, agregá:

```yaml
  frontend:
    build: ./autoflow-frontend
    image: autoflow-frontend:1.0
    ports:
      - "3000:80"
    depends_on:
      - alquiler-api
    restart: unless-stopped
```

Mantené sin cambios los servicios Java y PostgreSQL y sus volúmenes. Si ya tenés otro servicio usando el puerto 3000, cambiá el primer valor, por ejemplo `"3001:80"`.

### 3. Iniciar

Desde PowerShell, en la carpeta donde está `compose.yaml`:

```powershell
docker compose up -d --build
docker compose ps
```

Abrí **http://localhost:3000** en el navegador.

Para reconstruir solo el frontend después de modificar HTML, CSS o JS:

```powershell
docker compose up -d --build frontend
```

No se modifican ni eliminan los datos del volumen de PostgreSQL al reconstruir el frontend. **No ejecutes `docker compose down -v`** si querés conservar los datos.

## Rutas reales utilizadas (basadas en tus controllers)

| Recurso | Métodos / endpoints |
|---|---|
| Clientes | `GET /clientes`, `POST /clientes`, `PUT /clientes/{id}`, `DELETE /clientes/{id}` |
| Vehículos | `GET /vehiculos`, `POST /vehiculos`, `PUT /vehiculos/{id}`, `DELETE /vehiculos/{id}` |
| Disponibilidad | `GET /vehiculos/buscar/por-fechas?inicio=AAAA-MM-DD&fin=AAAA-MM-DD` |
| Reservas | `GET /reservas`, `POST /reservas`, `DELETE /reservas/{id}` |

El formulario `POST /reservas` manda los campos exactos de `ReservaRequest`: `clienteId`, `vehiculoId`, `fechaInicio`, `fechaFin`, `telepaseIlimitado`. El detalle consume los campos de `ReservaResponseDTO`, incluido `reservaId`, `precioBase`, `cargoTelepaseIlimitado`, `porcentajeDescuento` y `precioTotal`.

**No incluye edición de reservas:** el `ReservaController` proporcionado no tiene `PUT /reservas/{id}`.

## Preguntas frecuentes

**¿Se puede abrir `index.html` haciendo doble clic?** Se abre la interfaz, pero el CRUD no funcionará de esa manera. Para comunicarla con la API, ejecutala con Docker Compose y Nginx. No hace falta configurar CORS.

**¿Necesito copiar la base de datos dentro del frontend?** No. La API accede a PostgreSQL y el frontend habla con la API. La información sigue persistiendo en `postgres-datos`.

**¿Puedo subir el frontend a Docker Hub?** Sí. Después de `docker compose up -d --build`, etiquetá `autoflow-frontend:1.0` con tu usuario y usá `docker push`. Para la otra PC también necesitás el `compose.yaml`, las variables `.env` y el respaldo de PostgreSQL si querés conservar los registros actuales.

**¿Sirve para producción pública?** Todavía no: es un prototipo de administración **sin autenticación ni control de permisos**. Ejecutalo localmente. Para exponerlo a Internet es necesario incorporar seguridad, autorización, HTTPS y controles adicionales.

## Requisitos

- Docker Desktop / Docker Engine + Docker Compose.
- Servicio Compose `alquiler-api` en el puerto interno 8080.
- El backend tiene que estar conectado a PostgreSQL y exponer los endpoints indicados.
- Internet solo para descargar la fuente tipográfica opcional; la interfaz usa fuentes de respaldo sin ella.
