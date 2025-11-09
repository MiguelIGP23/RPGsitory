package migp;


import migp.logica.Combate;
import migp.modelo.Monstruo;
import migp.modelo.Valiente;
import migp.datos.datosJuego.DaoMonstruo;
import migp.datos.datosJuego.DaoValiente;

public class Main {
    public static void main(String[] args) {

        DaoMonstruo daom = new DaoMonstruo();
        Monstruo m = daom.buscarPorTipo("almeja_gigante".toUpperCase());

        DaoValiente daov = new DaoValiente();
        Valiente v = daov.buscarPorTipo("paladin".toUpperCase());


        System.out.println();
//        v.setArma("DAGA");
//        v.setEscudo("ESCUDO_DE_HIERRO");
        Combate.iniciarCombate(v, m);
    }
}