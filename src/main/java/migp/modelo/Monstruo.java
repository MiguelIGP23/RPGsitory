package migp.modelo;

import migp.logica.Consola;
import migp.modelo.enums.TiposMonstruo;

public class Monstruo {

    //Atributos de clase - parámetros para ajustar daño
    public static final int DANO_VENENO = 5;
    public static final int TURNOS_VENENO = 3;
    public static final int TURNOS_DEBUFF_ATAQUE = 3;
    public static final float MITIGACION_DEFENSA = 0.4f;


    //Atributos de instancia
    private TiposMonstruo tipoMonstruo;
    private String nombreMostrado;
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
        this(tipo, vida, fuerza, defensa, habilidad, velocidad, 1);
    }

    public Monstruo(TiposMonstruo tipo, int vida, int fuerza, int defensa, int habilidad, int velocidad, int nivel) {
        this.tipoMonstruo = tipo;
        this.nombreMostrado = tipo.name();
        this.vida = vida;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.habilidad = habilidad;
        this.velocidad = velocidad;
        this.nivel = nivel;

        this.muerto = false;
        this.envenenado = false;
        this.contadorVeneno = 0;
        this.ataqueDebilitado = false;
        this.contadorDebuffAtaque = 0;
        this.reduccionFuerzaDebuff = 0;
    }

    public Monstruo(String nombreMostrado, int vida, int fuerza, int defensa, int habilidad, int velocidad, int nivel) {
        this.tipoMonstruo = null;
        this.nombreMostrado = nombreMostrado;
        this.vida = vida;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.habilidad = habilidad;
        this.velocidad = velocidad;
        this.nivel = nivel;

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

    public String getNombreMostrado() {
        return nombreMostrado;
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
        return nombreMostrado +
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
        int danoReal = valiente.recibirGolpe(fuerza);
        System.out.println(Consola.color(Consola.ANSI_ROJO, "- " + nombreMostrado + " ataca, " + valiente.getTipoValiente() + " pierde " + danoReal + " de vida"));
    }

    //Recibe daño de valiente
    //Aplica mitigación por defensa y devuelve el daño real recibido
    public int recibirGolpe(int ataqueBruto) {
        int reduccion = Math.round(defensa * MITIGACION_DEFENSA);
        int danoReal = Math.max(1, ataqueBruto - reduccion);
        if (this.vida - danoReal <= 0) {
            this.vida = 0;
            this.muerto = true;
        } else {
            this.vida -= danoReal;
        }
        return danoReal;
    }

    //Aplica solo el daño directo que no debe mitigarse
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
        System.out.println(Consola.color(Consola.ANSI_AMARILLO, "\n-" + nombreMostrado + " pierde " + danoVeneno + " de vida por veneno"));

        if ((contadorVeneno--) == 0) {
            cambiarEstadoVeneno(false);
            if (!muerto) {
                System.out.println(Consola.color(Consola.ANSI_AMARILLO, "-" + nombreMostrado + " ya no está envenenado."));
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
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "-" + nombreMostrado + " recupera su fuerza."));
        }
    }
}
