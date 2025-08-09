# ForoHub API

API REST de un foro con autenticación JWT, registro seguro (contraseña en BCrypt) y CRUD de tópicos.

## Requisitos
- Java 17+
- Maven
- MySQL 8+

## Configuración rápida (local)
1) Base de datos: la URL ya incluye `createDatabaseIfNotExist=true`. Si MySQL está corriendo, se creará el esquema `forohub` al iniciar.
2) Credenciales MySQL: ajusta en `src/main/resources/application.properties` si no usas las por defecto.
   - Usuario por defecto en este repo: `root` / `root`.
   - La URL incluye `allowPublicKeyRetrieval=true` para evitar errores con MySQL 8.
3) JWT: cambia `jwt.secret` por un valor fuerte para producción.

## Cómo ejecutar
- Windows (PowerShell):
  - `./mvnw.cmd spring-boot:run`
- Linux/macOS:
  - `./mvnw spring-boot:run`

La API queda en `http://localhost:8080`.

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
  Posibles errores:
  - 400: "El correo ya está registrado".

- Login: `POST /auth/login`
  Body JSON:
  {
    "correoElectronico": "ada@example.com",
    "contrasena": "Secreta123"
  }
  Respuesta 200: igual al registro.

Usa el token en el header:
Authorization: Bearer <JWT>

Endpoints públicos: `/auth/**`. Los demás requieren JWT.

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

- Filtros y paginación: `GET /topicos?curso=Spring&anio=2025&page=0&size=5&sort=fechaCreacion,asc`
  - `curso`: contiene (case-insensitive)
  - `anio`: año de creación (YYYY)
  - `page`, `size`, `sort`: paginación estándar de Spring Data

- Detalle: `GET /topicos/{id}` → 200 con objeto `TopicoResponse` o 404 si no existe.

- Crear: `POST /topicos`
  Headers: Authorization: Bearer <JWT>
  Body JSON:
  {
    "titulo": "Mi primer tópico",
    "mensaje": "Hola foro",
    "autorId": 1,
    "cursoId": 1
  }
  Respuestas:
  - 201: creado (con Location)
  - 400: tópico duplicado (título+mensaje) o IDs inválidos

- Actualizar: `PUT /topicos/{id}`
  Headers: Authorization: Bearer <JWT>
  Body JSON:
  {
    "titulo": "Título actualizado",
    "mensaje": "Mensaje actualizado",
    "cursoId": 1,
    "status": "activo"
  }
  Respuestas: 200 actualizado; 404 si no existe; 400 si `cursoId` inválido.

- Eliminar: `DELETE /topicos/{id}`
  Headers: Authorization: Bearer <JWT>
  Respuestas: 200; 404 si no existe.

## Uso en Postman
1) Crea una colección "ForoHub".
2) POST `/auth/register` con Body JSON (raw). En la pestaña Tests agrega este script para guardar el token en variable de colección:
   const data = pm.response.json();
   pm.collectionVariables.set("token", data.token);
3) POST `/auth/login` similar, reutiliza el mismo script para refrescar `{{token}}`.
4) En la colección, ve a Auth → "Bearer Token" → Token: `{{token}}` (todas las peticiones heredarán el token).
5) Crea requests para `GET/POST/PUT/DELETE /topicos`. Para crear/actualizar, asegúrate de usar IDs válidos de usuario y curso. La migración V2 ya carga cursos "Spring" y "Java".

## Migraciones y modelo
- Flyway crea tablas: `Perfil`, `Usuario`, `Curso`, `Topico`, `Respuesta`.
- `Usuario` guarda `contrasena` con BCrypt.
- `Topico` evita duplicados por combinación `titulo + mensaje` (validado en la capa de servicio/controlador).

## Problemas comunes
- 403 en /auth/register o /auth/login: verifica que no estés enviando un Authorization inválido, que el Body sea JSON, y que la URL sea correcta (`/auth/...`). CORS está habilitado.
- Error MySQL "Unknown column 'correo_electronico'": ya se configuró la estrategia de nombres para respetar camelCase (propiedad `spring.jpa.hibernate.naming.physical-strategy`). Asegúrate de haber reiniciado la app.
- "Public Key Retrieval is not allowed": la URL JDBC incluye `allowPublicKeyRetrieval=true`. Si cambias la URL, conserva ese parámetro para MySQL 8.

## Autor

**Luis Alejandro Vera**
- GitHub: [@LuisVera03](https://github.com/LuisVera03)