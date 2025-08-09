-- Creación de la base de datos y tablas básicas
-- Nota: Flyway ejecuta en el esquema actual; asegúrate de apuntar a la BD forohub en application.properties

CREATE TABLE IF NOT EXISTS Perfil (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correoElectronico VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    perfiles INT,
    CONSTRAINT fk_usuario_perfil FOREIGN KEY (perfiles) REFERENCES Perfil(id)
);

CREATE TABLE IF NOT EXISTS Curso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Topico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    mensaje TEXT NOT NULL,
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'activo',
    autor INT NOT NULL,
    curso INT NOT NULL,
    respuestas INT DEFAULT 0,
    CONSTRAINT fk_topico_autor FOREIGN KEY (autor) REFERENCES Usuario(id),
    CONSTRAINT fk_topico_curso FOREIGN KEY (curso) REFERENCES Curso(id)
);

CREATE TABLE IF NOT EXISTS Respuesta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mensaje TEXT NOT NULL,
    topico INT NOT NULL,
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    autor INT NOT NULL,
    solucion BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_respuesta_topico FOREIGN KEY (topico) REFERENCES Topico(id),
    CONSTRAINT fk_respuesta_autor FOREIGN KEY (autor) REFERENCES Usuario(id)
);
