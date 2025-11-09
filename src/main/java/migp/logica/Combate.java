package migp.logica;

import migp.datos.datosJuego.DaoEquipable;
import migp.modelo.Equipable;
import migp.modelo.Monstruo;
import migp.modelo.Valiente;

import java.util.Scanner;

public class Combate {

    //Atributos de clase para ajustar % de golpe
    public static final int BASE_HIT = 70;              //% de golpe si atk=def+esc
    public static final double DIFICULTAD = 3.0;        //más valor -> más diferencia entre niveles para % golpeo

    public static int turnoCombate;


    //Iniciar bucle de combate entre valiente y monstruo, termina cuando uno muere
    public static void iniciarCombate(Valiente valiente, Monstruo monstruo) {
        System.out.println("*** Apareció un " + monstruo.getTipoMonstruo() + "!***\n");
        turnoCombate=1;
        while (!valiente.getMuerto() && !monstruo.getMuerto()) {
//            System.out.println(valiente.toString());
//            System.out.println(monstruo.toString()+"\n");
            turno(valiente, monstruo);
            turnoCombate++;
        }
        if (valiente.getMuerto()){
            System.out.println("\n\t\txxx HAS MUERTO xxx");
        }else {
            System.out.println("\n\t\t-- VICTORIA --");
            valiente.subirNivel();
        }

    }

    private static void opcionesCombate(Valiente valiente, Monstruo monstruo, int opcion) {
        switch (opcion) {
            case 1 -> valiente.atacar(monstruo, 0);
            case 2 -> valiente.usarHabilidadEspecial(monstruo);
            default -> System.out.println("Opción de turno no valida");
        }
    }


    //Aplica iniciativa y realiza los ataques
    public static void turno(Valiente valiente, Monstruo monstruo) {
        //Calcula iniciativa
        double iniVal = iniciativa(valiente);
        double iniMon = iniciativa(monstruo);
        //Recoge opcion menu antes de empezar ataques
        System.out.printf("\n\n\t\tTURNO %d\n1.Ataque   2.Usar habilidad\n", turnoCombate);
        int opcion = new Scanner(System.in).nextInt();
        System.out.println();
        if (iniMon > iniVal) {
            if (ataqueExitoso(monstruo, valiente)) {
                monstruo.atacar(valiente);

            } else {
                System.out.println(monstruo.getTipoMonstruo() + " falló el ataque!");
            }
            if (!valiente.getMuerto()) {
                if (ataqueExitoso(valiente, monstruo)) {
                    opcionesCombate(valiente, monstruo, opcion);
                } else {
                    System.out.println(valiente.getTipoValiente() + " tu ataque falló!");
                }
            }
        } else {
            if (ataqueExitoso(valiente, monstruo)) {
                opcionesCombate(valiente, monstruo, opcion);
            }else {
                System.out.println(valiente.getTipoValiente() + " tu ataque falló!");
            }
            if (!monstruo.getMuerto()) {
                if (ataqueExitoso(monstruo, valiente)) {
                    monstruo.atacar(valiente);
                }else{
                    System.out.println(monstruo.getTipoMonstruo() + " falló el ataque!");
                }
            }
        }
        System.out.println("\n-HP "+valiente.getTipoValiente()+": "+valiente.getVida());
        System.out.println("-HP "+monstruo.getTipoMonstruo()+": "+monstruo.getVida());
    }



    //Calcula iniciativa en funcion de velocidad
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
            Equipable escudo = new DaoEquipable().buscarPorTipo(def.getEscudo());
            int poderEscudo = (escudo!=null) ? escudo.getPoder() : 0;
            probAcierto = BASE_HIT + DIFICULTAD * (ata.getHabilidad() - (def.getDefensa()+poderEscudo));
            probMinima = (int) (Math.random() * 101);
            if (probAcierto>probMinima) {
                exito = true;
            }
        }
        return exito;
    }


}
