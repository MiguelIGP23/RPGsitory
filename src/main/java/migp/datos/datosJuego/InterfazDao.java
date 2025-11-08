package migp.datos.datosJuego;

import java.sql.SQLException;
import java.util.List;

public interface InterfazDao<T, K> {

    List<T> listar();
    T buscarPorTipo(K id);      //valiente,monstruo -> tipo    equipable -> nombre

    static void mostrarErrores(SQLException e){
        System.out.println("ERROR: "+e.getMessage()+"\nERROR_CODE: "+e.getErrorCode()+"\nSQL_STATE: "+e.getSQLState());
    }
}
