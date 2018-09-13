package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Dispositivo;
import vos.TipoDispositivo;


public class DAOTablaDispositivos {


	public static final int BUSQUEDA_POR_ID = 1;
	public static final int BUSQUEDA_POR_MACS = 2;
	public static final int BUSQUEDA_POR_CLIENTE = 3;
	public static final int BUSQUEDA_TIPO_POR_DISP = 4;
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaDispositivos() {
		recursos = new ArrayList<Object>();
	}


	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	public void setConn(Connection conn) throws SQLException {
		this.conn = conn;
	}



	public List<Dispositivo> darDispositivosPor(int filtro, String parametro) throws SQLException, Exception {
		List<Dispositivo> routers = new ArrayList<Dispositivo>();
		String sql = "SELECT DI.*, TIP.ID TIPO_ID, TIP.NOMBRE FROM DISPOSITIVOS DI, TIPOS_DISPOSITIVOS TIP WHERE DI.TIPO = TIP.ID" ;

		switch(filtro) {

		case BUSQUEDA_POR_ID:
			sql += " AND ID = " + parametro + " AND ROWNUM <= 1";
			break;

		case BUSQUEDA_POR_MACS:
			String[] paraSp = parametro.split(":");
			sql+= " AND MAC1_1 = '" + paraSp[0] +
					"' AND MAC1_2 = '" + paraSp[1] +
					"' AND MAC2_1 = '" + paraSp[2] +
					"' AND MAC2_2 = '" + paraSp[3] +
					"' AND MAC3_1 = '" + paraSp[4] +
					"' AND MAC3_2 = '" + paraSp[5] +
					"' AND MAC4_1 = '" + paraSp[6] +
					"' AND MAC4_2 = '" + paraSp[7] + "'";
			break;
		case BUSQUEDA_POR_CLIENTE:
			sql +=" AND CLIENTE = " + parametro;
			break;
		default:
			break;
		}


		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println("Filtro: " + filtro + ", paramatro: " + parametro);
		System.out.println(sql);
		ResultSet rs = st.executeQuery();

		while(rs.next()) {
			Dispositivo act = new Dispositivo();
			act.setId(rs.getLong("ID"));
			act.setDescripcion(rs.getString("DESCRIPCION"));
			act.setMac1_1(rs.getString("MAC1_1"));
			act.setMac1_2(rs.getString("MAC1_2"));
			act.setMac2_1(rs.getString("MAC2_1"));
			act.setMac2_2(rs.getString("MAC2_2"));
			act.setMac3_1(rs.getString("MAC3_1"));
			act.setMac3_2(rs.getString("MAC3_2"));
			act.setMac4_1(rs.getString("MAC4_1"));
			act.setMac4_2(rs.getString("MAC4_2"));
			
			TipoDispositivo tp = new TipoDispositivo(rs.getLong("TIPO_ID"), rs.getString("NOMBRE"));

			act.setTipo(tp);
			routers.add(act);
		}
		return routers;
	}


	public void crearDispositivo(Dispositivo disp) throws SQLException, Exception{
		String sql = String.format("INSERT INTO DISPOSITIVOS(id, descripcion, mac1_1, mac1_2, mac2_1, mac2_2, mac3_1, mac3_2, mac4_1, mac4_2, tipo) "
				+ "VALUES (ROU_SEQUENCE.NEXTVAL, '%1$s', '%2$s', '%3$s', '%4$s', '%5$s','%6$s', '%7$s', '%8$s', '%9$s', '%10$s')",
				disp.getDescripcion(),
				disp.getMac1_1(),
				disp.getMac1_2(),
				disp.getMac2_1(),
				disp.getMac2_2(),
				disp.getMac3_1(),
				disp.getMac3_2(),
				disp.getMac4_1(),
				disp.getMac4_2(),
				disp.getTipo().getId());
		System.out.println(sql);
		System.out.println("paso 1");
		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println("paso 2");
		recursos.add(st);
		System.out.println("paso 3");
		st.executeQuery();
		System.out.println("paso 4");
	}

	public void trasladarDispositivo(Long anteriorClienteId, Long nuevoClienteId) throws SQLException, Exception{
		String sql = "UPDATE DISPOSITIVOS SET CLIENTE = " + nuevoClienteId +" WHERE CLIENTE = " + anteriorClienteId;
		System.out.println(sql);
		System.out.println("paso 1");
		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println("paso 2");
		recursos.add(st);
		System.out.println("paso 3");
		st.executeQuery();
		System.out.println("paso 4");

	}
	
	public List<TipoDispositivo> darTiposDispositivoPor(int filtro, String parametro)throws SQLException, Exception{
		List<TipoDispositivo> ls = new ArrayList<TipoDispositivo>();
		String sql = "SELECT * FROM TIPOS_DISPOSITIVOS WHERE 1 = 1";
		
		
		switch(filtro) {

		case BUSQUEDA_POR_ID:
			sql += " AND ID = " + parametro + " AND ROWNUM <= 1";
			break;

		default:
			break;
		}
		
		
		
		System.out.println(sql);
		System.out.println("paso 1");
		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println("paso 2");
		recursos.add(st);
		System.out.println("paso 3");
		ResultSet rs = st.executeQuery();
		System.out.println("paso 4");
		while(rs.next()) {
			TipoDispositivo tp = new TipoDispositivo(rs.getLong("ID"), rs.getString("NOMBRE"));
			ls.add(tp);
		}
		
		return ls;

	}

}
