package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import vos.Plan;
import vos.Dispositivo;
import vos.Orden;


public class DAOTablaOrdenes {


	public static final int BUSQUEDA_POR_ID = 1;
	public static final int BUSQUEDA_POR_CLIENTE = 2;
	public static final int BUSQUEDA_POR_CLIENTE_ACTIVAS = 3;
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaOrdenes() {
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



	public List<Orden> darOrdenesPor(int filtro, String parametro) throws SQLException, Exception {
		List<Orden> ordenes = new ArrayList<Orden>();
		String sql = "SELECT * FROM ORDENES ORD, PLANES PLA "
				+ "WHERE ID_PLAN = PLA.ID";

		switch(filtro) {

		case BUSQUEDA_POR_ID:
			sql += " AND ORD.ID = " + parametro + " AND ROWNUM <= 1";
			break;	
		
		case BUSQUEDA_POR_CLIENTE_ACTIVAS:
			sql += " AND ESTADO < 2"; 
			
		case BUSQUEDA_POR_CLIENTE:
			sql += " AND ID_CLIENTE=" + parametro;
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
			Orden act = new Orden();
			act.setId(rs.getLong("ID"));
			act.setFecha(rs.getDate("FECHA"));
			act.setTipo(rs.getInt("TIPO"));
			act.setEstado(rs.getInt("ESTADO"));
			Plan pl = new Plan();
			pl.setId(rs.getLong("ID_PLAN"));
			pl.setDescripcion(rs.getString("DESCRIPCION"));
			pl.setCantidadCanales(rs.getInt("CANT_CANALES"));
			pl.setVelocidadInternet(rs.getDouble("VEL_INTERNET"));
			pl.setNombre(rs.getString("NOMBRE"));
			act.setPlan(pl);
			ordenes.add(act);
		}
		return ordenes;
	}





	public void crearOrden(Orden orden, Long idCliente) throws SQLException, Exception{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sql = String.format("INSERT INTO ORDENES(ID, FECHA, TIPO, ID_CLIENTE, ID_PLAN) VALUES (%1$s, TO_DATE('%2$s', 'yyyy-mm-dd'), %3$s, %4$s, %5$s)",
				orden.getId(),
				df.format(orden.getFecha()),
				orden.getTipo(),
				idCliente,
				1L);
		System.out.println(sql);
		System.out.println("paso 1");
		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println("paso 2");
		recursos.add(st);
		System.out.println("paso 3");
		st.executeQuery();
		System.out.println("paso 4");
	}
	
	
	public void asignarTecnico(Long idOrden, Long idTecnico, Long idUsuarioFTP) throws SQLException, Exception {
		String sql = String.format("INSERT INTO TEC_ORD(ID_ORDEN, ID_TECNICO, ID_USER_FTP) VALUES (%1$s, %2$s, %3$s)",
				idOrden,
				idTecnico,
				idUsuarioFTP);
		System.out.println(sql);
		System.out.println("paso 1");
		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println("paso 2");
		recursos.add(st);
		System.out.println("paso 3");
		st.executeQuery();
		System.out.println("paso 4");
	}



}
