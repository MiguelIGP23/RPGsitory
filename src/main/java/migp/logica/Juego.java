package migp.logica;

import migp.modelo.Equipable;
import migp.modelo.InventarioItem;
import migp.modelo.Monstruo;
import migp.modelo.Valiente;
import migp.modelo.enums.TiposEquipable;
import migp.persistencia.DaoEquipable;
import migp.persistencia.DaoMonstruo;
import migp.persistencia.DaoValiente;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Gestiona el flujo principal del juego por consola
public class Juego {

    public static final int PUNTOS_INICIALES = 41;
    public static final int VIDA_BASE_MINIMA = 70;
    public static final int VIDA_POR_PUNTO = 5;
    public static final int STAT_BASE_CREACION = 3;
    public static final int STAT_MAXIMA_CREACION = 16;
    public static final int STAT_EXTRA_MAXIMA_CREACION = STAT_MAXIMA_CREACION - STAT_BASE_CREACION;
    private static final Scanner SCANNER = new Scanner(System.in);

    private final DaoValiente daoValiente;
    private final DaoMonstruo daoMonstruo;
    private final DaoEquipable daoEquipable;

    private Valiente valienteJugador;
    private Mapa mapa;
    private int posX;
    private int posY;
    private int monstruosDerrotados;
    private int objetosEncontrados;
    private int casillasVisitadas;
    private boolean juegoTerminado;
    private boolean juegoGanado;

    public Juego() {
        this.daoValiente = new DaoValiente();
        this.daoMonstruo = new DaoMonstruo();
        this.daoEquipable = new DaoEquipable();
        this.posX = 1;
        this.posY = 1;
        this.monstruosDerrotados = 0;
        this.objetosEncontrados = 0;
        this.casillasVisitadas = 0;
        this.juegoTerminado = false;
        this.juegoGanado = false;
    }

    //Inicia el juego creando o eligiendo valiente y abre el menú principal
    public void iniciarJuego() {
        System.out.println(Consola.color(Consola.ANSI_CYAN, "=== RPGsitory ===\n"));
        this.valienteJugador = creacionOEleccionValiente();
        inicializarMapa();
        System.out.println(Consola.color(Consola.ANSI_VERDE, "\nComienza la aventura de " + valienteJugador.getTipoValiente() + " en la posición (1,1).\n"));
        mostrarMenuPrincipal();
    }

