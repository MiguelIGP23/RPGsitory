# RPGsitory

Proyecto de creación de un juego RPG de consola

# ⚔️ JavaLand vs. el Compilador Oscuro

## 🌌 La Tierra de los Códigos Olvidados

En los remotos confines del Reino Digital, donde los algoritmos susurran antiguos
secretos y los bucles se entrelazan como místicas serpientes, se extiende un mundo
al borde del colapso. Los bytes se desmoronan, los lenguajes de programación luchan
por sobrevivir, y una sombra inmensa amenaza con borrar toda la memoria: el
Compilador Oscuro.

Este no es un mundo para programadores débiles. Aquí, cada valiente es un guerrero
del código, cada misión una batalla contra la entropía digital. Tu misión: reunir un
equipo de valientes, dominar tus habilidades de programación, y derrotar al ser que
amenaza con fragmentar la realidad misma.

Programa por programa, función por función, línea por línea, construirás la resistencia
que salvará este reino. ¿Serás capaz de escribir el código que cambiará la historia?

El Compilador Oscuro te espera. ¡Que comience la compilación!

---

## 🧭 Descripción del Proyecto

En este proyecto, el alumnado desarrollará un juego de rol (RPG) por turnos donde un grupo
de valientes explora un mundo peligroso, lucha contra monstruos y se prepara para
enfrentar al jefe final: el Compilador Oscuro. El juego será desarrollado en equipos, donde
cada componente del grupo será responsable de una parte específica del código.
Finalmente, todas las piezas se integrarán para formar un juego funcional.

---

## 🧙‍♂️ Clase: Valiente, GestorValientes

**Objetivo:** Implementar los personajes jugables (valientes) con atributos y
habilidades.

**Métodos principales:**

- `Valiente.atacar(Monstruo enemigo)`: Calcula y aplica el daño al enemigo.
- `Valiente.recibirDaño(int cantidad)`: Resta vida al valiente al recibir daño.
- `Valiente.usarHabilidadEspecial(Monstruo enemigo)`: Ejecuta un ataque especial según la clase del valiente.
- `Valiente.subirNivel()`: Mejora atributos del valiente tras ganar experiencia.
- `GestorValientes.crearValientesIniciales()`: Genera un conjunto inicial de valientes.  
  Los atributos iniciales (fuerza, velocidad, habilidad y defensa) sumarán un total de 40 puntos.

**Tipos de Valiente:**
- 🗡️ **Guerrero/a:** entre sus características destacará la fuerza.
- 🛡️ **Paladín:** entre sus características destacará la defensa.
- 🔮 **Mago/a:** entre sus características destacará la habilidad.
- 🏃‍♂️ **Pícaro/a:** entre sus características destacará la velocidad.

**Atributos:**
- `vida`: Representa el nivel de vida. Valor entre 0 y 100.
- `fuerza`: representa el daño que puede causar al enemigo. Valor entre 1 y 20.
- `defensa`: representa la capacidad para defenderse del enemigo. Valor entre 1 y 20.
- `habilidad`: representa la probabilidad de tener éxito en un ataque. Valor inicial de 1 a 20.
- `velocidad`: indica la probabilidad de iniciar el turno de combate. Valor entre 1 y 20.
- `arma`: será un objeto de tipo Arma (ver más adelante) que servirá para incrementar el daño al enemigo.
- `escudo`: será un objeto de tipo Escudo (ver más adelante) que servirá para incrementar el valor de defensa.
- `nivel`: Comenzará valiendo 1 y se incrementará cada vez que se derrote a un monstruo.  
  Cada punto que aumente el nivel aumenta la vida en 10 puntos y el resto de los atributos en 1 punto.

---

## 👹 Clases: Monstruo, GestorMonstruos

**Objetivo:** Implementar los enemigos que los valientes enfrentarán.

**Métodos principales:**

- `Monstruo.atacar(Valiente valiente)`: Aplica daño a un valiente durante el combate.
- `Monstruo.recibirDaño(int cantidad)`: Reduce la vida del monstruo.
- `GestorMonstruos.generarMonstruos(int nivel)`: Crea un monstruo con estadísticas adecuadas al nivel.
- `GestorMonstruos.eliminarMonstruo(Monstruo m)`: Remueve un monstruo derrotado.

**Atributos:**
- `vida`: Representa el nivel de vida. Valor entre 0 y 100.
- `fuerza`: representa el daño que puede causar al enemigo. Valor entre 1 y 20.
- `defensa`: representa el valor de escudo para defenderse del enemigo. Valor entre 1 y 20.
- `habilidad`: representa la probabilidad de tener éxito en un ataque. Valor de 1 a 20.
- `velocidad`: indica la probabilidad de iniciar el turno de combate. Valor entre 1 y 20.
- `nivel`: se fija en GestorMonstruos al ser creado y no cambia durante el juego.

Un monstruo de nivel 1 tendrá esos valores más bajos que uno de nivel 5.

---

## ⚔️ Clase: Combate

**Objetivo:** Implementar la lógica de combate entre valientes y monstruos.

**Métodos principales:**

- `Combate.iniciarCombate(Valiente valiente, Monstruo monstruo)`  
  Inicia un combate por turnos. Se realizará un bucle hasta que el Valiente o el Monstruo mueran (`vida == 0`).  
  En cada iteración del bucle se comparan las iniciativas con la siguiente fórmula:

      Iniciativa_Valiente = Valiente.velocidad * (tirada entre 0,75 y 1)
      Iniciativa_Monstruo = Monstruo.velocidad * (tirada entre 0,75 y 1)

  Empieza el turno quien tenga mayor valor de Iniciativa.  
  Se invocará a `Combate.turno` en cada iteración y se mostrará el resultado del turno (si el ataque ha tenido éxito y el daño aplicado).

