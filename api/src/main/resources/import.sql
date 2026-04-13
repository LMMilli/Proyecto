-- Insertar equipamiento
INSERT INTO equipamiento (nombre) VALUES ('Peso Corporal');
INSERT INTO equipamiento (nombre) VALUES ('Barra');
INSERT INTO equipamiento (nombre) VALUES ('Mancuerna');
INSERT INTO equipamiento (nombre) VALUES ('Máquina');

-- Insertar un par de ejercicios de ejemplo
INSERT INTO ejercicios (nombre, grupo_muscular, descripcion) VALUES ('Sentadilla', 'Piernas', 'Flexión de rodillas y cadera');
INSERT INTO ejercicios (nombre, grupo_muscular, descripcion) VALUES ('Press Banca', 'Pecho', 'Empuje horizontal en banco plano');

-- Vincular la Sentadilla (ID 1) con Peso Corporal (1), Barra (2) y Máquina (4)
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (1, 1);
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (1, 2);
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (1, 4);

-- Vincular el Press Banca (ID 2) con Barra (2) y Mancuerna (3)
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (2, 2);
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (2, 3);