    //Permite elegir un valiente de base de datos o crear uno nuevo
    public Valiente creacionOEleccionValiente() {
        while (true) {
            //Primer menú del juego: reutiliza valientes base o crea uno nuevo
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "1. Elegir valiente"));
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "2. Crear valiente"));
            System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- Elige opción: "));

            int opcion = leerEntero();
            System.out.println();

            switch (opcion) {
                case 1 -> {
                    return elegirValienteBase();
                }
                case 2 -> {
                    return crearValientePersonalizado();
                }
                default -> System.out.println(Consola.color(Consola.ANSI_ROJO, "Opción no válida.\n"));
            }
        }
    }

    //Muestra el menú principal actual del juego
    public void mostrarMenuPrincipal() {
        boolean salir = false;

        while (!salir && !juegoTerminado) {
            System.out.println(Consola.color(Consola.ANSI_CYAN, "=== MENÚ PRINCIPAL ==="));
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "1. Mostrar valiente"));
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "2. Equipar objeto"));
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "3. Moverse"));
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "4. Salir del juego"));
            System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- Elige opción: "));

            int opcion = leerEntero();
            System.out.println();

            switch (opcion) {
                case 1 -> mostrarValiente();
                case 2 -> equiparObjeto();
                case 3 -> explorarMapa();
                case 4 -> {
                    salir = true;
                    System.out.println(Consola.color(Consola.ANSI_VERDE, "Saliendo del juego..."));
                }
                default -> System.out.println(Consola.color(Consola.ANSI_ROJO, "Opción no válida.\n"));
            }
        }
    }

    //Muestra datos generales del valiente y su inventario
    public void mostrarEstadoJuego() {
        System.out.println(Consola.color(Consola.ANSI_CYAN, "=== ESTADO DEL JUEGO ==="));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Nivel actual: " + valienteJugador.getNivel()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Vida actual: " + valienteJugador.getVida() + "/" + valienteJugador.getVidaMaxima()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Posición actual: (" + posX + "," + posY + ")"));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Casillas visitadas: " + casillasVisitadas));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Monstruos derrotados: " + monstruosDerrotados));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Monstruos restantes: " + mapa.contarMonstruosRestantes()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Compilador Oscuro: " + (juegoGanado ? "Derrotado" : "Pendiente")));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Objetos encontrados: " + objetosEncontrados));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Objetos en inventario: " + valienteJugador.getInventario().getItems().size()));
        System.out.println();
    }

    //Selecciona uno de los valientes precargados en base de datos
    private Valiente elegirValienteBase() {
        List<Valiente> valientesBase = daoValiente.listar();

        while (true) {
            System.out.println(Consola.color(Consola.ANSI_CYAN, "=== VALIENTES DISPONIBLES ==="));
            for (int i = 0; i < valientesBase.size(); i++) {
                Valiente valiente = valientesBase.get(i);
                System.out.println(Consola.color(Consola.ANSI_AZUL, String.format(
                        "%d. %-8s | Nivel: %d | Vida: %d | Fuerza: %d | Defensa: %d | Habilidad: %d | Velocidad: %d",
                        i + 1,
                        valiente.getTipoValiente(),
                        valiente.getNivel(),
                        valiente.getVida(),
                        valiente.getFuerza(),
                        valiente.getDefensa(),
                        valiente.getHabilidad(),
                        valiente.getVelocidad()
                )));
            }
            System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- Elige valiente: "));

            int opcion = leerEntero();
            if (opcion >= 1 && opcion <= valientesBase.size()) {
                Valiente seleccionado = valientesBase.get(opcion - 1);
                //Antes de aceptar el valiente, enseña su equipo y habilidad especial en detalle
                if (confirmarValienteBase(seleccionado)) {
                    return seleccionado;
                }
                System.out.println();
                continue;
            }

            System.out.println(Consola.color(Consola.ANSI_ROJO, "Valiente no válido.\n"));
        }
    }

    //Crea un valiente nuevo heredando vida base y habilidad especial de la plantilla elegida
    private Valiente crearValientePersonalizado() {
        Valiente plantilla = elegirPlantillaCreacion();

        while (true) {
            //El reparto devuelve también los puntos sobrantes para decidir si rehacerlo
            int[] stats = repartirStatsPersonalizados(plantilla);
            if (preguntarReasignacionPuntosSobrantes(stats[4])) {
                continue;
            }
            Valiente candidato = construirValientePersonalizado(plantilla, stats);

            //Antes de aceptar el valiente permite revisar el resultado final
            if (confirmarValientePersonalizado(candidato, plantilla)) {
                return candidato;
            }

            int opcion = elegirAccionTrasRevision();
            switch (opcion) {
                case 1 -> plantilla = elegirPlantillaCreacion();
                case 2 -> {
                    //Mantiene la plantilla y vuelve a repartir stats
                }
                case 3 -> {
                    return candidato;
                }
                default -> System.out.println(Consola.color(Consola.ANSI_ROJO, "Opción no válida.\n"));
            }
        }
    }

    //Permite elegir la plantilla que aporta vida base y habilidad especial
    private Valiente elegirPlantillaCreacion() {
        List<Valiente> plantillas = daoValiente.listar();

        while (true) {
            //La plantilla define la vida base y la habilidad especial, no el reparto final de stats
            System.out.println(Consola.color(Consola.ANSI_CYAN, "=== PLANTILLAS DE CREACIÓN ==="));
            for (int i = 0; i < plantillas.size(); i++) {
                Valiente plantilla = plantillas.get(i);
                System.out.println(Consola.color(Consola.ANSI_AZUL, String.format(
                        "%d. %-8s | Vida base: %-2d | Puntos para stats: %-2d | Habilidad: %s",
                        i + 1,
                        plantilla.getTipoValiente(),
                        plantilla.getVida(),
                        calcularPuntosCreacion(plantilla),
                        descripcionHabilidadEspecial(plantilla)
                )));
            }
            System.out.println(Consola.color(Consola.ANSI_MAGENTA, "\nAl crear un valiente se usará su vida base y su habilidad especial de clase."));
            System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- Elige plantilla: "));

            int opcion = leerEntero();
            if (opcion >= 1 && opcion <= plantillas.size()) {
                return plantillas.get(opcion - 1);
            }

            System.out.println(Consola.color(Consola.ANSI_ROJO, "Plantilla no válida.\n"));
        }
    }

    //Convierte la ventaja de vida inicial en menos puntos repartibles
    private int calcularPuntosCreacion(Valiente plantilla) {
        //Cada bloque de vida extra por encima de la referencia reduce el presupuesto de stats
        int tramosExtraVida = (plantilla.getVida() - VIDA_BASE_MINIMA) / VIDA_POR_PUNTO;
        return PUNTOS_INICIALES - tramosExtraVida;
    }

    //Recoge el reparto de stats partiendo de un valor base común en cada atributo
    private int[] repartirStatsPersonalizados(Valiente plantilla) {
        int puntosRepartibles = calcularPuntosCreacion(plantilla);
        int puntosRestantes = puntosRepartibles;

        System.out.println(Consola.color(Consola.ANSI_CYAN,
                "\nReparte tus " + puntosRepartibles + " puntos extra entre fuerza, defensa, habilidad y velocidad ("
                        + (STAT_BASE_CREACION-3) + "-" + (STAT_MAXIMA_CREACION-3) + "):\n"));

        //Cada stat parte del valor base y solo se añaden puntos extra elegidos por el jugador
        int fuerza = STAT_BASE_CREACION + leerPuntosAtributo("Fuerza", puntosRestantes);
        puntosRestantes -= (fuerza - STAT_BASE_CREACION);
        mostrarResumenRepartoActual(fuerza, STAT_BASE_CREACION, STAT_BASE_CREACION, STAT_BASE_CREACION, puntosRestantes);
        if (puntosRestantes == 0) {
            //Si se agotan los puntos, el resto de stats mantienen su valor base
            avisarStatsRestantesAUno();
            return new int[]{fuerza, STAT_BASE_CREACION, STAT_BASE_CREACION, STAT_BASE_CREACION, puntosRestantes};
        }

        int defensa = STAT_BASE_CREACION + leerPuntosAtributo("Defensa", puntosRestantes);
        puntosRestantes -= (defensa - STAT_BASE_CREACION);
        mostrarResumenRepartoActual(fuerza, defensa, STAT_BASE_CREACION, STAT_BASE_CREACION, puntosRestantes);
        if (puntosRestantes == 0) {
            avisarStatsRestantesAUno();
            return new int[]{fuerza, defensa, STAT_BASE_CREACION, STAT_BASE_CREACION, puntosRestantes};
        }

        int habilidad = STAT_BASE_CREACION + leerPuntosAtributo("Habilidad", puntosRestantes);
        puntosRestantes -= (habilidad - STAT_BASE_CREACION);
        mostrarResumenRepartoActual(fuerza, defensa, habilidad, STAT_BASE_CREACION, puntosRestantes);
        if (puntosRestantes == 0) {
            avisarStatsRestantesAUno();
            return new int[]{fuerza, defensa, habilidad, STAT_BASE_CREACION, puntosRestantes};
        }

        int velocidad = STAT_BASE_CREACION + leerPuntosAtributo("Velocidad", puntosRestantes);
        puntosRestantes -= (velocidad - STAT_BASE_CREACION);
        mostrarResumenRepartoActual(fuerza, defensa, habilidad, velocidad, puntosRestantes);
        return new int[]{fuerza, defensa, habilidad, velocidad, puntosRestantes};
    }

    //Construye el valiente final usando plantilla y stats elegidos
    private Valiente construirValientePersonalizado(Valiente plantilla, int[] stats) {
        return new Valiente(
                plantilla.getTipoValiente(),
                plantilla.getVida(),
                stats[0],
                stats[1],
                stats[2],
                stats[3],
                plantilla.getArma(),
                plantilla.getEscudo(),
                1
        );
    }

    //Muestra el resumen final y pide conformidad antes de continuar
    private boolean confirmarValientePersonalizado(Valiente candidato, Valiente plantilla) {
        //Resumen final antes de crear el personaje definitivamente
        System.out.println(Consola.color(Consola.ANSI_CYAN, "\n=== RESUMEN DEL VALIENTE ==="));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Clase base: " + plantilla.getTipoValiente()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Vida base: " + plantilla.getVida()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Stats: Fuerza " + candidato.getFuerza()
                + ", Defensa " + candidato.getDefensa()
                + ", Habilidad " + candidato.getHabilidad()
                + ", Velocidad " + candidato.getVelocidad()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Habilidad especial: " + descripcionHabilidadEspecial(plantilla)));
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "\n1. Sí"));
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "2. No"));
        System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- ¿Confirmas este valiente?: "));

        int opcion = leerEntero();
        System.out.println();
        return opcion == 1;
    }

    //Permite decidir si cambiar de plantilla, repartir otra vez o confirmar igualmente
    private int elegirAccionTrasRevision() {
        System.out.println(Consola.color(Consola.ANSI_CYAN, "=== REVISAR VALIENTE ==="));
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "1. Volver a elegir valiente"));
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "2. Repartir stats"));
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "3. Confirmar"));
        System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- Elige opción: "));

        int opcion = leerEntero();
        System.out.println();
        return opcion;
    }

    //Devuelve un resumen legible de la habilidad especial de cada clase
    private String descripcionHabilidadEspecial(Valiente plantilla) {
        return switch (plantilla.getTipoValiente()) {
            case GUERRERO -> "Carga Asesina: ataque con 150% de potencia";
            case PALADIN -> "Armadura Sacra: aumenta defensa 25% durante 3 turnos";
            case MAGO -> "Bola de Escarcha: reduce ataque rival 20% durante 3 turnos";
            case PICARO -> "Colmillo Podrido: envenena durante 3 turnos";
        };
    }

    //Muestra un resumen del valiente base y permite confirmarlo o volver al listado
    private boolean confirmarValienteBase(Valiente valiente) {
        //A diferencia del listado, aquí sí se enseña equipo y habilidad especial completos
        System.out.println(Consola.color(Consola.ANSI_CYAN, "\n=== RESUMEN DEL VALIENTE ==="));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Clase: " + valiente.getTipoValiente()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Nivel: " + valiente.getNivel()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Vida: " + valiente.getVida()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Fuerza: " + valiente.getFuerza()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Defensa: " + valiente.getDefensa()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Habilidad: " + valiente.getHabilidad()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Velocidad: " + valiente.getVelocidad()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Habilidad especial: " + descripcionHabilidadEspecial(valiente)));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Arma: " + descripcionEquipable(valiente.getArma())));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Escudo: " + descripcionEquipable(valiente.getEscudo())));
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "\n1. Elegir este valiente"));
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "2. Cambiar"));
        System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- Elige opción: "));

        int opcion = leerEntero();
        System.out.println();
        return opcion == 1;
    }

    //Devuelve el nombre y poder del equipo si existe, o un guion si no tiene
    private String descripcionEquipable(Equipable equipable) {
        if (equipable == null) {
            return "-";
        }
        return equipable.getNombre() + " (" + equipable.getPoder() + ")";
    }

    //Muestra toda la información útil del personaje seleccionado
    private void mostrarValiente() {
        System.out.println(Consola.color(Consola.ANSI_CYAN, "=== VALIENTE ==="));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, valienteJugador.toString()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Vida máxima: " + valienteJugador.getVidaMaxima()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Posición: (" + posX + "," + posY + ")"));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Monstruos derrotados: " + monstruosDerrotados));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Compilador Oscuro: " + (juegoGanado ? "Derrotado" : "Pendiente")));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Objetos encontrados: " + objetosEncontrados));
        mostrarInventario();
        System.out.println();
    }

    //Permite equipar armas y escudos que existan en el inventario
    private void equiparObjeto() {
        List<InventarioItem> equipables = obtenerEquipablesInventario();
        if (equipables.isEmpty()) {
            System.out.println(Consola.color(Consola.ANSI_ROJO, "No tienes armas ni escudos en el inventario.\n"));
            return;
        }

        while (true) {
            //Se agrupan armas y escudos porque son los únicos equipables manualmente
            System.out.println(Consola.color(Consola.ANSI_CYAN, "=== EQUIPAR OBJETO ==="));
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "1. Atrás"));
            for (int i = 0; i < equipables.size(); i++) {
                InventarioItem item = equipables.get(i);
                System.out.println(Consola.color(Consola.ANSI_AZUL, (i + 2) + ". " + item.getEquipable().getNombre()
                        + " [" + item.getEquipable().getTipo() + "] x" + item.getCantidad()));
            }
            System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- Elige objeto: "));

            int opcion = leerEntero();
            if (opcion == 1) {
                System.out.println();
                return;
            }
            if (opcion < 2 || opcion > equipables.size() + 1) {
                System.out.println(Consola.color(Consola.ANSI_ROJO, "Objeto no válido.\n"));
                continue;
            }

            Equipable seleccionado = equipables.get(opcion - 2).getEquipable();
            if (seleccionado.getTipo() == TiposEquipable.ARMA) {
                valienteJugador.equiparArma(seleccionado);
                System.out.println(Consola.color(Consola.ANSI_VERDE, "\nHas equipado el arma " + seleccionado.getNombre() + ".\n"));
            } else {
                valienteJugador.equiparEscudo(seleccionado);
                System.out.println(Consola.color(Consola.ANSI_VERDE, "\nHas equipado el escudo " + seleccionado.getNombre() + ".\n"));
            }
            return;
        }
    }

    private void explorarMapa() {
        while (!juegoTerminado) {
            System.out.println(mapa.representar(posX, posY));
            System.out.println(Consola.color(Consola.ANSI_CYAN, "=== EXPLORAR MAPA ==="));
            System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Posición actual: (" + posX + "," + posY + ")"));
            System.out.println(Consola.color(Consola.ANSI_AMARILLO,
                    "1. Arriba    2. Abajo    3. Izquierda    4. Derecha    5. Atrás"));
            System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- Elige dirección: "));

            int opcion = leerEntero();
            System.out.println();

            switch (opcion) {
                case 1 -> moverValiente(-1, 0);
                case 2 -> moverValiente(1, 0);
                case 3 -> moverValiente(0, -1);
                case 4 -> moverValiente(0, 1);
                case 5 -> {
                    return;
                }
                default -> System.out.println(Consola.color(Consola.ANSI_ROJO, "Dirección no válida.\n"));
            }
        }
    }

    //Deja el mapa listo y revela la zona de inicio
    private void inicializarMapa() {
        this.mapa = new Mapa(daoMonstruo, daoEquipable, valienteJugador.getNivel());
        this.posX = 1;
        this.posY = 1;
        this.monstruosDerrotados = 0;
        this.objetosEncontrados = 0;
        this.casillasVisitadas = 1;
        this.juegoTerminado = false;
        this.juegoGanado = false;
    }

    //Mueve al jugador una casilla si la dirección es válida y resuelve su contenido
    private void moverValiente(int deltaX, int deltaY) {
        int nuevaX = posX + deltaX;
        int nuevaY = posY + deltaY;

        if (!mapa.dentroLimites(nuevaX, nuevaY)) {
            System.out.println(Consola.color(Consola.ANSI_ROJO, "No puedes salir de los límites del mapa.\n"));
            return;
        }

        posX = nuevaX;
        posY = nuevaY;
        mapa.revelarEntorno(posX, posY);

        Casilla casillaActual = mapa.getCasilla(posX, posY);
        if (!casillaActual.getVisitada()) {
            casillasVisitadas++;
        }
        casillaActual.marcarVisitada();
        mapa.prepararCasilla(casillaActual, valienteJugador.getNivel());

        System.out.println(Consola.color(Consola.ANSI_VERDE, "Te desplazas a la posición (" + posX + "," + posY + ")."));
        resolverCasillaActual(casillaActual);
        System.out.println();
    }

    //Al entrar en una casilla se resuelve su evento solo si aún conserva contenido
    private void resolverCasillaActual(Casilla casillaActual) {
        if (casillaActual.getJefeFinal() && casillaActual.tieneMonstruo()) {
            System.out.println(Consola.color(Consola.ANSI_MAGENTA, "*** Has encontrado al Compilador Oscuro. ***\n"));
            resolverCombateCasilla(casillaActual);
            return;
        }

        if (casillaActual.tieneMonstruo()) {
            resolverCombateCasilla(casillaActual);
            return;
        }

        if (casillaActual.tieneObjeto()) {
            resolverObjetoCasilla(casillaActual);
            return;
        }

        System.out.println(Consola.color(Consola.ANSI_CYAN, "La casilla está vacía."));
    }

    //Tras cada combate se limpia la casilla y se actualiza el estado de la partida
    private void resolverCombateCasilla(Casilla casillaActual) {
        Monstruo monstruo = casillaActual.getMonstruo();
        Combate.iniciarCombate(valienteJugador, monstruo);

        if (valienteJugador.getMuerto()) {
            juegoTerminado = true;
            System.out.println(Consola.color(Consola.ANSI_ROJO, "\nFin de la partida."));
            return;
        }

        monstruosDerrotados++;
        boolean jefeFinal = casillaActual.getJefeFinal();
        casillaActual.eliminarMonstruo();

        if (jefeFinal) {
            juegoGanado = true;
            juegoTerminado = true;
            System.out.println(Consola.color(Consola.ANSI_VERDE, "\nHas derrotado al Compilador Oscuro. Has ganado la partida."));
        }
    }

    //Los objetos de equipo se guardan y los curativos se consumen al momento
    private void resolverObjetoCasilla(Casilla casillaActual) {
        Equipable objeto = casillaActual.getObjeto();
        objetosEncontrados++;
        boolean objetoSuperior = esObjetoSuperiorParaNivel(objeto);
        String prefijo = objetoSuperior ? "! " : "";

        if (objeto.getTipo() == TiposEquipable.CONSUMIBLE) {
            int vidaRecuperada = valienteJugador.curar(objeto.getPoder());
            if (vidaRecuperada > 0) {
                System.out.println(Consola.color(Consola.ANSI_VERDE,
                        prefijo + "Has encontrado " + objeto.getNombre() + ", recuperas " + vidaRecuperada + " hp."));
            } else {
                System.out.println(Consola.color(Consola.ANSI_AMARILLO,
                        prefijo + "Has encontrado " + objeto.getNombre() + ", pero tu HP ya estaba al máximo."));
            }
        } else {
            valienteJugador.nuevoObjeto(objeto, 1);
            String color = objetoSuperior ? Consola.ANSI_VERDE : Consola.ANSI_AZUL;
            System.out.println(Consola.color(color,
                    prefijo + "Has encontrado " + objeto.getNombre() + " (" + objeto.getPoder() + ") y pasa a tu inventario."));
        }

        casillaActual.eliminarObjeto();
    }

    //Marca hallazgos ligeramente adelantados al nivel actual del valiente
    private boolean esObjetoSuperiorParaNivel(Equipable objeto) {
        if (objeto.getTipo() == TiposEquipable.CONSUMIBLE) {
            return objeto.getPoder() > poderConsumibleEsperado(valienteJugador.getNivel());
        }
        return objeto.getPoder() > poderEquipoEsperadoMaximo(valienteJugador.getNivel());
    }

    private int poderConsumibleEsperado(int nivelValiente) {
        if (nivelValiente <= 2) {
            return 20;
        }
        if (nivelValiente <= 4) {
            return 45;
        }
        return 80;
    }

    private int poderEquipoEsperadoMaximo(int nivelValiente) {
        return Math.min(8, nivelValiente + 3);
    }

    //Pide los puntos extra de un atributo teniendo en cuenta los puntos restantes
    private int leerPuntosAtributo(String nombreAtributo, int puntosRestantes) {
        int maximoExtraStat = STAT_EXTRA_MAXIMA_CREACION;
        int maximoPosible = Math.min(maximoExtraStat, puntosRestantes);

        while (true) {
            System.out.print(Consola.color(Consola.ANSI_AMARILLO, "- " + nombreAtributo + " (restantes: " + puntosRestantes + "): "));
            int valor = leerEntero();

            //Las validaciones priorizan mensajes concretos para que el usuario sepa qué corregir
            if (valor < 0) {
                System.out.println(Consola.color(Consola.ANSI_ROJO, "Valor no válido. El mínimo extra es 0."));
                continue;
            }
            if (valor > maximoExtraStat) {
                System.out.println(Consola.color(Consola.ANSI_ROJO,
                        "Valor no válido. El máximo es " + STAT_EXTRA_MAXIMA_CREACION));
                continue;
            }
            if (valor > puntosRestantes) {
                System.out.println(Consola.color(Consola.ANSI_ROJO,
                        "Puntos insuficientes. Te quedan " + puntosRestantes + " puntos."));
                continue;
            }
            if (valor > maximoPosible) {
                System.out.println(Consola.color(Consola.ANSI_ROJO,
                        "El máximo posible para " + nombreAtributo + " es " + maximoPosible + "."));
                continue;
            }

            if (valor >= 0 && valor <= STAT_EXTRA_MAXIMA_CREACION) {
                return valor;
            }
        }
    }

    //Informa de que las stats restantes se quedan en su valor base
    private void avisarStatsRestantesAUno() {
        System.out.println(Consola.color(Consola.ANSI_VERDE,
                "No quedan más puntos. Las stats restantes se quedan en " + STAT_BASE_CREACION + ".\n"));
    }

    //Pregunta si se quiere rehacer el reparto cuando todavía sobran puntos
    private boolean preguntarReasignacionPuntosSobrantes(int puntosRestantes) {
        if (puntosRestantes <= 0) {
            return false;
        }

        System.out.println(Consola.color(Consola.ANSI_VERDE,
                "Han sobrado " + puntosRestantes + " puntos."));
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "¿Qué quieres hacer?"));
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "1. Volver a asignarlos"));
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "2. Continuar"));
        System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- Elige opción: "));

        int opcion = leerEntero();
        System.out.println();
        return opcion == 1;
    }

    //Muestra cómo va quedando el reparto después de asignar cada stat
    private void mostrarResumenRepartoActual(int fuerza, int defensa, int habilidad, int velocidad, int puntosRestantes) {
        System.out.println(Consola.color(Consola.ANSI_MAGENTA,
                "Actual -> Fuerza: " + fuerza
                        + " | Defensa: " + defensa
                        + " | Habilidad: " + habilidad
                        + " | Velocidad: " + velocidad));
        System.out.println();
    }

    //Agrupa solo los objetos que pueden equiparse desde inventario
    private List<InventarioItem> obtenerEquipablesInventario() {
        List<InventarioItem> equipables = new ArrayList<>();
        equipables.addAll(valienteJugador.getInventario().getArmas());
        equipables.addAll(valienteJugador.getInventario().getEscudos());
        return equipables;
    }

    //Muestra el inventario separado por categorías para hacerlo más legible
    private void mostrarInventario() {
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Inventario: " + valienteJugador.getInventario().getItems()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Armas: " + valienteJugador.getInventario().getArmas()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Escudos: " + valienteJugador.getInventario().getEscudos()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "- Consumibles: " + valienteJugador.getInventario().getConsumibles()));
    }

    //Lee enteros de consola sin romper el menú si el input no es numérico
    private int leerEntero() {
        while (!SCANNER.hasNextInt()) {
            System.out.print(Consola.color(Consola.ANSI_ROJO, "Introduce un número: "));
            SCANNER.next();
        }
        return SCANNER.nextInt();
    }
}
