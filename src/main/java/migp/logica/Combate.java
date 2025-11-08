package migp.logica;

import migp.datos.datosJuego.DaoEquipable;
import migp.modelo.Equipable;
import migp.modelo.Monstruo;
import migp.modelo.Valiente;

import java.util.Scanner;

public class Combate {


    //Inicia bucle de combate entre valiente y monstruo, termina cuando uno muere
    public static void iniciarCombate(Valiente valiente, Monstruo monstruo) {
        System.out.println("*** Apareció un " + monstruo.getTipoMonstruo() + "!***\n");
        while (!valiente.getMuerto() && !monstruo.getMuerto()) {
            System.out.println(valiente.toString());
            System.out.println(monstruo.toString()+"\n");
            turno(valiente, monstruo);
            System.out.println("hpval "+valiente.getVida()+" - hpmon "+monstruo.getVida()+"\n");
        }
        if (valiente.getMuerto()){
            System.out.println("xxx HAS MUERTO xxx");
        }else {
            System.out.println("-- VICTORIA --");
            valiente.subirNivel();
        }

    }

    //Aplica iniciativa y realiza los ataques
    public static void turno(Valiente valiente, Monstruo monstruo) {
        //Calcula iniciativa
        double iniVal = iniciativa(valiente);
        double iniMon = iniciativa(monstruo);
        System.out.println(":::TAG - despues calcular iniciativas   iniVal "+iniVal+"   iniMon "+iniMon);
        //Recoge opcion menu antes de empezar ataques
        System.out.println("1.Ataque   2.Usar habilidad");
        int opcion = new Scanner(System.in).nextInt();
        if (iniMon > iniVal) {
            if (ataqueExitoso(monstruo, valiente)) {
                monstruo.atacar(valiente);
                System.out.println(":::TAG - despues ataque monstruo + rapido");
                System.out.println("-HP "+valiente.getTipoValiente()+": "+valiente.getVida());
            } else {
                System.out.println(monstruo.getTipoMonstruo() + " falló el ataque!");
            }
            if (!valiente.getMuerto()) {
                if (ataqueExitoso(valiente, monstruo)) {
                    opcionesCombate(valiente, monstruo, opcion);
                    System.out.println(":::TAG - despues ataque valiente - rapido");
                    System.out.println("-HP "+monstruo.getTipoMonstruo()+": "+monstruo.getVida());
                } else {
                    System.out.println(valiente.getTipoValiente() + " tu ataque falló!\n");
                }
            }
        } else {
            if (ataqueExitoso(valiente, monstruo)) {
                opcionesCombate(valiente, monstruo, opcion);
                System.out.println(":::TAG - despues de ataque valiente + rapido");
                System.out.println("-HP "+monstruo.getTipoMonstruo()+": "+monstruo.getVida());
            }else {
                System.out.println(valiente.getTipoValiente() + " tu ataque falló!");
            }
            if (!monstruo.getMuerto()) {
                if (ataqueExitoso(monstruo, valiente)) {
                    monstruo.atacar(valiente);
                    System.out.println(":::TAG - despues golpde monstruo - rapido");
                    System.out.println("-HP "+valiente.getTipoValiente()+": "+valiente.getVida());
                }else{
                    System.out.println(monstruo.getTipoMonstruo() + " falló el ataque!\n");
                }
            }
        }
    }

    private static void opcionesCombate(Valiente valiente, Monstruo monstruo, int opcion) {
        switch (opcion) {
            case 1 -> valiente.atacar(monstruo, 0);
            case 2 -> valiente.usarHabilidadEspecial(monstruo);
            default -> System.out.println("Opción de turno no valida");
        }
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

    //Determina si ataque tiene exito
    public static boolean ataqueExitoso(Object atacante, Object defensor) {
        boolean exito = false;
        int random = (int) (Math.random() * 101);
        if (atacante instanceof Valiente ata && defensor instanceof Monstruo def) {
            if (random < (ata.getHabilidad() - def.getDefensa())) {
                exito = true;
            }
        } else if (atacante instanceof Monstruo ata && defensor instanceof Valiente def) {
            Equipable escudo = new DaoEquipable().buscarPorTipo(def.getEscudo());
            int poderEscudo = (escudo!=null) ? escudo.getPoder() : 0;
            if (random < (ata.getHabilidad() - (def.getDefensa() + poderEscudo))) {
                exito = true;
            }
        }
        return exito;
    }


}
