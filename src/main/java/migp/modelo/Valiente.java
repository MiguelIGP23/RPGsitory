package migp.modelo;

import migp.datos.datosJuego.DaoEquipable;
import migp.modelo.enums.TiposValiente;

import java.util.ArrayList;
import java.util.List;

public class Valiente {

    //Atributos de clase - parámetros para daño de habilidades
    public static final double DANO_HAB_GUERRERO = 0.6;
    public static final double DANO_HAB_PALADIN = 0.4;
    public static final double AUMENTO_DEFENSA_PALADIN = 0.4;
    public static final double DANO_HAB_MAGO = 0.5;
    public static final double REDUCCION_ATAQUE_MAGO = 0.30;
    public static final double DANO_HAB_PICARO = 0.6;

    //Atributos de instancia
    private TiposValiente tipoValiente;
    private int vida;
    private int fuerza;
    private int defensa;
    private int habilidad;
    private int velocidad;
    private String arma;
    private String escudo;
    private int anInt;

    private boolean muerto;
    private boolean buff;
    private int contadorBuff;

    private List<Monstruo> victorias;       //Guarda historial de monstruos derrotados

    public Valiente(TiposValiente tipoValiente, int vida, int fuerza, int defensa, int habilidad, int velocidad, String arma, String escudo, int nivel) {
        this.tipoValiente = tipoValiente;
        this.vida = vida;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.habilidad = habilidad;
        this.velocidad = velocidad;
        this.arma = arma;
        this.escudo = escudo;
        this.anInt = nivel;

        this.muerto = false;
        this.buff=false;
        this.contadorBuff=0;

        this.victorias = new ArrayList<>();
    }


    //Metodos get
    public TiposValiente getTipoValiente() {
        return tipoValiente;
    }

    public int getVida() {
        return vida;
    }

    public List<Monstruo> getVictorias() {
        return victorias;
    }

    public boolean getMuerto() {
        return muerto;
    }


    //Metodos set

    public void setArma(String arma) {
        this.arma = arma;
    }


    //Metodo toString
    @Override
    public String toString() {
        return "Valiente -> " +
                "Tipo: " + tipoValiente +
                ", Nivel: " + anInt +
                ", Vida: " + vida +
                ", Fuerza: " + fuerza +
                ", Defensa: " + defensa +
                ", Habilidad: " + habilidad +
                ", Velocidad: " + velocidad +
                ", Arma: " + arma +
                ", Escudo: " + escudo +
                '}';
    }


    //Quita bonus poder escudo actual y suma bonus poder escudo nuevo
    public void setEscudo(String escudo) {
        DaoEquipable dao = new DaoEquipable();
        Equipable escudoActual = dao.buscarPorTipo(this.escudo);
        Equipable escudoNuevo = dao.buscarPorTipo(escudo);

        this.defensa -= (defensa);
        this.escudo = escudo;
        Equipable es = dao.buscarPorTipo(escudo);
        this.defensa += es.getPoder();
    }

    //Ataca a monstruo
    public void atacar(Monstruo enemigo, int danoExtra) {
        //Vida-(fuerza+poderArma)
        int ataque = fuerza + danoExtra;
        DaoEquipable dao = new DaoEquipable();
        Equipable arma = dao.buscarPorTipo(this.arma);
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
        System.out.printf("%s pierde %d de vida\n", tipoValiente, cantidad);
        return this.muerto;
    }

    //Usa habilidad especial
    public void usarHabilidadEspecial(Monstruo enemigo) {
        switch (tipoValiente) {
            case GUERRERO -> {
                //Golpe 1.6
                int danoExtra = Math.round(fuerza * 0.6f);
                System.out.println(tipoValiente + " utilizó Carga Asesina!\n");
                atacar(enemigo, danoExtra);
            }
            case PALADIN -> {
                //Quita 0.6 y sube la defensa 40%
                int danoExtra = Math.round(fuerza * -0.40f);
                System.out.println(tipoValiente + " utilizó Armadura Sacra!\n");
                atacar(enemigo, danoExtra);
                this.defensa *= 1.4f;
                System.out.println("Defensa de " + tipoValiente + " aumentada 40%\n");
            }
            case MAGO -> {
                //Quita 0.5 y reduce ataque enemigo 30%
                int danoExtra = Math.round(fuerza * -0.50f);
                System.out.println(tipoValiente + " utilizo Bola de Escarcha!\n");
                atacar(enemigo, danoExtra);
                int ataqueReducido = Math.round(enemigo.getFuerza() * 0.7f);
                enemigo.setFuerza(ataqueReducido);
                System.out.println("Ataque " + enemigo + " reducido 30%\n");
            }
            case PICARO -> {
                //Quita 0.4 y aplica veneno que quita 6vida/turno
                int danoExtra = Math.round(fuerza * -0.6f);
                System.out.println(tipoValiente + " utilizó Colmillo Podrido!\n");
                atacar(enemigo, danoExtra);
                if(!enemigo.getEnvenenado()) {
                    enemigo.cambiarEstadoVeneno(true);
                    System.out.println(enemigo.getTipo() + " envenedado! (6vida/turno)");
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
        System.out.printf("-Nivel " + anInt + " +1\t\t\t-Vida " + vida + " +10\n-Fuerza " + fuerza + " +1" +
                "\t\t-Defensa " + defensa + " +1\n-Habilidad " + habilidad + " +1\t-Velocidad " + velocidad + " +1\n\n");
        this.anInt++;
        this.vida += 10;
        this.fuerza++;
        this.defensa++;
        this.habilidad++;
        this.velocidad++;
    }
}
