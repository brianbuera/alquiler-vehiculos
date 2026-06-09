# API REST — Sistema de Alquiler de Vehículos

Proyecto desarrollado con Java y Spring Boot para la gestión de alquiler de vehículos mediante una API REST.

## Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* H2 Database
* Maven

---

# Arquitectura

La aplicación utiliza una arquitectura monolítica basada en capas:

* Controller
* Service
* Repository
* Model
* DTO
* Validator
* Mapper

---

# Funcionalidades principales

## Clientes

* Crear clientes
* Listar clientes
* Buscar cliente por ID
* Actualizar clientes
* Eliminar clientes
* Validación de email único

## Vehículos

* Crear vehículos
* Listar vehículos
* Buscar vehículos disponibles
* Actualizar vehículos
* Eliminar vehículos

## Reservas

* Crear reservas
* Validar disponibilidad por fechas
* Historial de reservas por cliente
* Historial de reservas por vehículo
* Cálculo automático de precios
* Descuentos según tipo de cliente
* Cargo adicional por TelePASE ilimitado

---

# Reglas de negocio

* Un vehículo no puede reservarse en fechas ocupadas.
* Clientes PREMIUM reciben descuentos.
* El TelePASE ilimitado agrega un cargo adicional fijo.
* El precio total se calcula automáticamente en el backend.

---

# Base de datos

Se utiliza H2 Database.

Acceso a consola H2:

```text
http://localhost:8080/h2-console
```

Configuración:

```text
JDBC URL: jdbc:h2:file:./data/clientedb
User: sa
Password:
```

---

# Ejecución del proyecto

## Clonar repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
```

---

## Ejecutar aplicación

```bash
mvn spring-boot:run
```

La API quedará disponible en:

```text
http://localhost:8080
```

---

# Endpoints principales

## Clientes

```http
GET /clientes
POST /clientes
GET /clientes/{id}
PUT /clientes/{id}
DELETE /clientes/{id}
```

---

## Vehículos

```http
GET /vehiculos
POST /vehiculos
GET /vehiculos/{id}
PUT /vehiculos/{id}
DELETE /vehiculos/{id}
```

---

## Reservas

```http
GET /reservas
POST /reservas
GET /reservas/{id}
DELETE /reservas/{id}
```

---

# Autor

Brian Buera
