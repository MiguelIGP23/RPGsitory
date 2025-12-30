package migp;


import migp.modelo.InventarioItem;
import migp.persistencia.ConexionBaseDatos;
import migp.persistencia.DaoEquipable;
import migp.logica.Combate;
import migp.modelo.Equipable;
import migp.modelo.Monstruo;
import migp.modelo.Valiente;
import migp.persistencia.DaoMonstruo;
import migp.persistencia.DaoValiente;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        DaoValiente daov = new DaoValiente();
        Valiente v = daov.buscarPorTipo("paladin".toUpperCase());

        DaoMonstruo daom = new DaoMonstruo();
        Monstruo m = daom.buscarPorTipo("almeja_gigante".toUpperCase());


        // Prueba de inventario

//        DaoEquipable daoe= new DaoEquipable();
//        Equipable arma = daoe.buscarPorTipo("DAGA");
//        Equipable escudo = daoe.buscarPorTipo("ESCUDO_DE_HIERRO");
//
//        v.getInventario().agregarItem(arma, 1);
//        v.getInventario().agregarItem(escudo, 3);
//
//        List<InventarioItem> items = v.getInventario().getItems();
//
//        System.out.println(items);
//
//        v.equiparArma(arma);
//        v.equiparEscudo(escudo);
//        System.out.println(v);
//
//        v.desequiparArma();
//        v.desequiparEscudo();
//        System.out.println(v);


        // Prueba de combate

//        Combate.iniciarCombate(v, m);

        //Cierra conexión db importante para borrar db temporal
        ConexionBaseDatos.getInstance().cerrar();
    }
}