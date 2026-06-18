-- Rebalanceo de valientes para la fórmula actual:
-- - fuerza = daño
-- - defensa = mitigación
-- - habilidad = acierto
-- - velocidad = iniciativa + evasión ligera
--
-- Se reduce un poco el sesgo previo hacia defensa pura y se da más valor
-- a velocidad/habilidad para que todas las clases jueguen distinto.

UPDATE valientes
SET vida = 85,
    fuerza = 13,
    defensa = 10,
    habilidad = 8,
    velocidad = 8
WHERE tipoValiente = 'GUERRERO';

UPDATE valientes
SET vida = 90,
    fuerza = 9,
    defensa = 12,
    habilidad = 8,
    velocidad = 8
WHERE tipoValiente = 'PALADIN';

UPDATE valientes
SET vida = 70,
    fuerza = 8,
    defensa = 7,
    habilidad = 14,
    velocidad = 12
WHERE tipoValiente = 'MAGO';

UPDATE valientes
SET vida = 75,
    fuerza = 8,
    defensa = 7,
    habilidad = 10,
    velocidad = 14
WHERE tipoValiente = 'PICARO';
