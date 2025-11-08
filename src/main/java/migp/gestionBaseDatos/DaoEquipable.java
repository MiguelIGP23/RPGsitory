package migp.gestionBaseDatos;

import migp.datos.Equipable;
import migp.enums.TiposEquipable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoEquipable implements InterfazDao<Equipable, String> {

    Connection con = ConexionBaseDatos.getInstance().getConnection();

    @Override
    public List<Equipable> listar() {
        ArrayList<Equipable> lista = new ArrayList<>();
        String sql = "select nombre, tipoEquipable, poder from equipables";
        try (PreparedStatement st = con.prepareStatement(sql);) {
            try (ResultSet rs = st.executeQuery();) {
                while (rs.next()) {
                    lista.add(new Equipable(
                            rs.getString(1),
                            TiposEquipable.valueOf(rs.getString(2)),
                            rs.getInt(3)
                    ));
                }
            }
        } catch (SQLException e) {
            InterfazDao.mostrarErrores(e);
        }
        return lista;
    }

    @Override
    public Equipable buscarPorTipo(String id) {
        Equipable equipable = null;
        String sql = "select nombre, tipoEquipable, poder from equipables where nombre=?";
        try (PreparedStatement st = con.prepareStatement(sql);) {
            st.setString(1, id);
            try (ResultSet rs = st.executeQuery();) {
                while (rs.next()) {
                    equipable = new Equipable(
                            rs.getString(1),
                            TiposEquipable.valueOf(rs.getString(2)),
                            rs.getInt(3)
                    );
                }
            }
        } catch (SQLException e) {
            InterfazDao.mostrarErrores(e);
        }
        return equipable;
    }
}
