package migp.datos.datosJuego;

import migp.modelo.Equipable;
import migp.modelo.Valiente;
import migp.modelo.enums.TiposValiente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoValiente implements InterfazDao<Valiente, String> {

    Connection con = ConexionBaseDatos.getInstance().getConnection();

    @Override
    public List<Valiente> listar() {
        ArrayList<Valiente> lista =new ArrayList<>();
        for(TiposValiente tipo:TiposValiente.values()){
            lista.add(buscarPorTipo(tipo.name()));
        }
        return lista;
    }

    @Override
    public Valiente buscarPorTipo(String id) {
        Valiente valiente = null;
        String sql="select tipoValiente, vida, fuerza, defensa, habilidad," +
                " velocidad, arma, escudo, nivel from valientes where tipoValiente=?";
        try(PreparedStatement st = con.prepareStatement(sql);){
            st.setString(1,id);
            try (ResultSet rs = st.executeQuery()){
                if(rs.next()){
                    //Cargamos arma y escudo desde la bd por sus nombres
                    String armaNombre = rs.getString(7);
                    String escudoNombre = rs.getString(8);
                    Equipable arma= new DaoEquipable().buscarPorTipo(armaNombre);
                    Equipable escudo= new DaoEquipable().buscarPorTipo(escudoNombre);

                    valiente= new Valiente(
                            TiposValiente.valueOf(rs.getString(1)),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getInt(4),
                            rs.getInt(5),
                            rs.getInt(6),
                            arma,
                            escudo,
                            rs.getInt(9)
                    );
                }
            }
        }catch (SQLException e){
            InterfazDao.mostrarErrores(e);
        }
        return valiente;
    }
}