- `Combate.turno(Atacante, Defensor)`  
  Maneja la acción de valiente o monstruo en el turno: el atacante atacará al defensor y se calculará si este recibe daño, utilizando el siguiente cálculo:

      Variable_aleatoria: tirada entre 0 y 100.  
      Si (Variable_aleatoria < Atacante.habilidad – (Defensor.defensa + Defensor.escudo, en caso de estar equipado))  
      → el ataque tiene éxito.  

      Cálculo del daño:  
      Defensor.vida se decrementa en (Atacante.fuerza + Atacante.arma.ataque)  
      (si hay arma equipada).  

  Se aplica al defensor solo si el ataque ha tenido éxito.

- `Combate.combateTerminado(Valiente valiente, Monstruo monstruo)`  
  Cuando un valiente derrota a un monstruo, recibirá una bonificación en alguno de sus atributos (vida, fuerza, defensa o habilidad).  
  Imprime por pantalla el ganador y sus niveles actuales.

---

## 🪄 Clases: Objeto, Inventario

**Objetivo:** Implementar un sistema de objetos y un inventario para los valientes.  
Los objetos podrán equiparse para aumentar la fuerza o la defensa del valiente.  
También habrá objetos curativos que se consumirán cuando el valiente los
encuentre y recuperarán puntos de vida.

**Ejemplos de objetos:**
- ⚔️ Espada (aumenta la fuerza si se equipa)
- 🛡️ Escudo (aumenta la defensa si se equipa)
- 🌿 Planta curativa (recupera puntos de vida si el valiente entra en esa casilla)

**Métodos principales:**
- `Objeto.equipar(Valiente valiente)`: Aplica el efecto del objeto en un valiente.
- `Inventario.agregarObjeto(Objeto obj)`: Añade un objeto al inventario.
- `Inventario.usarObjeto(String nombre, Valiente valiente)`: Permite a un valiente usar un objeto.
- `Inventario.mostrarInventario()`: Lista los objetos disponibles.

---

## 🗺️ Clases: Juego, Mapa

**Objetivo:** Crear la estructura general del juego y el sistema de exploración.

**Métodos principales:**

- `Juego.iniciarJuego()`: Inicia la partida y gestiona el flujo del juego.
- `Juego.creacionOEleccionValiente()`: muestra un menú en el que el jugador
  decide si crea un valiente o lo elige de entre los generados por
  GestorValientes. En el caso de elegir crear su propio valiente dispondrá de
  un saldo inicial de puntos para repartir entre las habilidades: fuerza,
  defensa, velocidad y habilidad. El juego se iniciará con este menú.  
  El valiente empezará en la posición (1,1) del mapa.

- `Juego.mostrarMenuPrincipal()`: Muestra las opciones del menú principal.
    - Mostrar valiente (niveles, inventario, etc.)
    - Equipar objeto
    - Mostrar mapa
    - Moverse (arriba, abajo, izquierda, derecha)
    - Salir del juego

- `Juego.explorarMapa()`: Permite desplazarse y encontrar enemigos.  
  El valiente solo puede moverse arriba, abajo, izquierda y derecha (según los límites del mapa).  
  Cuando entra en una casilla se indicará si está vacía, si hay un objeto o un monstruo, y se desencadenará el evento correspondiente.

- `Juego.mostrarEstadoJuego()`: Muestra información sobre el progreso de la
  partida (monstruos derrotados, características del jugador, número de objetos encontrados, etc.).

**Atributos principales del mapa:**
- `Mapa.casillas`: una cuadrícula de n × m casillas.  
  Cada casilla puede haber sido revelada o no y puede estar vacía, contener un monstruo o un objeto.  
  Cuando el valiente entra en una casilla:
    - Se revelarán las casillas adyacentes (arriba, abajo, izquierda, derecha).
    - Si hay un monstruo, se luchará con él.
  - Si hay un objeto, pasará a su inventario si es un arma o un escudo, o se consumirá si es curativo.

---

## 💀 Clase: CompiladorOscuro

Se trata del enemigo principal del juego, al que el valiente debe derrotar para ganar la partida.  
Su posición inicial será la casilla n × m (la más alejada de la 1,1).

**Atributos iniciales:**
- Vida: 150
- Fuerza, defensa, habilidad y velocidad: 3 puntos por cada monstruo creado en el juego.  

---



## ⚙️ EXTRAS

### 🧩 Niveles de dificultad

Se ofrecerá al jugador elegir el nivel de dificultad al inicio del juego:

- **Fácil:** todas las casillas se revelarán desde el inicio.  
  El Compilador Oscuro tiene 125 puntos de vida.

- **Intermedio:** las casillas se revelan cuando el valiente entra en una casilla
  adyacente y el Compilador Oscuro tiene 150 puntos de vida.

- **Difícil:** no se revelan las casillas adyacentes, solo las ya visitadas.  
  El Compilador Oscuro tiene 200 puntos de vida.

---

### 🔁 Post-game

Cuando se derrota al Compilador Oscuro, se puede recomenzar el juego
(**Nuevo Juego +**) con un valiente especial (**Explorador**) que puede revelar y recorrer
casillas en diagonal.

---

