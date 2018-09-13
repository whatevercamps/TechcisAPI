package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Cliente;
import vos.Nodo;
import vos.Dispositivo;


public class DAOTablaNodos {
	

	public static final int BUSQUEDA_POR_ID = 1;
	public static final int BUSQUEDA_POR_OCTETOS = 2;
	public static final int BUSQUEDA_POR_NOMBRE = 3;
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaNodos() {
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



	public List<Nodo> darNodosPor(int filtro, String parametro) throws SQLException, Exception {
		List<Nodo> nodos = new ArrayList<Nodo>();
		String sql = "SELECT * FROM NODOS" ;

		switch(filtro) {

		case BUSQUEDA_POR_ID:
			sql += " WHERE ID = " + parametro + " AND ROWNUM <=1";
			break;
		case BUSQUEDA_POR_OCTETOS:
			String[] octSp = parametro.split(":");
			sql += " WHERE OCTETO1 = " + octSp[0] + " AND OCTETO2 = ";
			sql +=  octSp[1] + " AND OCTETO3 = " + octSp[2] + "AND ROWNUM <=1";
			break;
			
		case BUSQUEDA_POR_NOMBRE:
			sql += " WHERE NOMBRE = " + parametro + " AND ROWNUM <= 1";
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
			Nodo act = new Nodo();
			act.setId(rs.getLong("ID"));
			act.setNombre(rs.getString("NOMBRE"));
			act.setOcteto1(rs.getInt("OCTETO1"));
			act.setOcteto2(rs.getInt("OCTETO2"));
			act.setOcteto3(rs.getInt("OCTETO3"));
			
			nodos.add(act);
		}
		return nodos;
	}


	public Integer buscarOcteto4Disponible(Long idNodo) throws SQLException, Exception {
		
		String sql = "SELECT OCT4 FROM OCTETOS4 OC LEFT OUTER JOIN (SELECT * FROM CLIENTES WHERE IDNODO = "+ 
		idNodo + ") CL "
				+ "ON OC.OCT4 = CL.OCTETO4 "
				+ "WHERE CEDULA IS NULL AND ROWNUM <=1";
		
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println("buscarOcteto4Disponible: " + sql);
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			return rs.getInt("OCT4");
		}
		
		return null;
	}
	
	public void crearNodo(Nodo nodo) throws SQLException, Exception {
		String sql = String.format("INSERT INTO NODOS(ID, NOMBRE, OCTETO1, OCTETO2, OCTETO3) VALUES (NOD_SEQUENCE.NEXTVAL, '%1$s', '%2$s', '%3$s', '%4$s')",
														nodo.getNombre(),
														nodo.getOcteto1(),
														nodo.getOcteto2(),
														nodo.getOcteto3());
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
