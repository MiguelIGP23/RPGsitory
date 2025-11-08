package migp;


import migp.datos.Equipable;
import migp.datos.Monstruo;
import migp.datos.Valiente;
import migp.gestionBaseDatos.DaoEquipable;
import migp.gestionBaseDatos.DaoMonstruo;
import migp.gestionBaseDatos.DaoValiente;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        DaoMonstruo daom = new DaoMonstruo();
        Monstruo m = daom.buscarPorTipo("DRAGON_ROJO");

        DaoValiente daov= new DaoValiente();
        Valiente v = daov.buscarPorTipo("picaro".toUpperCase());

        System.out.println(v.toString());
        System.out.println(m.toString());
        System.out.println();

        v.setArma("DAGA");
        v.atacar(m,0);
        System.out.println();
        System.out.println(m.toString());

        v.usarHabilidadEspecial(m);
        System.out.println();
        System.out.println(m.toString());

        v.setArma("DAGA");
        v.atacar(m,0);
        System.out.println();
        System.out.println(m.toString());
    }
}