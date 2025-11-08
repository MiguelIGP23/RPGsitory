package migp;


import migp.modelo.Monstruo;
import migp.modelo.Valiente;
import migp.datos.datosJuego.DaoMonstruo;
import migp.datos.datosJuego.DaoValiente;

public class Main {
    public static void main(String[] args) {

        DaoMonstruo daom = new DaoMonstruo();
        Monstruo m = daom.buscarPorTipo("DRAGON_ROJO");

        DaoValiente daov = new DaoValiente();
        Valiente v = daov.buscarPorTipo("picaro".toUpperCase());

        System.out.println(v.toString());
        System.out.println(m.toString());

        v.setArma("DAGA");
        v.atacar(m, 0);
        System.out.println(m.toString());

        v.usarHabilidadEspecial(m);
        System.out.println(m.toString());

        v.setArma("DAGA");
        v.atacar(m, 0);
        System.out.println(m.toString())
        ;
        v.setArma("DAGA");
        v.atacar(m, 0);
        System.out.println(m.toString());
    }
}