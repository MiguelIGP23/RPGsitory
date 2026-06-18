package migp.modelo;

import migp.logica.Consola;
import migp.modelo.enums.TiposMonstruo;

public class Monstruo {

    //Atributos de clase - parámetros para ajustar daño
    public static final int DANO_VENENO = 6;
    public static final int TURNOS_VENENO = 3;
    public static final int TURNOS_DEBUFF_ATAQUE = 3;
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
    private boolean ataqueDebilitado;
    private int contadorDebuffAtaque;
    private int reduccionFuerzaDebuff;

    public Monstruo(TiposMonstruo tipo, int vida, int fuerza, int defensa, int habilidad, int velocidad) {
        double lvl = Math.random() * 10;
        this.nivel = ( lvl < 1) ? 1 : ((int) lvl);
//        this.nivel=1;

        this.tipoMonstruo = tipo;
        this.vida = (int) (vida + (vida * 0.15 * (nivel - 1)));
        this.fuerza = (int) (fuerza + (fuerza * SUBIDA_NIVEL * (nivel - 1)));
        this.defensa = (int) (defensa + (defensa * SUBIDA_NIVEL * (nivel - 1)));
        this.habilidad = (int) (habilidad + (habilidad * SUBIDA_NIVEL * (nivel - 1)));
        this.velocidad = (int) (velocidad + (velocidad * SUBIDA_NIVEL * (nivel - 1)));

        this.muerto = false;
        this.envenenado = false;
        this.contadorVeneno = 0;
        this.ataqueDebilitado = false;
        this.contadorDebuffAtaque = 0;
        this.reduccionFuerzaDebuff = 0;
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

    public int getNivel() {return nivel;}

    public boolean getMuerto() {
        return muerto;
    }

    public int getVida() {
        return vida;
    }

    public boolean getEnvenenado() {
        return envenenado;
    }

    public boolean getAtaqueDebilitado() {
        return ataqueDebilitado;
    }

    //Metodos set
    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    //Reduce temporalmente la fuerza del monstruo si no estaba ya debilitado
    public boolean aplicarDebuffAtaque(float reduccionPorcentual) {
        if (ataqueDebilitado) {
            return false;
        }

        reduccionFuerzaDebuff = Math.max(1, Math.round(fuerza * reduccionPorcentual));
        fuerza = Math.max(1, fuerza - reduccionFuerzaDebuff);
        ataqueDebilitado = true;
        contadorDebuffAtaque = TURNOS_DEBUFF_ATAQUE;
        return true;
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
        int vidaInicialValiente = valiente.getVida();
        valiente.recibirDano(fuerza);
        int danoReal = vidaInicialValiente - valiente.getVida();
        System.out.println(Consola.color(Consola.ANSI_ROJO, "- " + tipoMonstruo + " ataca, " + valiente.getTipoValiente() + " pierde " + danoReal + " de vida"));
    }

    //Recibe daño de valiente
    //Aplica solo el daño del golpe recibido
    public boolean recibirDaño(int cantidad) {
        if (this.vida - cantidad <= 0) {
            this.vida = 0;
            this.muerto = true;
        } else {
            this.vida -= cantidad;
        }
        return this.muerto;
    }

    //Aplica el daño del veneno al final del golpe si el monstruo está envenenado
    public void aplicarVeneno() {
        if (!envenenado || muerto) {
            return;
        }

        int danoVeneno = Math.min(DANO_VENENO, vida);
        vida -= danoVeneno;
        if (vida <= 0) {
            vida = 0;
            muerto = true;
        }
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "\n-" + tipoMonstruo + " pierde " + danoVeneno + " de vida por veneno"));

        if ((contadorVeneno--) == 0) {
            cambiarEstadoVeneno(false);
            if (!muerto) {
                System.out.println(Consola.color(Consola.ANSI_AMARILLO, "-" + tipoMonstruo + " ya no está envenenado."));
            }
        }
    }

    //Si recibe true, cambia envenado a true e inicia contador 3 turnos veneno
    //Si recibe false anula estado veneno
    public void cambiarEstadoVeneno(boolean envenenar) {
        this.envenenado = envenenar;
        if (envenenar) {
            contadorVeneno = TURNOS_VENENO - 1;
        }
    }

    //Actualiza la duración del debuff de ataque y restaura la fuerza al caducar
    public void actualizarDebuffAtaque() {
        if (!ataqueDebilitado) {
            return;
        }

        contadorDebuffAtaque--;
        if (contadorDebuffAtaque <= 0) {
            fuerza += reduccionFuerzaDebuff;
            ataqueDebilitado = false;
            reduccionFuerzaDebuff = 0;
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "-" + tipoMonstruo + " recupera su fuerza."));
        }
    }
}
