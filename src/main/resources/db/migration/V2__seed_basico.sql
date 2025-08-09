INSERT INTO Perfil (nombre) VALUES ('USUARIO') ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- Cursos de ejemplo
INSERT INTO Curso (nombre, categoria) VALUES ('Spring', 'Backend') ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), categoria = VALUES(categoria);
INSERT INTO Curso (nombre, categoria) VALUES ('Java', 'Backend') ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), categoria = VALUES(categoria);
