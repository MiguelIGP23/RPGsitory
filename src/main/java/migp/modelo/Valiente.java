package migp.modelo;

import migp.logica.Consola;
import migp.modelo.enums.TiposValiente;

import java.util.ArrayList;
import java.util.List;

public class Valiente {

    //Atributos de clase - parámetros para daño de habilidades
    public static final float DANO_HAB_GUERRERO = 1.5f;
    public static final float DANO_HAB_PALADIN = 0.6f;
    public static final float DANO_HAB_MAGO = 0.5f;
    public static final float DANO_HAB_PICARO = 0.5f;

    public static final float AUMENTO_DEFENSA_PALADIN = 0.25f;
    public static final int TURNOS_BUFF_PALADIN = 3;
    public static final float REDUCCION_ATAQUE_MAGO = 0.20f;
    public static final float MITIGACION_DEFENSA = 0.4f;
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
    private int bonusDefensaBuff;

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
        this.bonusDefensaBuff = 0;

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

    public int getNivel() {
        return nivel;
    }

    public boolean getMuerto() {
        return muerto;
    }

    public boolean getBuff() {
        return buff;
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
        return String.format(
                "%-8s: Nivel: %d, Vida: %d, Fuerza: %d, Defensa: %d, Habilidad: %d, Velocidad: %d, Arma: %s, Escudo: %s",
                tipoValiente,
                nivel,
                vida,
                fuerza,
                defensa,
                habilidad,
                velocidad,
                (arma != null ? arma.getNombre() + "-" + arma.getPoder() : "-"),
                (escudo != null ? escudo.getNombre() + "-" + escudo.getPoder() : "-")
        );
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
                curar(consumible.getEquipable().getPoder() * cantidad);
                inventario.eliminarItem(consumible.getEquipable(), cantidad);
                return true;
            }
        }
        return false;
    }

    //Recupera vida sin superar el máximo y devuelve la curación real aplicada
    public int curar(int cantidad) {
        if (cantidad <= 0 || muerto) {
            return 0;
        }
        int vidaAntes = this.vida;
        this.vida = Math.min(vidaMaxima, this.vida + cantidad);
        return this.vida - vidaAntes;
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
        //El daño bruto se mitiga después según la defensa del objetivo
        int ataque = this.fuerza + danoExtra;
        int danoReal = enemigo.recibirGolpe(ataque);
        System.out.println(Consola.color(Consola.ANSI_VERDE, "- " + tipoValiente + " ataca, " + enemigo.getNombreMostrado() + " pierde " + danoReal + " de vida"));
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

    //Recibe un golpe físico aplicando mitigación por defensa
    public int recibirGolpe(int ataqueBruto) {
        int reduccion = Math.round(defensa * MITIGACION_DEFENSA);
        int danoReal = Math.max(1, ataqueBruto - reduccion);
        recibirDano(danoReal);
        return danoReal;
    }

    //Usa habilidad especial
    public void usarHabilidadEspecial(Monstruo enemigo) {
        switch (tipoValiente) {
            case GUERRERO -> {
                //Golpe fuerte daño extra
                int danoExtra = Math.round(fuerza * (DANO_HAB_GUERRERO - 1));
                System.out.println(Consola.color(Consola.ANSI_CYAN, "-" + tipoValiente + " utilizó Carga Asesina!"));
                atacar(enemigo, danoExtra);
            }
            case PALADIN -> {
                //Golpe flojo y aumenta defensa
                int danoExtra = Math.round(fuerza * (DANO_HAB_PALADIN - 1));
                activarBuff(true);
                    System.out.println(Consola.color(Consola.ANSI_CYAN, "-" + tipoValiente + " utilizó Armadura Sacra, defensa aumentada 25%!"));
                atacar(enemigo, danoExtra);
            }
            case MAGO -> {
                //Golpe flojo y baja ataque
                int danoExtra = Math.round(fuerza * (DANO_HAB_MAGO - 1));
                boolean debilitado = enemigo.aplicarDebuffAtaque(REDUCCION_ATAQUE_MAGO);
                if (debilitado) {
                    System.out.println(Consola.color(Consola.ANSI_CYAN, "-" + tipoValiente + " utilizó Bola de Escarcha, ataque de " + enemigo.getNombreMostrado() + " reducido 20%!"));
                } else {
                    System.out.println(Consola.color(Consola.ANSI_ROJO, "-" + tipoValiente + " utilizó Bola de Escarcha, " + enemigo.getNombreMostrado() + " ya estaba debilitado."));
                }
                atacar(enemigo, danoExtra);
            }
            case PICARO -> {
                //Golpe flojo y aplica veneno
                int danoExtra = Math.round(fuerza * (DANO_HAB_PICARO - 1));
                boolean envenenar = !enemigo.getEnvenenado();
                if (envenenar) {
                    enemigo.cambiarEstadoVeneno(true);
                    System.out.println(Consola.color(Consola.ANSI_CYAN, "-" + tipoValiente + " utilizó Colmillo Podrido, " + enemigo.getNombreMostrado() + " envenenado! (5 hp/turno)"));
                } else {
                    System.out.println(Consola.color(Consola.ANSI_ROJO, "-" + tipoValiente + " utilizó Colmillo Podrido, " + enemigo.getNombreMostrado() + " ya estaba envenenado."));
                }
                atacar(enemigo, danoExtra);
            }
        }
    }

    //Comprueba si la habilidad especial puede aplicarse antes de gastar el turno
    public boolean puedeUsarHabilidadEspecial(Monstruo enemigo) {
        switch (tipoValiente) {
            case GUERRERO -> {
                return true;
            }
            case PALADIN -> {
                if (buff) {
                    System.out.println(Consola.color(Consola.ANSI_ROJO, "-" + tipoValiente + " ya tiene Armadura Sacra activa."));
                    return false;
                }
                return true;
            }
            case MAGO -> {
                if (enemigo.getAtaqueDebilitado()) {
                    System.out.println(Consola.color(Consola.ANSI_ROJO, "-" + enemigo.getNombreMostrado() + " ya está debilitado."));
                    return false;
                }
                return true;
            }
            case PICARO -> {
                if (enemigo.getEnvenenado()) {
                    System.out.println(Consola.color(Consola.ANSI_ROJO, "-" + enemigo.getNombreMostrado() + " ya está envenenado."));
                    return false;
                }
                return true;
            }
            default -> {
                return true;
            }
        }
    }

    //Si recibe true cambia activa buff e inicia contador de 3 turnos
    public void activarBuff(boolean buff) {
        if (buff) {
            if (!this.buff) {
                bonusDefensaBuff = Math.max(1, Math.round(defensa * AUMENTO_DEFENSA_PALADIN));
                this.defensa += bonusDefensaBuff;
            }
            this.buff = buff;
            contadorBuff = TURNOS_BUFF_PALADIN;
        } else {
            if (this.buff && bonusDefensaBuff > 0) {
                this.defensa -= bonusDefensaBuff;
            }
            this.buff = false;
            this.contadorBuff = 0;
            this.bonusDefensaBuff = 0;
        }
    }

    //Actualiza la duración del buff al final del turno y lo elimina cuando caduca
    public void actualizarBuff() {
        if (!buff) {
            return;
        }
        contadorBuff--;
        if (contadorBuff <= 0) {
            activarBuff(false);
            System.out.println(Consola.color(Consola.ANSI_AMARILLO, "-" + tipoValiente + " ya no tiene Armadura Sacra activa."));
        }
    }

    //Sube nivel y stats
    public void subirNivel() {
        System.out.printf("%s-Nivel %d +1\t\t\t-Vida %d +8\n-Fuerza %d +1\t\t-Defensa %d +1\n-Habilidad %d +1\t-Velocidad %d +1\n\n%s",
                Consola.ANSI_VERDE, nivel, vida, fuerza, defensa, habilidad, velocidad, Consola.ANSI_RESET);
        this.nivel++;
        this.vidaMaxima += 8;
        this.vida = Math.min(vidaMaxima, this.vida + 8);
        this.fuerza++;
        this.defensa++;
        this.habilidad++;
        this.velocidad++;
        System.out.println(Consola.color(Consola.ANSI_MAGENTA, this.toString()));
    }
}
