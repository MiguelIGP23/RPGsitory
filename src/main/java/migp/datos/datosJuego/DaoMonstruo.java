package migp.datos.datosJuego;

import migp.modelo.Monstruo;
import migp.modelo.enums.TiposMonstruo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoMonstruo implements InterfazDao<Monstruo,String>{

    Connection con = ConexionBaseDatos.getInstance().getConnection();

    @Override
    public List<Monstruo> listar() {
        ArrayList<Monstruo> lista =new ArrayList<>();
        for(TiposMonstruo tipo:TiposMonstruo.values()){
            lista.add(buscarPorTipo(tipo.name()));
        }
        return lista;
    }

    @Override
    public Monstruo buscarPorTipo(String id) {
        Monstruo monstruo = null;
        String sql="select tipo, vida, fuerza, defensa, habilidad, " +
                "velocidad, nivel from monstruos where tipo=?";
        try(PreparedStatement st = con.prepareStatement(sql);){
            st.setString(1,id);
            try (ResultSet rs = st.executeQuery()){
                if(rs.next()){
                    monstruo= new Monstruo(
                            TiposMonstruo.valueOf(rs.getString(1)),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getInt(4),
                            rs.getInt(5),
                            rs.getInt(6),
                            rs.getInt(7)
                    );
                }
            }
        }catch (SQLException e){
            InterfazDao.mostrarErrores(e);
        }
        return monstruo;
    }
}
