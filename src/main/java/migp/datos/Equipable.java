package migp.datos;

import migp.enums.TiposEquipable;

public class Equipable {

    private TiposEquipable tipo;
    private String nombre;
    private int poder;      //implica daño hecho o daño resistido

    public Equipable(TiposEquipable tipo, String nombre, int poder){
        this.tipo=tipo;
        this.nombre=nombre;
        this.poder=poder;
    }

    public TiposEquipable getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPoder() {
        return poder;
    }

    @Override
    public String toString() {
        return "Equipable{" +
                "tipo=" + tipo +
                ", nombre='" + nombre + '\'' +
                ", poder=" + poder +
                '}';
    }
}
