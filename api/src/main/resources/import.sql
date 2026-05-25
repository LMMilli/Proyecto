-- ==========================================
-- 1. INSERTAR EQUIPAMIENTO
-- ==========================================
INSERT INTO equipamiento (id, nombre) VALUES (1, 'Peso Corporal');
INSERT INTO equipamiento (id, nombre) VALUES (2, 'Barra');
INSERT INTO equipamiento (id, nombre) VALUES (3, 'Mancuerna');
INSERT INTO equipamiento (id, nombre) VALUES (4, 'Máquina');

-- ==========================================
-- 2. INSERTAR EJERCICIOS (Catálogo completo)
-- ==========================================

-- PIERNAS
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (1, 'Sentadilla', 'Piernas', 'Flexión profunda de rodillas y cadera manteniendo la espalda recta.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (2, 'Peso Muerto', 'Piernas y Espalda Baja', 'Levantamiento de peso desde el suelo extendiendo cadera y rodillas.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (3, 'Zancada (Lunge)', 'Piernas', 'Paso hacia adelante flexionando ambas rodillas a 90 grados.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (4, 'Hip Thrust', 'Glúteos', 'Elevación de cadera apoyando la parte superior de la espalda en un banco.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (5, 'Prensa', 'Piernas', 'Empuje de plataforma con las piernas desde una posición sentada.');

-- PECHO
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (6, 'Press Banca Plano', 'Pecho', 'Empuje horizontal en banco plano.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (7, 'Press Banca Inclinado', 'Pecho', 'Empuje en banco inclinado, enfocando en el haz clavicular (pecho superior).');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (8, 'Aperturas', 'Pecho', 'Movimiento de aducción de brazos tumbado en un banco.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (9, 'Flexiones', 'Pecho y Tríceps', 'Empuje del propio peso corporal desde el suelo.');

-- ESPALDA
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (10, 'Dominadas', 'Espalda', 'Tracción vertical del propio peso corporal colgado de una barra.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (11, 'Remo con Barra', 'Espalda', 'Tracción horizontal inclinando el torso hacia adelante.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (12, 'Jalón al Pecho', 'Espalda', 'Tracción vertical en polea hacia la parte superior del pecho.');

-- HOMBROS
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (13, 'Press Militar', 'Hombros', 'Empuje vertical por encima de la cabeza.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (14, 'Elevaciones Laterales', 'Hombros', 'Abducción de los brazos hacia los lados para aislar el deltoides lateral.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (15, 'Pájaros', 'Hombros', 'Elevaciones posteriores con el torso inclinado para el deltoides posterior.');

-- BRAZOS (BÍCEPS / TRÍCEPS)
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (16, 'Curl con Barra', 'Bíceps', 'Flexión de codos de pie sujetando el peso con ambas manos.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (17, 'Curl Martillo', 'Bíceps', 'Flexión de codos con agarre neutro (palmas enfrentadas).');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (18, 'Extensión de Tríceps', 'Tríceps', 'Extensión de codos hacia abajo utilizando una polea.');
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (19, 'Press Francés', 'Tríceps', 'Extensión de codos tumbado llevando el peso hacia la frente.');

-- CORE
INSERT INTO ejercicios (id, nombre, grupo_muscular, descripcion) VALUES (20, 'Plancha Abdominal', 'Core', 'Mantenimiento de la postura isométrica apoyado en antebrazos y pies.');

-- ==========================================
-- 3. VINCULAR EJERCICIOS CON EQUIPAMIENTO
-- (Equipamiento: 1=Peso Corporal, 2=Barra, 3=Mancuerna, 4=Máquina)
-- ==========================================

-- Sentadilla (1) -> Corporal, Barra, Máquina
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (1, 1), (1, 2), (1, 4);

-- Peso Muerto (2) -> Barra, Mancuerna
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (2, 2), (2, 3);

-- Zancada (3) -> Corporal, Barra, Mancuerna
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (3, 1), (3, 2), (3, 3);

-- Hip Thrust (4) -> Barra, Máquina
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (4, 2), (4, 4);

-- Prensa (5) -> Máquina
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (5, 4);

-- Press Banca Plano (6) -> Barra, Mancuerna, Máquina
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (6, 2), (6, 3), (6, 4);

-- Press Banca Inclinado (7) -> Barra, Mancuerna, Máquina
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (7, 2), (7, 3), (7, 4);

-- Aperturas (8) -> Mancuerna, Máquina
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (8, 3), (8, 4);

-- Flexiones (9) -> Peso Corporal
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (9, 1);

-- Dominadas (10) -> Peso Corporal, Máquina (dominadas asistidas)
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (10, 1), (10, 4);

-- Remo con Barra (11) -> Barra
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (11, 2);

-- Jalón al Pecho (12) -> Máquina
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (12, 4);

-- Press Militar (13) -> Barra, Mancuerna, Máquina
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (13, 2), (13, 3), (13, 4);

-- Elevaciones Laterales (14) -> Mancuerna, Máquina (polea)
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (14, 3), (14, 4);

-- Pájaros (15) -> Mancuerna, Máquina
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (15, 3), (15, 4);

-- Curl con Barra (16) -> Barra
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (16, 2);

-- Curl Martillo (17) -> Mancuerna
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (17, 3);

-- Extensión de Tríceps (18) -> Máquina (polea)
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (18, 4);

-- Press Francés (19) -> Barra, Mancuerna
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (19, 2), (19, 3);

-- Plancha Abdominal (20) -> Peso Corporal
INSERT INTO ejercicio_equipamiento (ejercicio_id, equipamiento_id) VALUES (20, 1);