package migp;


import migp.gestionBaseDatos.ConexionBaseDatos;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {

//        System.out.println("WD  = " + new java.io.File(".").getAbsolutePath());
//        System.out.println("CL  = " + System.getProperty("java.class.path"));
//
//        // Ver drivers disponibles
//        var it = DriverManager.getDrivers();
//        while (it.hasMoreElements()) System.out.println("Driver: " + it.nextElement());
//
//        // Driver y BD
//        Class.forName("org.sqlite.JDBC");
//        try (Connection con = DriverManager.getConnection("jdbc:sqlite:src/main/resources/rpg.db");
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM monstruos")) {
//            rs.next();
//            System.out.println("Monstruos = " + rs.getInt(1));
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        Connection con = ConexionBaseDatos.getInstance().getConnection();
    }
}