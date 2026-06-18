BEGIN TRANSACTION;

-- Progresión lineal de armas: 2 a 8
UPDATE equipables SET poder = 2 WHERE nombre = 'DAGA';
UPDATE equipables SET poder = 3 WHERE nombre = 'DAGAS_GEMELAS';
UPDATE equipables SET poder = 4 WHERE nombre = 'ESPADA_CORTA';
UPDATE equipables SET poder = 5 WHERE nombre = 'ESPADA_LARGA';
UPDATE equipables SET poder = 6 WHERE nombre = 'BASTON_ARCANO';
UPDATE equipables SET poder = 7 WHERE nombre = 'LANZA_TEMPLADA';
UPDATE equipables SET poder = 8 WHERE nombre = 'MANDOBLE';

-- Progresión lineal de escudos: 2 a 8
UPDATE equipables SET poder = 2 WHERE nombre = 'RODELA_DE_CUERO';
UPDATE equipables SET poder = 3 WHERE nombre = 'BROQUEL_LIGERO';
UPDATE equipables SET poder = 4 WHERE nombre = 'ESCUDO_DE_MADERA';
UPDATE equipables SET poder = 5 WHERE nombre = 'ESCUDO_DE_HIERRO';
UPDATE equipables SET poder = 7 WHERE nombre = 'ESCUDO_TORRE';
UPDATE equipables SET poder = 8 WHERE nombre = 'ESCUDO_SAGRADO';

-- Añade el escalón que faltaba para no dejar huecos en la progresión
INSERT INTO equipables (nombre, tipoEquipable, poder)
SELECT 'ESCUDO_REFORZADO', 'ESCUDO', 6
WHERE NOT EXISTS (
    SELECT 1
    FROM equipables
    WHERE nombre = 'ESCUDO_REFORZADO'
);

-- Curación ajustada al nuevo rango de daño y vida
UPDATE equipables SET poder = 20 WHERE nombre = 'POCION';
UPDATE equipables SET poder = 45 WHERE nombre = 'SUPERPOCION';
UPDATE equipables SET poder = 80 WHERE nombre = 'HIPERPOCION';

COMMIT;
