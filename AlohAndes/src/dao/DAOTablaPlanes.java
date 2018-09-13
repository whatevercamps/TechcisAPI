package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Plan;
import vos.Dispositivo;


public class DAOTablaPlanes {


	public static final int BUSQUEDA_POR_ID = 1;
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaPlanes() {
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



	public List<Plan> darPlanesPor(int filtro, String parametro) throws SQLException, Exception {
		List<Plan> planes = new ArrayList<Plan>();
		String sql = "SELECT * FROM PlANES" ;

		switch(filtro) {

		case BUSQUEDA_POR_ID:
			sql += " WHERE ID = " + parametro + " AND ROWNUM <= 1";
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
			Plan act = new Plan();
			act.setId(rs.getLong("ID"));
			act.setNombre(rs.getString("NOMBRE"));
			act.setDescripcion(rs.getString("DESCRIPCION"));
			act.setVelocidadInternet(rs.getDouble("VEL_INTERNET"));
			act.setCantidadCanales(rs.getInt("CANT_CANALES"));


			planes.add(act);
		}
		return planes;
	}


	public void crearPlan(Plan plan) throws SQLException, Exception{
		String sql = String.format("INSERT INTO PLANES(ID, NOMBRE, DESCRIPCION, VEL_INTERNET, CANT_CANALES) VALUES (PLA_SEQUENCE.NEXTVAL, '%1$s', '%2$s', %3$s, %4$s)",
				plan.getNombre(),
				plan.getDescripcion(),
				plan.getVelocidadInternet(),
				plan.getCantidadCanales());
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
