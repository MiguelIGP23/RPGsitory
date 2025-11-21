package migp.modelo;

import migp.datos.datosJuego.DaoEquipable;
import migp.modelo.enums.TiposValiente;

import java.util.ArrayList;
import java.util.List;

public class Valiente {

    //Atributos de clase - parámetros para daño de habilidades
    public static final float DANO_HAB_GUERRERO = 1.6f;
    public static final float DANO_HAB_PALADIN = 0.5f;
    public static final float DANO_HAB_MAGO = 0.4f;
    public static final float DANO_HAB_PICARO = 0.4f;

    public static final float AUMENTO_DEFENSA_PALADIN = 0.40f;
    public static final float REDUCCION_ATAQUE_MAGO = 0.30f;

    //Atributos de instancia
    private TiposValiente tipoValiente;
    private int vida;
    private int fuerza;
    private int defensa;
    private int habilidad;
    private int velocidad;
    private ArrayList<Equipable> inventario= new ArrayList<>();
    private Equipable arma;
    private Equipable escudo;
    private int nivel;

    private boolean muerto;
    private boolean buff;
    private int contadorBuff;

    private List<Monstruo> victorias;       //Guarda historial de monstruos derrotados

    public Valiente(TiposValiente tipoValiente, int vida, int fuerza, int defensa, int habilidad, int velocidad, Equipable arma, Equipable escudo, int nivel) {
        this.tipoValiente = tipoValiente;
        this.vida = vida;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.habilidad = habilidad;
        this.velocidad = velocidad;
        this.arma = arma;
        this.escudo = escudo;
        this.nivel = nivel;

        this.muerto = false;
        this.buff=false;
        this.contadorBuff=0;

        this.victorias = new ArrayList<>();
    }


    //Metodos get
    public TiposValiente getTipoValiente() {return tipoValiente;}

    public int getVida() {return vida;}

    public int getDefensa() {return defensa;}

    public int getVelocidad() {
        return velocidad;
    }

    public int getHabilidad() {
        return habilidad;
    }

    public boolean getMuerto() {
        return muerto;
    }

    public Equipable getEscudo() {
        return escudo;
    }


    //Métodos set
    public void setDefensa(int defensa) {this.defensa = defensa;}

    public void setArma(Equipable arma) {
        this.arma = arma;
    }

    public void setEscudo(Equipable escudo) {this.escudo = escudo;}


    //Metodo toString
    @Override
    public String toString() {
        return tipoValiente +
                ": Nivel: " + nivel +
                ", Vida: " + vida +
                ", Fuerza: " + fuerza +
                ", Defensa: " + defensa +
                ", Habilidad: " + habilidad +
                ", Velocidad: " + velocidad +
                ", Arma: " + arma.getNombre() +
                ", Escudo: " + escudo.getNombre()
                ;
    }


    //Quita bonus poder escudo actual y suma bonus poder escudo nuevo
    public void equiparEscudo(Equipable escudo) {
        DaoEquipable dao = new DaoEquipable();
        Equipable escudoActual = dao.buscarPorTipo(this.escudo.getNombre());
        Equipable escudoNuevo = dao.buscarPorTipo(escudo.getNombre());
        this.escudo = escudo;
        if(escudoActual!=null) {
            this.defensa -= escudoActual.getPoder();
        }
        this.defensa += escudoNuevo.getPoder();
    }

    //Ataca a monstruo
    public void atacar(Monstruo enemigo, int danoExtra) {
        //Vida-(fuerza+poderArma)
        int ataque = fuerza + danoExtra;
        DaoEquipable dao = new DaoEquipable();
        Equipable arma = dao.buscarPorTipo(this.arma.getNombre());
        if (arma != null) {
            ataque += arma.getPoder();
        }
        enemigo.recibirDaño(ataque);
    }

    //Recibe daño de monstruo
    public boolean recibirDano(int cantidad) {
        if (this.vida - cantidad <= 0) {
            this.vida = 0;
            this.muerto = true;
        } else {
            this.vida -= cantidad;
        }
        System.out.printf("-%s pierde %d de vida\n", tipoValiente, cantidad);
        return this.muerto;
    }

    //Usa habilidad especial
    public void usarHabilidadEspecial(Monstruo enemigo) {
        switch (tipoValiente) {
            case GUERRERO -> {
                //Golpe fuerte daño extra
                int danoExtra = Math.round(fuerza * DANO_HAB_GUERRERO);
                System.out.println(tipoValiente + " utilizó Carga Asesina!\n");
                atacar(enemigo, danoExtra);
            }
            case PALADIN -> {
                //Golpe flojo y aumenta defensa
                int danoExtra = Math.round(fuerza * (DANO_HAB_PALADIN-1));
                System.out.println(tipoValiente + " utilizó Armadura Sacra!");
                atacar(enemigo, danoExtra);
                this.defensa += Math.round(defensa*AUMENTO_DEFENSA_PALADIN);
                this.buff=true;
                System.out.println("Defensa de " + tipoValiente + " aumentada 40%");
            }
            case MAGO -> {
                //Golpe flojo y baja ataque
                int danoExtra = Math.round(fuerza * (DANO_HAB_MAGO-1));
                System.out.println(tipoValiente + " utilizo Bola de Escarcha!");
                atacar(enemigo, danoExtra);
                int ataqueReducido = Math.round(enemigo.getFuerza() * (REDUCCION_ATAQUE_MAGO-1));
                enemigo.setFuerza(ataqueReducido);
                System.out.println("Ataque " + enemigo + " reducido 30%\n");
            }
            case PICARO -> {
                //Golpe flojo y aplica veneno
                int danoExtra = Math.round(fuerza * (DANO_HAB_PICARO-1));
                System.out.println(tipoValiente + " utilizó Colmillo Podrido!");
                atacar(enemigo, danoExtra);
                if(!enemigo.getEnvenenado()) {
                    enemigo.cambiarEstadoVeneno(true);
                    System.out.println(enemigo.getTipoMonstruo() + " envenenado! (6vida/turno)");
                }
            }
        }
    }

    //Si recibe true cambia buff  a true e inicia contador 3 turnos
    public void cambiarEstadoBuff(boolean buff) {
        if(buff){
            this.buff = buff;
            contadorBuff=3;
        }else {
            this.buff=false;
        }
    }

    //Sube nivel y stats
    public void subirNivel() {
        System.out.printf("-Nivel " + nivel + " +1\t\t\t-Vida " + vida + " +10\n-Fuerza " + fuerza + " +1" +
                "\t\t-Defensa " + defensa + " +1\n-Habilidad " + habilidad + " +1\t-Velocidad " + velocidad + " +1\n\n");
        this.nivel++;
        this.vida += 10;
        this.fuerza++;
        this.defensa++;
        this.habilidad++;
        this.velocidad++;
        System.out.println(this.toString());
    }
}
