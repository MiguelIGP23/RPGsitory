package migp.modelo;

//Para el inventario vamos a implementar 2 clases, Inventario() que representa una lista con todos los objetos del valiente,
//y InventarioItem() que representa cada uno de los objetos del inventario con sus atributos.


//Esta clase representa cada uno de los objetos del inventario
//Contiene el objeto, la cantidad y un booleano que indica si está equipado
public class InventarioItem {

    private Equipable equipable;
    private int cantidad;

    public InventarioItem(Equipable equipable, int cantidad){
        if (equipable == null) {
            throw new IllegalArgumentException("El equipable no puede ser null");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
        }
        this.equipable=equipable;
        this.cantidad=cantidad;
    }

    public Equipable getEquipable() {
        return equipable;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
        }
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return equipable.getNombre() + ": "+ cantidad;
    }
}
