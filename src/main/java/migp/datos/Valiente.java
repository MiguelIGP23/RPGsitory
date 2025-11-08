package migp.datos;

import migp.enums.TiposMonstruo;
import migp.enums.TiposValiente;

public class Valiente {

    private TiposValiente tipoValiente;
    private int vida;
    private int fuerza;
    private int defensa;
    private int habilidad;
    private int velocidad;
    private String arma;
    private String escudo;
    private int nivel;

    public Valiente(TiposValiente tipoValiente, int vida, int fuerza, int defensa, int habilidad, int velocidad, String arma, String escudo, int nivel){
        this.tipoValiente = tipoValiente;
        this.vida=vida;
        this.fuerza=fuerza;
        this.defensa=defensa;
        this.habilidad=habilidad;
        this.velocidad=velocidad;
        this.arma=arma;
        this.escudo=escudo;
        this.nivel=nivel;
    }

    public TiposValiente getTipoValiente() {
        return tipoValiente;
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
        return "Valiente{" +
                "tipo='" + tipoValiente + '\'' +
                ", vida=" + vida +
                ", fuerza=" + fuerza +
                ", defensa=" + defensa +
                ", habilidad=" + habilidad +
                ", velocidad=" + velocidad +
                ", arma= " +arma +
                ", escudo= " +escudo+
                ", nivel=" + nivel +
                '}';
    }
}
