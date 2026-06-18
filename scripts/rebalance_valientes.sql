-- Rebalanceo de valientes usando un presupuesto de poder común.
-- Regla usada:
-- poder_total = fuerza + defensa + habilidad + velocidad + ((vida - 70) / 5)
-- Objetivo: poder_total = 41 para todas las clases base.

UPDATE valientes
SET vida = 85,
    fuerza = 13,
    defensa = 11,
    habilidad = 7,
    velocidad = 7
WHERE tipoValiente = 'GUERRERO';

UPDATE valientes
SET vida = 90,
    fuerza = 9,
    defensa = 13,
    habilidad = 7,
    velocidad = 8
WHERE tipoValiente = 'PALADIN';

UPDATE valientes
SET vida = 70,
    fuerza = 8,
    defensa = 8,
    habilidad = 14,
    velocidad = 11
WHERE tipoValiente = 'MAGO';

UPDATE valientes
SET vida = 75,
    fuerza = 8,
    defensa = 8,
    habilidad = 10,
    velocidad = 14
WHERE tipoValiente = 'PICARO';
