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

    private boolean envenenado;

    public Monstruo(TiposMonstruo tipo, int vida, int fuerza, int defensa, int habilidad, int velocidad, int nivel){
        this.tipo=tipo;
        this.vida=vida;
        this.fuerza=fuerza;
        this.defensa=defensa;
        this.habilidad=habilidad;
        this.velocidad=velocidad;
        this.nivel=nivel;
        this.envenenado=false;
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

    public void setVida(int vida) {
        this.vida = vida;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public void setHabilidad(int habilidad) {
        this.habilidad = habilidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
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

    public void atacar(Valiente valiente){
        valiente.recibirDano(fuerza);
    }

    public void recibirDaño(int cantidad){
        int dano=cantidad;
        if(envenenado){
            dano+=6;
        }
        if(this.vida-dano<0) {
            this.vida=0;
        }else {
            this.vida -= dano;
        }
        System.out.printf("%s pierde %d de vida\n", tipo, dano);
    }

    public void aplicarVeneno(){
        this.envenenado=true;
    }
}
