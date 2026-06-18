BEGIN TRANSACTION;

-- Rebalanceo general de monstruos para la fórmula actual de combate:
-- - fuerza = daño bruto
-- - defensa = mitigación de daño
-- - habilidad = probabilidad de acierto
-- - velocidad = iniciativa + evasión ligera
--
-- Además se fija un nivel base en BD para poder generar encuentros coherentes.

UPDATE monstruos SET vida = 62, fuerza = 7,  defensa = 5,  habilidad = 8,  velocidad = 12, nivel = 1 WHERE tipo = 'KOBOLD';
UPDATE monstruos SET vida = 58, fuerza = 6,  defensa = 4,  habilidad = 13, velocidad = 10, nivel = 1 WHERE tipo = 'DEVORADOR_INTELECTOS';
UPDATE monstruos SET vida = 64, fuerza = 7,  defensa = 5,  habilidad = 9,  velocidad = 13, nivel = 1 WHERE tipo = 'AARAKOCRA';
UPDATE monstruos SET vida = 68, fuerza = 7,  defensa = 6,  habilidad = 10, velocidad = 7,  nivel = 1 WHERE tipo = 'HONGOS';
UPDATE monstruos SET vida = 70, fuerza = 8,  defensa = 7,  habilidad = 8,  velocidad = 7,  nivel = 1 WHERE tipo = 'NO_MUERTO';

UPDATE monstruos SET vida = 68, fuerza = 8,  defensa = 6,  habilidad = 11, velocidad = 11, nivel = 2 WHERE tipo = 'ABRIAN';
UPDATE monstruos SET vida = 69, fuerza = 8,  defensa = 5,  habilidad = 10, velocidad = 9,  nivel = 2 WHERE tipo = 'ARANA_GIGANTE';
UPDATE monstruos SET vida = 72, fuerza = 9,  defensa = 6,  habilidad = 11, velocidad = 11, nivel = 2 WHERE tipo = 'TIEFLING';
UPDATE monstruos SET vida = 74, fuerza = 9,  defensa = 7,  habilidad = 10, velocidad = 8,  nivel = 2 WHERE tipo = 'MIMIC';
UPDATE monstruos SET vida = 76, fuerza = 10, defensa = 7,  habilidad = 11, velocidad = 9,  nivel = 2 WHERE tipo = 'YUAN_TI';
UPDATE monstruos SET vida = 78, fuerza = 10, defensa = 7,  habilidad = 8,  velocidad = 7,  nivel = 2 WHERE tipo = 'ORCO';
UPDATE monstruos SET vida = 80, fuerza = 9,  defensa = 9,  habilidad = 7,  velocidad = 5,  nivel = 2 WHERE tipo = 'CUBO_GELATINOSO';

UPDATE monstruos SET vida = 82, fuerza = 10, defensa = 8,  habilidad = 9,  velocidad = 8,  nivel = 3 WHERE tipo = 'DRACONIDO';
UPDATE monstruos SET vida = 84, fuerza = 10, defensa = 9,  habilidad = 7,  velocidad = 5,  nivel = 3 WHERE tipo = 'ALMEJA_GIGANTE';
UPDATE monstruos SET vida = 84, fuerza = 10, defensa = 7,  habilidad = 7,  velocidad = 6,  nivel = 3 WHERE tipo = 'SILURO_GIGANTE';
UPDATE monstruos SET vida = 86, fuerza = 11, defensa = 8,  habilidad = 7,  velocidad = 6,  nivel = 3 WHERE tipo = 'OSO_LECHUZA';
UPDATE monstruos SET vida = 86, fuerza = 11, defensa = 6,  habilidad = 9,  velocidad = 12, nivel = 3 WHERE tipo = 'HOMBRE_TIGRE';
UPDATE monstruos SET vida = 80, fuerza = 8,  defensa = 7,  habilidad = 13, velocidad = 8,  nivel = 3 WHERE tipo = 'LICHE';

UPDATE monstruos SET vida = 88, fuerza = 11, defensa = 7,  habilidad = 11, velocidad = 12, nivel = 4 WHERE tipo = 'BESTIA_DESPLAZADORA';
UPDATE monstruos SET vida = 94, fuerza = 12, defensa = 10, habilidad = 8,  velocidad = 6,  nivel = 4 WHERE tipo = 'DRAGON_PIEDRA';
UPDATE monstruos SET vida = 95, fuerza = 12, defensa = 8,  habilidad = 10, velocidad = 8,  nivel = 4 WHERE tipo = 'DRAGON_NEGRO';

UPDATE monstruos SET vida = 98, fuerza = 13, defensa = 8,  habilidad = 11, velocidad = 9,  nivel = 5 WHERE tipo = 'DRAGON_ROJO';

COMMIT;
