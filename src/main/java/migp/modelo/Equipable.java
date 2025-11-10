package migp.modelo;

import migp.modelo.enums.TiposEquipable;

public class Equipable {

    private String nombre;
    private TiposEquipable tipo;
    private int poder;      //implica daño hecho, resistido o hp curado

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
        return this.nombre +" "+ nombre+", poder=" + poder;
    }
}
