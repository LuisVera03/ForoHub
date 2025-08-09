# ForoHub API

API REST de un foro con autenticación JWT, registro de usuarios con contraseña hasheada y CRUD de tópicos.

## Requisitos
- Java 17+
- Maven
- MySQL 8+

## Configuración
1. Crea la base de datos `forohub` o deja que Flyway la use si la URL tiene `createDatabaseIfNotExist=true`.
2. Ajusta credenciales en `src/main/resources/application.properties`.
3. Inicia la app con el comando `mvn spring-boot:run`.

## Autenticación
- Registro: `POST /auth/register`
  Body JSON:
  {
    "nombre": "Ada",
    "correoElectronico": "ada@example.com",
    "contrasena": "Secreta123"
  }
  Respuesta 200:
  {
    "token": "<JWT>",
    "type": "Bearer"
  }

- Login: `POST /auth/login`
  Body JSON:
  {
    "correoElectronico": "ada@example.com",
    "contrasena": "Secreta123"
  }
  Respuesta 200: igual al registro.

Usa el token en el header:
Authorization: Bearer <JWT>

## Tópicos
- Listar: `GET /topicos`
  Respuesta 200 (paginado):
  {
    "content": [
      {
        "id": 1,
        "titulo": "Duda JPA",
        "mensaje": "¿Cómo paginar?",
        "fechaCreacion": "2025-08-08T10:00:00",
        "status": "activo",
        "autor": "Ada",
        "curso": "Spring",
        "respuestas": 0
      }
    ],
    "pageable": { ... }
  }

- Listar con filtros opcionales: `GET /topicos?curso=Spring&anio=2025`
  - Query params: `curso` (contiene, case-insensitive), `anio` (YYYY)
  - Paginación: `page`, `size`, `sort` (por ejemplo: `?page=0&size=5&sort=fechaCreacion,asc`)

- Detalle: `GET /topicos/{id}`
  Respuesta 200: objeto TopicoResponse

- Crear: `POST /topicos`
  Headers: Authorization: Bearer <JWT>
  Body JSON:
  {
    "titulo": "Mi primer tópico",
    "mensaje": "Hola foro",
    "autorId": 1,
    "cursoId": 1
  }
  Respuestas: 201 con Location y objeto creado; 400 si duplicado o ids inválidos.

- Actualizar: `PUT /topicos/{id}`
  Headers: Authorization: Bearer <JWT>
  Body JSON:
  {
    "titulo": "Título actualizado",
    "mensaje": "Mensaje actualizado",
    "cursoId": 1,
    "status": "activo"
  }
  Respuestas: 200 actualizado; 404 si no existe; 400 si curso inválido.

- Eliminar: `DELETE /topicos/{id}`
  Headers: Authorization: Bearer <JWT>
  Respuestas: 200; 404 si no existe.

## Uso en Postman
1. Crea una colección "ForoHub".
2. Agrega petición POST /auth/register con Body JSON (raw) y guarda el token devuelto para usarlo en las siguientes consultas.
3. Agrega POST /auth/login similar y mismo test para actualizar `token`.
4. En la colección, en Auth selecciona "Bearer Token" y coloca `{{token}}`. Así todas las peticiones usarán el token.
5. Crea peticiones para cada endpoint. Para crear y actualizar tópicos, usa Body raw JSON como en los ejemplos. Asegura que existan `autorId` (usuario creado) y `cursoId` (crea registros en la tabla Curso).
6. Si no tienes cursos, inserta uno manualmente (SQL) o crea con migración ya incluida (V2 carga "Spring" y "Java").

## Notas
- Endpoints públicos: `/auth/register`, `/auth/login`. El resto requiere token.

