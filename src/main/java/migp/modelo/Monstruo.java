package migp.modelo;

import migp.modelo.enums.TiposMonstruo;

public class Monstruo {

    //Atributos de clase - parámetros para ajustar daño
    public static final int DANO_VENENO = 6;
    public static final int TURNOS_VENENO = 3;
    public static final double SUBIDA_NIVEL =0.1;


    //Atributos de instancia
    private TiposMonstruo tipoMonstruo;
    private int vida;
    private int fuerza;
    private int defensa;
    private int habilidad;
    private int velocidad;
    private int nivel;

    private boolean muerto;
    private boolean envenenado;
    private int contadorVeneno;

    public Monstruo(TiposMonstruo tipo, int vida, int fuerza, int defensa, int habilidad, int velocidad) {
        double lvl = Math.random() * 10;
        this.nivel = ( lvl < 1) ? 1 : ((int) lvl);

        this.tipoMonstruo = tipo;
        this.vida = (int) (vida + (vida * 0.15 * (nivel - 1)));
        this.fuerza = (int) (fuerza + (fuerza * SUBIDA_NIVEL * (nivel - 1)));
        this.defensa = (int) (defensa + (defensa * SUBIDA_NIVEL * (nivel - 1)));
        this.habilidad = (int) (habilidad + (habilidad * SUBIDA_NIVEL * (nivel - 1)));
        this.velocidad = (int) (velocidad + (velocidad * SUBIDA_NIVEL * (nivel - 1)));

        this.muerto = false;
        this.envenenado = false;
        this.contadorVeneno = 0;
    }


    //Metodos get
    public TiposMonstruo getTipoMonstruo() {
        return tipoMonstruo;
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
        return tipoMonstruo +
                ", nivel=" + nivel +
                ": vida=" + vida +
                ", fuerza=" + fuerza +
                ", defensa=" + defensa +
                ", habilidad=" + habilidad +
                ", velocidad=" + velocidad
                ;
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
                System.out.println(tipoMonstruo + " ya no esta envenenado");
            }
        }
        if (this.vida - dano <= 0) {
            this.vida = 0;
            this.muerto = true;
        } else {
            this.vida -= dano;
        }
        System.out.printf("%s pierde %d de vida\n", tipoMonstruo, dano);
        return this.muerto;
    }

    //Si recibe true, cambia envenado a true e inicia contador 3 turnos veneno
    //Si recibe false anula estado veneno
    public void cambiarEstadoVeneno(boolean envenenar) {
        this.envenenado = envenenar;
        if (envenenar) {
            contadorVeneno = TURNOS_VENENO - 1;
        }
    }
}
