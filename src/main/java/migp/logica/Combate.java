package migp.logica;

import migp.modelo.InventarioItem;
import migp.modelo.Monstruo;
import migp.modelo.Valiente;

import java.util.List;
import java.util.Scanner;

public class Combate {

    //Atributos de clase para ajustar % de golpe
    public static final int BASE_HIT = 62;              //% de golpe medio en enfrentamiento equilibrado
    public static final double PESO_HABILIDAD = 2.5;    //la habilidad define el control del golpe
    public static final double PESO_EVASION = 1.5;      //la velocidad del defensor reduce algo la probabilidad
    public static final double PESO_NIVEL = 4.0;        //el nivel influye, pero no debe decidir solo el combate
    public static final int PROBABILIDAD_MINIMA = 20;
    public static final int PROBABILIDAD_MAXIMA = 90;

    public static int turnoCombate;
    private static final Scanner SCANNER = new Scanner(System.in);


    //Iniciar bucle de combate entre valiente y monstruo, termina cuando uno muere
    public static void iniciarCombate(Valiente valiente, Monstruo monstruo) {
        //Guarda y restaura atributos del jugador al final del combate
        int fuerzaInicial = valiente.getFuerza();
        int defensaInicial = valiente.getDefensa();
        System.out.println(Consola.color(Consola.ANSI_AZUL, "***  Apareció " + monstruo.getNombreMostrado() + " nivel " + monstruo.getNivel() + "!  ***\n"));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, valiente.toString()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, monstruo.toString()));
        turnoCombate = 1;
        while (!valiente.getMuerto() && !monstruo.getMuerto()) {
            turno(valiente, monstruo);
            turnoCombate++;
        }
        if (valiente.getMuerto()) {
            System.out.println(Consola.color(Consola.ANSI_ROJO, "\n\t\txxx HAS MUERTO xxx"));
        } else {
            System.out.println(Consola.color(Consola.ANSI_VERDE, "\n\t\t-- VICTORIA --"));
            System.out.println(Consola.color(Consola.ANSI_MAGENTA, valiente.toString()));
            valiente.setFuerza(fuerzaInicial);
            valiente.setDefensa(defensaInicial);
            valiente.subirNivel();
        }

    }

    //Cuando se añadan opciones, añadir en este switch y en el sout del metodo turno() de debajo
    private static boolean opcionesCombate(Valiente valiente, Monstruo monstruo, int opcion) {
        switch (opcion) {
            case 1 -> {
                valiente.atacar(monstruo, 0);
                return true;
            }
            case 2 -> {
                valiente.usarHabilidadEspecial(monstruo);
                return true;
            }
            case 3 -> {
                return usarConsumibleEnCombate(valiente);
            }
            default -> {
                System.out.println(Consola.color(Consola.ANSI_ROJO, "Opción de turno no valida"));
                return false;
            }
        }
    }


    //Aplica iniciativa y realiza los ataques
    public static void turno(Valiente valiente, Monstruo monstruo) {
        //Calcula iniciativa
        double iniVal = iniciativa(valiente);
        double iniMon = iniciativa(monstruo);
        //El jugador elige accion antes de resolver el orden real del turno
        System.out.printf("%s\n\n\t\tTURNO %d\n%s", Consola.ANSI_CYAN, turnoCombate, Consola.ANSI_RESET);
        int opcion = turnoJugador(valiente, monstruo);

        //La iniciativa decide quien ejecuta primero la accion
        boolean monstruoPrimero = iniMon > iniVal;

        if (monstruoPrimero) {
            // Turno monstruo
            if (ataqueExitoso(monstruo, valiente)) monstruo.atacar(valiente);
            else System.out.println(Consola.color(Consola.ANSI_ROJO, "- " + monstruo.getNombreMostrado() + " falló el ataque!"));

            if (valiente.getMuerto()) return;

            // Turno valiente
            resolverAccionJugador(valiente, monstruo, opcion);

        } else {
            // Turno valiente
            resolverAccionJugador(valiente, monstruo, opcion);

            if (monstruo.getMuerto()) return;

            // Turno monstruo
            if (ataqueExitoso(monstruo, valiente)) monstruo.atacar(valiente);
            else System.out.println(Consola.color(Consola.ANSI_ROJO, "- " + monstruo.getNombreMostrado() + " falló el ataque!"));
        }
        //El veneno se resuelve una sola vez al final del turno
        monstruo.aplicarVeneno();
        monstruo.actualizarDebuffAtaque();
        valiente.actualizarBuff();
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "\n-HP " + valiente.getTipoValiente() + ": " + valiente.getVida()));
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, "-HP " + monstruo.getNombreMostrado() + ": " + monstruo.getVida()));
    }



    //Calcula iniciativa en función de velocidad
    public static double iniciativa(Object personaje) {
        double iniciativa = 0;
        if (personaje instanceof Valiente valiente) {
            iniciativa += valiente.getVelocidad() * ((Math.random() * 0.25) + 0.75);
        } else if (personaje instanceof Monstruo monstruo) {
            iniciativa += monstruo.getVelocidad() * ((Math.random() * 0.25) + 0.75);
        }
        return iniciativa;
    }

    //El acierto depende sobre todo de habilidad, un poco de evasión y también del nivel
    public static boolean ataqueExitoso(Object atacante, Object defensor) {
        double probAcierto;

        if (atacante instanceof Valiente ata && defensor instanceof Monstruo def) {
            probAcierto = calcularProbabilidadAcierto(
                    ata.getHabilidad(),
                    def.getVelocidad(),
                    ata.getNivel(),
                    def.getNivel()
            );
        } else if (atacante instanceof Monstruo ata && defensor instanceof Valiente def) {
            probAcierto = calcularProbabilidadAcierto(
                    ata.getHabilidad(),
                    def.getVelocidad(),
                    ata.getNivel(),
                    def.getNivel()
            );
        } else {
            return false;
        }

        int tirada = (int) (Math.random() * 101);
        return probAcierto > tirada;
    }

    //Recoge una accion valida del jugador y permite volver atras desde consumibles
    private static int turnoJugador(Valiente valiente, Monstruo monstruo) {
        while (true) {
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "1.Ataque   2.Usar habilidad   3.Usar objeto"));
            int opcion = SCANNER.nextInt();
            System.out.println();

            if (opcion == 3) {
                if (usarConsumibleEnCombate(valiente)) {
                    return opcion;
                }
            } else if (opcion == 2) {
                if (valiente.puedeUsarHabilidadEspecial(monstruo)) {
                    return opcion;
                }
            } else if (opcion == 1 || opcion == 2) {
                return opcion;
            } else {
                System.out.println(Consola.color(Consola.ANSI_ROJO, "Opción de turno no valida"));
            }
        }
    }

    //Resuelve la accion elegida por el jugador cuando llega su turno real
    private static void resolverAccionJugador(Valiente valiente, Monstruo monstruo, int opcion) {
        if (opcion == 3) {
            return;
        }
        if (ataqueExitoso(valiente, monstruo)) {
            opcionesCombate(valiente, monstruo, opcion);
        } else {
            System.out.println(Consola.color(Consola.ANSI_ROJO, "- " + valiente.getTipoValiente() + " tu ataque falló!"));
        }
    }

    //Muestra submenú de consumibles hasta usar uno o volver atras
    private static boolean usarConsumibleEnCombate(Valiente valiente) {
        while (true) {
            List<InventarioItem> consumibles = valiente.getInventario().getConsumibles();
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "Consumibles disponibles:\n"));

            if (consumibles.isEmpty()) {
                System.out.println(Consola.color(Consola.ANSI_ROJO, " XX No tienes consumibles."));
            }

            System.out.println(Consola.color(Consola.ANSI_AZUL, "1. Atrás"));
            for (int i = 0; i < consumibles.size(); i++) {
                InventarioItem item = consumibles.get(i);
                System.out.printf("%s%d. %s x%d (%d vida)%n%s",
                        Consola.ANSI_AZUL,
                        i + 2,
                        item.getEquipable().getNombre(),
                        item.getCantidad(),
                        item.getEquipable().getPoder(),
                        Consola.ANSI_RESET);
            }

            System.out.print(Consola.color(Consola.ANSI_AMARILLO, "\n- Elige consumible: "));
            int opcionConsumible = SCANNER.nextInt();
            if (opcionConsumible == 1) {
                return false;
            }
            if (opcionConsumible < 2 || opcionConsumible > consumibles.size() + 1) {
                System.out.println(Consola.color(Consola.ANSI_ROJO, "\nXX  Consumible no válido.  XX"));
                continue;
            }

            if (consumibles.isEmpty()) {
                continue;
            }

            InventarioItem seleccionado = consumibles.get(opcionConsumible - 2);
            if (valiente.getVida() >= valiente.getVidaMaxima()) {
                System.out.println(Consola.color(Consola.ANSI_ROJO, "\n- Tus HP está al máximo."));
                continue;
            }

            System.out.print(Consola.color(Consola.ANSI_AMARILLO, "- Cantidad a usar: "));
            int cantidad = SCANNER.nextInt();

            int vidaAntes = valiente.getVida();
            boolean usado = valiente.usarConsumible(seleccionado.getEquipable(), cantidad);
            if (usado) {
                int vidaRecuperada = valiente.getVida() - vidaAntes;
                System.out.println(Consola.color(Consola.ANSI_VERDE, "\n- Has usado " + cantidad + " " + seleccionado.getEquipable().getNombre()
                        + ", recuperas " + vidaRecuperada + " hp."));
                return true;
            }

            System.out.println(Consola.color(Consola.ANSI_ROJO, "No se pudo usar el consumible."));
        }
    }

    //Separa la fórmula para poder ajustar pesos sin tocar el flujo del combate
    private static double calcularProbabilidadAcierto(int habilidadAtacante, int velocidadDefensor, int nivelAtacante, int nivelDefensor) {
        double probabilidad = BASE_HIT
                + PESO_HABILIDAD * habilidadAtacante
                - PESO_EVASION * velocidadDefensor
                + PESO_NIVEL * (nivelAtacante - nivelDefensor);
        return limitarProbabilidad(probabilidad);
    }

    private static double limitarProbabilidad(double probabilidad) {
        return Math.max(PROBABILIDAD_MINIMA, Math.min(PROBABILIDAD_MAXIMA, probabilidad));
    }
}
