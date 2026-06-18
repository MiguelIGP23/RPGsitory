package migp.logica;

import migp.modelo.Equipable;
import migp.modelo.Monstruo;
import migp.modelo.enums.TiposEquipable;

//Representa una casilla del mapa con su estado visible y su contenido
public class Casilla {

    private boolean revelada;
    private boolean visitada;
    private Monstruo monstruo;
    private Equipable objeto;
    private boolean monstruoPendiente;
    private TiposEquipable tipoObjetoPendiente;
    private boolean jefeFinal;

    public Casilla() {
        this.revelada = false;
        this.visitada = false;
        this.monstruo = null;
        this.objeto = null;
        this.monstruoPendiente = false;
        this.tipoObjetoPendiente = null;
        this.jefeFinal = false;
    }

    public boolean getRevelada() {
        return revelada;
    }

    public boolean getVisitada() {
        return visitada;
    }

    public Monstruo getMonstruo() {
        return monstruo;
    }

    public Equipable getObjeto() {
        return objeto;
    }

    public boolean getJefeFinal() {
        return jefeFinal;
    }

    public boolean getMonstruoPendiente() {
        return monstruoPendiente;
    }

    public TiposEquipable getTipoObjetoPendiente() {
        return tipoObjetoPendiente;
    }

    public void revelar() {
        this.revelada = true;
    }

    public void marcarVisitada() {
        this.visitada = true;
        this.revelada = true;
    }

    public void setMonstruo(Monstruo monstruo) {
        this.monstruo = monstruo;
        this.monstruoPendiente = false;
    }

    public void eliminarMonstruo() {
        this.monstruo = null;
        this.monstruoPendiente = false;
        this.jefeFinal = false;
    }

    public void setObjeto(Equipable objeto) {
        this.objeto = objeto;
        this.tipoObjetoPendiente = null;
    }

    public void eliminarObjeto() {
        this.objeto = null;
        this.tipoObjetoPendiente = null;
    }

    public void marcarComoJefeFinal() {
        this.jefeFinal = true;
    }

    public void reservarMonstruo() {
        this.monstruoPendiente = true;
    }

    public void reservarObjeto(TiposEquipable tipoObjeto) {
        this.tipoObjetoPendiente = tipoObjeto;
    }

    public boolean tieneMonstruo() {
        return monstruo != null || monstruoPendiente;
    }

    public boolean tieneObjeto() {
        return objeto != null || tipoObjetoPendiente != null;
    }

    public boolean estaVacia() {
        return monstruo == null && objeto == null && !monstruoPendiente && tipoObjetoPendiente == null;
    }
}
