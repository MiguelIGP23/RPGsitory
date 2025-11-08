package migp;


import migp.datos.Equipable;
import migp.gestionBaseDatos.DaoEquipable;
import migp.gestionBaseDatos.DaoMonstruo;
import migp.gestionBaseDatos.DaoValiente;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        DaoMonstruo daom = new DaoMonstruo();
        System.out.println(daom.buscarPorTipo("ORCO"));
        DaoValiente daov= new DaoValiente();
        System.out.println(daov.buscarPorTipo("PALADIN").toString());
        DaoEquipable daoe = new DaoEquipable();
    }
}