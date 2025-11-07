package migp.gestionBaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatos {

    private Connection connection;
    private static ConexionBaseDatos instance;

    private ConexionBaseDatos() {
        try {
            String db = "rpg.db";
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/"+db);
            System.out.println("Conectado a BD: "+db);
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage() + "\nERROR_CODE " + e.getErrorCode() + "\nSQL_STATE " + e.getSQLState());
        }
    }

    public static synchronized ConexionBaseDatos getInstance(){
        if (instance==null){
            instance=new ConexionBaseDatos();
        }
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }
}
