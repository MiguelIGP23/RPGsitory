package migp.logica;

import migp.modelo.Monstruo;
import migp.modelo.Valiente;

import java.util.List;
import java.util.Scanner;

public class Combate {

    //Atributos de clase para ajustar % de golpe
    public static final int BASE_HIT = 70;              //% de golpe medio ( si atk=def+esc )
    public static final double DIFICULTAD = 3.0;        //más valor -> meyor diferencia de %golpe por diferencia de nivel

    public static int turnoCombate;
    private static final Scanner SCANNER = new Scanner(System.in);


    //Iniciar bucle de combate entre valiente y monstruo, termina cuando uno muere
    public static void iniciarCombate(Valiente valiente, Monstruo monstruo) {
        //Guarda y restaura atributos del jugador al final del combate
        int fuerzaInicial = valiente.getFuerza();
        int defensaInicial = valiente.getDefensa();
        System.out.println("***  Apareció un " + monstruo.getTipoMonstruo() + " nivel " + monstruo.getNivel() + "!  ***\n");
        System.out.println(valiente);
        System.out.println(monstruo);
        turnoCombate=1;
        while (!valiente.getMuerto() && !monstruo.getMuerto()) {
            turno(valiente, monstruo);
            turnoCombate++;
        }
        if (valiente.getMuerto()){
            System.out.println("\n\t\txxx HAS MUERTO xxx");
        }else {
            System.out.println("\n\t\t-- VICTORIA --");
            System.out.println(valiente);
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
                System.out.println("Opción de turno no valida");
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
        System.out.printf("\n\n\t\tTURNO %d\n", turnoCombate);
        int opcion = turnoJugador(valiente);

        //La iniciativa decide quien ejecuta primero la accion
        boolean monstruoPrimero = iniMon > iniVal;

        if (monstruoPrimero) {
            // Turno monstruo
            if (ataqueExitoso(monstruo, valiente)) monstruo.atacar(valiente);
            else System.out.println("--" + monstruo.getTipoMonstruo() + " falló el ataque!");

            if (valiente.getMuerto()) return;

            // Turno valiente
            resolverAccionJugador(valiente, monstruo, opcion);

        } else {
            // Turno valiente
            resolverAccionJugador(valiente, monstruo, opcion);

            if (monstruo.getMuerto()) return;

            // Turno monstruo
            if (ataqueExitoso(monstruo, valiente)) monstruo.atacar(valiente);
            else System.out.println("-" + monstruo.getTipoMonstruo() + " falló el ataque!");
        }
        //El veneno se resuelve una sola vez al final del turno
        monstruo.aplicarVeneno();
        System.out.println("\n-HP " + valiente.getTipoValiente() + ": " + valiente.getVida());
        System.out.println("-HP " + monstruo.getTipoMonstruo() + ": " + monstruo.getVida());
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

    //La formula del enunciado da tasas de éxito ~0
//    public static boolean ataqueExitoso(Object atacante, Object defensor) {
//        boolean exito = false;
//        int random = (int) (Math.random() * 101);
//        if (atacante instanceof Valiente ata && defensor instanceof Monstruo def) {
//            if (random < (ata.getHabilidad() - def.getDefensa())) {
//                exito = true;
//            }
//        } else if (atacante instanceof Monstruo ata && defensor instanceof Valiente def) {
//            Equipable escudo = new DaoEquipable().buscarPorTipo(def.getEscudo());
//            int poderEscudo = (escudo!=null) ? escudo.getPoder() : 0;
//            if (random < (ata.getHabilidad() - (def.getDefensa() + poderEscudo))) {
//                exito = true;
//            }
//        }
//        return exito;
//    }

    //Para calcular % acierto usa formula % = base_hit + %diferencia * ( ata.habilidad - ( def.defensa + escudo.poder ))
    //Después lanza número entre 1 y 100, y si probabilidad es mayor que número aleatorio ataque es exitoso
    public static boolean ataqueExitoso(Object atacante, Object defensor) {
        boolean exito = false;
        double probAcierto;
        int probMinima;
        if (atacante instanceof Valiente ata && defensor instanceof Monstruo def) {
            probAcierto = BASE_HIT + DIFICULTAD * (ata.getHabilidad() - (def.getDefensa()));
            probMinima = (int) (Math.random() * 101);
            if (probAcierto>probMinima) {
                exito = true;
            }
        } else if (atacante instanceof Monstruo ata && defensor instanceof Valiente def) {
            probAcierto = BASE_HIT + DIFICULTAD * (ata.getHabilidad() - def.getDefensa());
            probMinima = (int) (Math.random() * 101);
            if (probAcierto>probMinima) {
                exito = true;
            }
        }
        return exito;
    }

    //Recoge una accion valida del jugador y permite volver atras desde consumibles
    private static int turnoJugador(Valiente valiente) {
        while (true) {
            System.out.println("1.Ataque   2.Usar habilidad   3.Usar objeto");
            int opcion = SCANNER.nextInt();
            System.out.println();

            if (opcion == 3) {
                if (usarConsumibleEnCombate(valiente)) {
                    return opcion;
                }
            } else if (opcion == 1 || opcion == 2) {
                return opcion;
            } else {
                System.out.println("Opción de turno no valida");
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
            System.out.println("-" + valiente.getTipoValiente() + " tu ataque falló!");
        }
    }

    //Muestra submenú de consumibles hasta usar uno o volver atras
    private static boolean usarConsumibleEnCombate(Valiente valiente) {
        while (true) {
            List<InventarioItem> consumibles = valiente.getInventario().getConsumibles();
            System.out.println("Consumibles disponibles:\n");

            if (consumibles.isEmpty()) {
                System.out.println(" XX No tienes consumibles.");
            }

            System.out.println("1. Atrás");
            for (int i = 0; i < consumibles.size(); i++) {
                InventarioItem item = consumibles.get(i);
                System.out.printf("%d. %s x%d (%d vida)%n",
                        i + 2,
                        item.getEquipable().getNombre(),
                        item.getCantidad(),
                        item.getEquipable().getPoder());
            }

            System.out.print("\n- Elige consumible: ");
            int opcionConsumible = SCANNER.nextInt();
            if (opcionConsumible == 1) {
                return false;
            }
            if (opcionConsumible < 2 || opcionConsumible > consumibles.size() + 1) {
                System.out.println("\nXX  Consumible no válido.  XX");
                continue;
            }

            if (consumibles.isEmpty()) {
                continue;
            }

            InventarioItem seleccionado = consumibles.get(opcionConsumible - 2);
            System.out.print("- Cantidad a usar: ");
            int cantidad = SCANNER.nextInt();

            boolean usado = valiente.usarConsumible(seleccionado.getEquipable(), cantidad);
            if (usado) {
                System.out.println("\nHas usado " + cantidad + " " + seleccionado.getEquipable().getNombre() + ".");
                return true;
            }

            System.out.println("No se pudo usar el consumible.");
        }
    }
}
