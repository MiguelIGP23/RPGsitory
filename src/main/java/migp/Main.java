package migp;


import migp.gestionBaseDatos.ConexionBaseDatos;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {

        Connection con = ConexionBaseDatos.getInstance().getConnection();
    }
}