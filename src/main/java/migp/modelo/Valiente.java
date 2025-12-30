package migp.modelo;

import migp.modelo.enums.TiposValiente;
import migp.persistencia.DaoEquipable;

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
    private Inventario inventario;
    private Equipable arma;
    private Equipable escudo;
    private int nivel;

    private boolean muerto;
    private boolean buff;
    private int contadorBuff;

    private List<Monstruo> victorias;       //Guarda historial de monstruos derrotados

    DaoEquipable daoEquipable = new DaoEquipable();

    public Valiente(TiposValiente tipoValiente, int vida, int fuerza, int defensa, int habilidad, int velocidad, Equipable arma, Equipable escudo, int nivel) {
        this.tipoValiente = tipoValiente;
        this.vida = vida;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.habilidad = habilidad;
        this.velocidad = velocidad;
        this.inventario = new Inventario();
        this.arma = arma;
        this.escudo = escudo;
        this.nivel = nivel;

        this.muerto = false;
        this.buff = false;
        this.contadorBuff = 0;

        this.victorias = new ArrayList<>();
    }


    //Metodos get
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
                ", Arma: " + arma.getNombre() +
                ", Escudo: " + escudo.getNombre()
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
        boolean enInventario = false;
        for (InventarioItem arma : listaArmas) {
            if (armaNueva.getNombre().equalsIgnoreCase(arma.getEquipable().getNombre())) {
                enInventario = true;
                armaInventario = arma.getEquipable();
                break;
            }
        }
        if (enInventario) {
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
        boolean enInventario = false;
        for (InventarioItem escudo : listaEscudos) {
            if (escudoNuevo.getNombre().equalsIgnoreCase(escudo.getEquipable().getNombre())) {
                enInventario = true;
                escudoInventario = escudo.getEquipable();
                break;
            }
        }
        if (enInventario) {
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
    public void usarConsumible(Equipable usado, int cantidad) {
        if (usado == null || cantidad <= 0) return;
        List<InventarioItem> listaConsumibles = inventario.getConsumibles();
        for (InventarioItem consumible : listaConsumibles) {
            if (usado.getNombre().equalsIgnoreCase(consumible.getEquipable().getNombre()) && cantidad <= consumible.getCantidad()) {
                this.vida += (consumible.getEquipable().getPoder() * cantidad);
                inventario.eliminarItem(consumible.getEquipable(), cantidad);
                return;
            }
        }
    }

    // Agrega un objeto al inventario
    public void nuevoObjeto(Equipable nuevo, int cantidad) {
        inventario.agregarItem(nuevo, cantidad);
    }

    // Elimina un objeto del inventario y lo desequipa si estuviera equipado
    public void eliminarObjeto(Equipable eliminado, int cantidad) {
        if (eliminado == null || cantidad <= 0) return;
        boolean enInventario= inventario.getItems().stream().anyMatch(item -> item.getEquipable().getNombre().equalsIgnoreCase(eliminado.getNombre()));
        if(!enInventario) return;
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
                int danoExtra = Math.round(fuerza * (DANO_HAB_PALADIN - 1));
                System.out.println(tipoValiente + " utilizó Armadura Sacra!");
                atacar(enemigo, danoExtra);
                this.defensa += Math.round(defensa * AUMENTO_DEFENSA_PALADIN);
                this.buff = true;
                System.out.println("Defensa de " + tipoValiente + " aumentada 40%");
            }
            case MAGO -> {
                //Golpe flojo y baja ataque
                int danoExtra = Math.round(fuerza * (DANO_HAB_MAGO - 1));
                System.out.println(tipoValiente + " utilizo Bola de Escarcha!");
                atacar(enemigo, danoExtra);
                int ataqueReducido = Math.round(enemigo.getFuerza() * (REDUCCION_ATAQUE_MAGO - 1));
                enemigo.setFuerza(ataqueReducido);
                System.out.println("Ataque " + enemigo + " reducido 30%\n");
            }
            case PICARO -> {
                //Golpe flojo y aplica veneno
                int danoExtra = Math.round(fuerza * (DANO_HAB_PICARO - 1));
                System.out.println(tipoValiente + " utilizó Colmillo Podrido!");
                atacar(enemigo, danoExtra);
                if (!enemigo.getEnvenenado()) {
                    enemigo.cambiarEstadoVeneno(true);
                    System.out.println(enemigo.getTipoMonstruo() + " envenenado! (6vida/turno)");
                }
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
        this.vida += 10;
        this.fuerza++;
        this.defensa++;
        this.habilidad++;
        this.velocidad++;
        System.out.println(this.toString());
    }
}
