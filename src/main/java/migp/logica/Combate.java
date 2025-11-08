package migp.logica;

import migp.modelo.Monstruo;
import migp.modelo.Valiente;

import java.util.Scanner;

public class Combate {


    //Inicia bucle de combate entre valiente y monstruo, termina cuando uno muere
    public static void iniciarCombate(Valiente valiente, Monstruo monstruo){
        while(!valiente.getMuerto() && !monstruo.getMuerto()){
            System.out.println("1.Ataque   2.Usar habilidad");
            switch (new Scanner(System.in).nextInt()){
                case 1 ->{
                    valiente.atacar(monstruo, 0);
                    monstruo.atacar(valiente);
                    System.out.println("\n-Monstruo HP: "+monstruo.getVida()+"\n-Valiente HP: "+valiente.getVida());
                }
                case 2 ->{
                    System.out.println("Ambos vivos");
                    valiente.usarHabilidadEspecial(monstruo);
                    monstruo.atacar(valiente);
                }
            }
            if(valiente.getMuerto()){
                System.out.println("xxx HAS MUERTO xxx");
            } else if (monstruo.getMuerto()) {
                System.out.println("-- VICTORIA --");
                valiente.subirNivel();
            }
        }

    }
}
