package migp.datos;

import migp.enums.TiposMonstruo;

public class Monstruo {

    private TiposMonstruo tipo;
    private int vida;
    private int fuerza;
    private int defensa;
    private int habilidad;
    private int velocidad;
    private int nivel;

    public Monstruo(TiposMonstruo tipo, int vida, int fuerza, int defensa, int habilidad, int velocidad, int nivel){
        this.tipo=tipo;
        this.vida=vida;
        this.fuerza=fuerza;
        this.defensa=defensa;
        this.habilidad=habilidad;
        this.velocidad=velocidad;
        this.nivel=nivel;
    }

    public TiposMonstruo getTipo() {
        return tipo;
    }

    public int getVida() {
        return vida;
    }

    public int getFuerza() {
        return fuerza;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getHabilidad() {
        return habilidad;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public String toString() {
        return "Monstruo{" +
                "tipo='" + tipo + '\'' +
                ", vida=" + vida +
                ", fuerza=" + fuerza +
                ", defensa=" + defensa +
                ", habilidad=" + habilidad +
                ", velocidad=" + velocidad +
                ", nivel=" + nivel +
                '}';
    }
}
