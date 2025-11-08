package migp.datos;

import migp.enums.TiposEquipable;

public class Equipable {

    private String nombre;
    private TiposEquipable tipo;
    private int poder;      //implica daño hecho o daño resistido

    public Equipable(String nombre,TiposEquipable tipo,  int poder){
        this.nombre=nombre;
        this.tipo=tipo;
        this.poder=poder;
    }

    public String getNombre() {
        return nombre;
    }

    public TiposEquipable getTipo() {
        return tipo;
    }

    public int getPoder() {
        return poder;
    }

    @Override
    public String toString() {
        return "Equipable{" +
                ", nombre='" + nombre + '\'' +
                "tipo=" + tipo +
                ", poder=" + poder +
                '}';
    }
}
