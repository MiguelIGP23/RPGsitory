package migp.modelo;

import migp.modelo.enums.TiposMonstruo;

public class Monstruo {

    //Atributos de clase - parámetros para ajustar daño
    public static final int DANO_VENENO = 6;
    public static final int TURNOS_VENENO = 3;

    //Atributos de instancia
    private TiposMonstruo tipo;
    private int vida;
    private int fuerza;
    private int defensa;
    private int habilidad;
    private int velocidad;
    private int nivel;

    private boolean muerto;
    private boolean envenenado;
    private int contadorVeneno;

    public Monstruo(TiposMonstruo tipo, int vida, int fuerza, int defensa, int habilidad, int velocidad, int nivel) {
        this.tipo = tipo;
        this.vida = vida;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.habilidad = habilidad;
        this.velocidad = velocidad;
        this.nivel = nivel;

        this.muerto = false;
        this.envenenado = false;
        this.contadorVeneno = 0;
    }


    //Metodos get
    public TiposMonstruo getTipo() {
        return tipo;
    }

    public int getFuerza() {
        return fuerza;
    }

    public boolean getMuerto() {
        return muerto;
    }

    public int getVida() {
        return vida;
    }

    public boolean getEnvenenado() {
        return envenenado;
    }

    //Metodos set
    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }


    //ToString
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


    //Ataca a valiente
    public void atacar(Valiente valiente) {

        valiente.recibirDano(fuerza);
    }

    //Recibe daño de valiente
    //Si esta envenenado, aplica daño veneno, reduce contador y quita veneno al llegar a 0
    public boolean recibirDaño(int cantidad) {
        int dano = cantidad;
        if (envenenado) {
            dano += DANO_VENENO;
            if ((contadorVeneno--) == 0) {
                cambiarEstadoVeneno(false);
                System.out.println(tipo + " ya no esta envenenado");
            }
        }
        if (this.vida - dano <= 0) {
            this.vida = 0;
            this.muerto = true;
        } else {
            this.vida -= dano;
        }
        System.out.printf("%s pierde %d de vida\n", tipo, dano);
        return this.muerto;
    }

    //Si recibe true, cambia envenado a true e inicia contador 3 turnos veneno
    //Si recibe false anula estado veneno
    public void cambiarEstadoVeneno(boolean envenenar) {
        this.envenenado = envenenar;
        if (envenenar) {
            contadorVeneno = TURNOS_VENENO-1;
        } else {
            this.envenenado = false;
        }
    }
}
