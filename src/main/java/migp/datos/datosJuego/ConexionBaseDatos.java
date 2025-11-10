package migp.datos.datosJuego;

import migp.datos.persistencia.TempDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatos {

    private Connection connection;
    private static ConexionBaseDatos instance;

    private ConexionBaseDatos() {
        try {
            String db = "rpg.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + TempDatabase.getTempDBPath());
            System.out.println("Conectado a BD: " + db + "\n\n");
        } catch (SQLException e) {
            InterfazDao.mostrarErrores(e);
        }
    }

    public static synchronized ConexionBaseDatos getInstance() {
        if (instance == null) {
            instance = new ConexionBaseDatos();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void cerrar() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            InterfazDao.mostrarErrores(e);
        }
    }
}
