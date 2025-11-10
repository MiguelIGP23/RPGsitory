package migp;


import migp.datos.datosJuego.ConexionBaseDatos;
import migp.datos.datosJuego.DaoEquipable;
import migp.datos.persistencia.TempDatabase;
import migp.logica.Combate;
import migp.modelo.Equipable;
import migp.modelo.Monstruo;
import migp.modelo.Valiente;
import migp.datos.datosJuego.DaoMonstruo;
import migp.datos.datosJuego.DaoValiente;

public class Main {
    public static void main(String[] args) {

        DaoValiente daov = new DaoValiente();
        Valiente v = daov.buscarPorTipo("paladin".toUpperCase());

        DaoMonstruo daom = new DaoMonstruo();
        Monstruo m = daom.buscarPorTipo("almeja_gigante".toUpperCase());

        System.out.println();
        v.setArma("DAGA");
        v.setEscudo("ESCUDO_DE_HIERRO");
        Combate.iniciarCombate(v, m);


        //Cierra conexión db importante para borrar db temporal
        ConexionBaseDatos.getInstance().cerrar();
    }
}