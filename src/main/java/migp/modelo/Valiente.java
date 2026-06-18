package migp.modelo;

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
    private int vidaMaxima;
    private int fuerza;
    private int defensa;
    private int habilidad;
    private int velocidad;
    private Inventario inventario;
    private Equipable arma;
    private Equipable escudo;
    private int nivel;

    private boolean muerto;
    private boolean buff;
    private int contadorBuff;

    private List<Monstruo> victorias;       //Guarda historial de monstruos derrotados

    public Valiente(TiposValiente tipoValiente, int vida, int fuerza, int defensa, int habilidad, int velocidad, Equipable arma, Equipable escudo, int nivel) {
        this.tipoValiente = tipoValiente;
        this.vidaMaxima = vida;
        this.vida = vida;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.habilidad = habilidad;
        this.velocidad = velocidad;
        this.inventario = new Inventario();
        this.arma = null;
        this.escudo = null;
        this.nivel = nivel;

        this.muerto = false;
        this.buff = false;
        this.contadorBuff = 0;

        this.victorias = new ArrayList<>();

        if (arma != null) {
            inventario.agregarItem(arma, 1);
            equiparArma(arma);
        }
        if (escudo != null) {
            inventario.agregarItem(escudo, 1);
            equiparEscudo(escudo);
        }
    }


    //Metodos get
    public TiposValiente getTipoValiente() {
        return tipoValiente;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getFuerza() {
        return fuerza;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getHabilidad() {
        return habilidad;
    }

    public boolean getMuerto() {
        return muerto;
    }

    public Equipable getArma() {
        return arma;
    }

    public Equipable getEscudo() {
        return escudo;
    }

    public Inventario getInventario() {
        return inventario;
    }


    //Métodos set
    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public void setArma(Equipable arma) {
        this.arma = arma;
    }

    public void setEscudo(Equipable escudo) {
        this.escudo = escudo;
    }


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
                ", Arma: " + (arma != null ? arma.getNombre() + "-" + arma.getPoder() : "-") +
                ", Escudo: " + (escudo != null ? escudo.getNombre() + "-" + escudo.getPoder() : "-")
                ;
    }


    // ----------------------------------------------
    // REFERENTE AL INVENTARIO  Y OBJETOS
    // ----------------------------------------------

    // Desequipa el arma actual y equipa el nuevo comprobando que esté en el inventario
    // Usa stats del arma del inventario para evitar el uso de objetos externos
    public void equiparArma(Equipable armaNueva) {
        if (armaNueva == null) {
            desequiparArma();
            return;
        }
        List<InventarioItem> listaArmas = inventario.getArmas();
        Equipable armaInventario = null;    //Evita meter armas desde fuera, usa la del inventario
        for (InventarioItem arma : listaArmas) {
            if (armaNueva.getNombre().equalsIgnoreCase(arma.getEquipable().getNombre())) {
                armaInventario = arma.getEquipable();
                break;
            }
        }
        if (armaInventario != null) {
            desequiparArma();
            this.arma = armaInventario;
            this.fuerza += armaInventario.getPoder();
        }
    }

    // Desequipa el arma actual si la hubiera y reduce la fuerza
    public void desequiparArma() {
        if (this.arma != null) {
            int poderArma = this.arma.getPoder();
            this.fuerza -= poderArma;
            this.arma = null;
        }
    }

    // Quita bonus de defensa actual y suma bonus de poder de nuevo escudo
    public void equiparEscudo(Equipable escudoNuevo) {
        if (escudoNuevo == null) {
            desequiparEscudo();
            return;
        }
        List<InventarioItem> listaEscudos = inventario.getEscudos();
        Equipable escudoInventario = null;    //Evita meter escudos desde fuera, usa el del inventario
        for (InventarioItem escudo : listaEscudos) {
            if (escudoNuevo.getNombre().equalsIgnoreCase(escudo.getEquipable().getNombre())) {
                escudoInventario = escudo.getEquipable();
                break;
            }
        }
        if (escudoInventario != null) {
            desequiparEscudo();
            this.escudo = escudoInventario;
            this.defensa += escudoInventario.getPoder();
        }
    }

    //Desequipa el escudo actual si la hubiera y reduce la defensa
    public void desequiparEscudo() {
        if (this.escudo != null) {
            int poderEscudo = this.escudo.getPoder();
            this.defensa -= poderEscudo;
            this.escudo = null;
        }
    }

    // Usa consumible, aplica el efecto y resta del inventario
    public boolean usarConsumible(Equipable usado, int cantidad) {
        if (usado == null || cantidad <= 0) return false;
        List<InventarioItem> listaConsumibles = inventario.getConsumibles();
        for (InventarioItem consumible : listaConsumibles) {
            if (usado.getNombre().equalsIgnoreCase(consumible.getEquipable().getNombre()) && cantidad <= consumible.getCantidad()) {
                this.vida = Math.min(vidaMaxima, this.vida + (consumible.getEquipable().getPoder() * cantidad));
                inventario.eliminarItem(consumible.getEquipable(), cantidad);
                return true;
            }
        }
        return false;
    }

    // Agrega un objeto al inventario
    public void nuevoObjeto(Equipable nuevo, int cantidad) {
        inventario.agregarItem(nuevo, cantidad);
    }

    // Elimina un objeto del inventario y lo desequipa si estuviera equipado
    public void eliminarObjeto(Equipable eliminado, int cantidad) {
        if (eliminado == null || cantidad <= 0) return;
        boolean enInventario = inventario.getItems().stream().anyMatch(item -> item.getEquipable().getNombre().equalsIgnoreCase(eliminado.getNombre()));
        if (!enInventario) return;
        int restantes = inventario.eliminarItem(eliminado, cantidad);
        String objEliminado = eliminado.getNombre();
        if (restantes <= 0) {
            if (this.arma != null && this.arma.getNombre().equalsIgnoreCase(objEliminado)) {
                this.desequiparArma();
            } else if (this.escudo != null && this.escudo.getNombre().equalsIgnoreCase(objEliminado)) {
                this.desequiparEscudo();
            }
        }
    }

    // ----------------------------------------------
    // REFERENTE AL COMBATE
    // ----------------------------------------------

    //Ataca a monstruo
    public void atacar(Monstruo enemigo, int danoExtra) {
        //Vida-(fuerza+poderArma)
        int ataque = this.fuerza + danoExtra;
        int vidaInicialEnemigo = enemigo.getVida();
        enemigo.recibirDaño(ataque);
        int danoReal = vidaInicialEnemigo - enemigo.getVida();
        System.out.println("- " + tipoValiente + " ataca, " + enemigo.getTipoMonstruo() + " pierde " + danoReal + " de vida");
    }

    //Recibe daño de monstruo
    public boolean recibirDano(int cantidad) {
        if (this.vida - cantidad <= 0) {
            this.vida = 0;
            this.muerto = true;
        } else {
            this.vida -= cantidad;
        }
        return this.muerto;
    }

    //Usa habilidad especial
    public void usarHabilidadEspecial(Monstruo enemigo) {
        switch (tipoValiente) {
            case GUERRERO -> {
                //Golpe fuerte daño extra
                int danoExtra = Math.round(fuerza * DANO_HAB_GUERRERO);
                System.out.println("-"+tipoValiente + " utilizó Carga Asesina!");
                atacar(enemigo, danoExtra);
            }
            case PALADIN -> {
                //Golpe flojo y aumenta defensa
                int danoExtra = Math.round(fuerza * (DANO_HAB_PALADIN - 1));
                this.defensa += Math.round(defensa * AUMENTO_DEFENSA_PALADIN);
                this.buff = true;
                System.out.println("-"+tipoValiente + " utilizó Armadura Sacra, defensa aumentada 40%!");
                atacar(enemigo, danoExtra);
            }
            case MAGO -> {
                //Golpe flojo y baja ataque
                int danoExtra = Math.round(fuerza * (DANO_HAB_MAGO - 1));
                int ataqueReducido = Math.round(enemigo.getFuerza() * (REDUCCION_ATAQUE_MAGO - 1));
                enemigo.setFuerza(ataqueReducido);
                System.out.println("-"+tipoValiente + " utilizó Bola de Escarcha, ataque de " + enemigo.getTipoMonstruo() + " reducido 30%!");
                atacar(enemigo, danoExtra);
            }
            case PICARO -> {
                //Golpe flojo y aplica veneno
                int danoExtra = Math.round(fuerza * (DANO_HAB_PICARO - 1));
                boolean envenenar = !enemigo.getEnvenenado();
                if (envenenar) {
                    enemigo.cambiarEstadoVeneno(true);
                    System.out.println("-"+tipoValiente + " utilizó Colmillo Podrido, " + enemigo.getTipoMonstruo() + " envenenado! (6 hp/turno)");
                } else {
                    System.out.println("-"+tipoValiente + " utilizó Colmillo Podrido, " + enemigo.getTipoMonstruo() + " ya estaba envenenado..");
                }
                atacar(enemigo, danoExtra);
            }
        }
    }

    //Si recibe true cambia activa buff e inicia contador de 3 turnos
    public void activarBuff(boolean buff) {
        if (buff) {
            this.buff = buff;
            contadorBuff = 3;
        } else {
            this.buff = false;
        }
    }

    //Sube nivel y stats
    public void subirNivel() {
        System.out.printf("-Nivel " + nivel + " +1\t\t\t-Vida " + vida + " +10\n-Fuerza " + fuerza + " +1" +
                "\t\t-Defensa " + defensa + " +1\n-Habilidad " + habilidad + " +1\t-Velocidad " + velocidad + " +1\n\n");
        this.nivel++;
        this.vidaMaxima += 10;
        this.vida = Math.min(vidaMaxima, this.vida + 10);
        this.fuerza++;
        this.defensa++;
        this.habilidad++;
        this.velocidad++;
        System.out.println(this.toString());
    }
}
