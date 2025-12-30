package migp.modelo;

//Para el inventario vamos a implementar 2 clases, Inventario() que representa una lista con todos los objetos del valiente,
//y InventarioItem() que representa cada uno de los objetos del inventario con sus atributos.


import migp.modelo.enums.TiposEquipable;

import java.util.ArrayList;
import java.util.List;

//Esta clase implementa la lista de objetos guardados en el inventario y los métodos para su gestión
public class Inventario {

    private ArrayList<InventarioItem> listaObjetos;

    public Inventario() {
        this.listaObjetos = new ArrayList<>();
    }


    // Añadir objeto (si ya existe, suma cantidad)
    public void agregarItem(Equipable equipable, int cantidad) {
        for (InventarioItem item : listaObjetos) {
            if (item.getEquipable().getNombre().equalsIgnoreCase(equipable.getNombre())) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        listaObjetos.add(new InventarioItem(equipable, cantidad));
    }

    // Quita 1 unidad de objeto, si llega a 0 lo elimina, si está equipado lo desequipa
    public int eliminarItem(Equipable equipable, int cantidad){
        int nuevaCantidad=0;
        for (int i = 0; i < listaObjetos.size(); i++) {
            InventarioItem item = listaObjetos.get(i);
            if (item.getEquipable().getNombre().equalsIgnoreCase(equipable.getNombre())) {
                nuevaCantidad = item.getCantidad() - cantidad;
                if (nuevaCantidad <= 0) {
                    listaObjetos.remove(i);
                } else {
                    item.setCantidad(nuevaCantidad);
                }
                break;
            }
        }
        return nuevaCantidad;
    }


    // Devuelve lista de objetos
    public List<InventarioItem> getItems() {
        return listaObjetos;
    }

    // Devolver solo armas
    public List<InventarioItem> getArmas() {
        List<InventarioItem> armas = new ArrayList<>();
        for (InventarioItem item : listaObjetos) {
            if (item.getEquipable().getTipo() == TiposEquipable.ARMA) {
                armas.add(item);
            }
        }
        return armas;
    }

    // Devolver solo escudos
    public List<InventarioItem> getEscudos() {
        List<InventarioItem> escudos = new ArrayList<>();
        for (InventarioItem item : listaObjetos) {
            if (item.getEquipable().getTipo() == TiposEquipable.ESCUDO) {
                escudos.add(item);
            }
        }
        return escudos;
    }

    // Devolver solo consumibles
    public List<InventarioItem> getConsumibles() {
        List<InventarioItem> consumibles = new ArrayList<>();
        for (InventarioItem item : listaObjetos) {
            if (item.getEquipable().getTipo() == TiposEquipable.CONSUMIBLE) {
                consumibles.add(item);
            }
        }
        return consumibles;
    }
}

