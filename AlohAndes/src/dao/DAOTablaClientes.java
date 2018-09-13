package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Cliente;
import vos.Plan;
import vos.Dispositivo;


public class DAOTablaClientes {

	public final static String USUARIO = "PARRANDEROS";
	public static final int BUSQUEDA_POR_CEDULA = 1;
	public static final int BUSQUEDA_POR_ID_NODO = 2;
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaClientes() {
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



	public List<Cliente> darClientesPor(int filtro, String parametro) throws SQLException, Exception {
		List<Cliente> clientes = new ArrayList<Cliente>();
		String sql = "SELECT CLIENTES.*, OCTETO1, OCTETO2, OCTETO3, NODOS.NOMBRE NOD_NOM FROM CLIENTES, NODOS WHERE CLIENTES.IDNODO = NODOS.ID" ;

		switch(filtro) {

		case BUSQUEDA_POR_CEDULA:
			sql += " AND CEDULA = " + parametro + " AND ROWNUM <= 1";
			break;
		default:
			break;
		}


		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println(conn.isValid(1000));
		recursos.add(st);
		System.out.println("Filtro: " + filtro + ", paramatro: " + parametro);
		System.out.println(sql);
		ResultSet rs = st.executeQuery();

		while(rs.next()) {
			System.out.println("si entró");
			Cliente act = new Cliente();
			if(rs.getInt("ESTADO") == 0)
				act.desactivarEstado();
			else
				act.activarEstado();
			act.setCedula(rs.getLong("CEDULA"));
			act.setDireccion(rs.getString("DIRECCION"));
			act.setEmail(rs.getString("EMAIL"));
			act.setNombre(rs.getString("NOMBRE"));
			act.setOcteto4(rs.getInt("OCTETO4"));
			act.setTelefono(rs.getLong("TELEFONO"));
			act.setOcteto1(rs.getInt("OCTETO1"));
			act.setOcteto2(rs.getInt("OCTETO2"));
			act.setOcteto3(rs.getInt("OCTETO3"));
			Plan plaTemp = new Plan();
			plaTemp.setId(rs.getLong("IDPLAN"));
			act.setPlan(plaTemp);
			act.setNombreNodo(rs.getString("NOD_NOM"));
			clientes.add(act);

		}
		return clientes;
	}

	public void crearCliente(Cliente cliente, Long idNodo) throws SQLException, Exception {
		String sql = String.format("INSERT INTO CLIENTES(CEDULA, NOMBRE, DIRECCION, EMAIL, OCTETO4, TELEFONO, IDNODO, IDPLAN) VALUES "
				+ "(%1$s, '%2$s', '%3$s', '%4$s', %5$s,%6$s, %7$s, %8$s)",
				cliente.getCedula(),
				cliente.getNombre(),
				cliente.getDireccion(),
				cliente.getEmail(),
				cliente.getOcteto4(),
				cliente.getTelefono(),
				idNodo,
				cliente.getPlan().getId());
		System.out.println(sql);
		System.out.println("paso 1");
		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println("paso 2");
		recursos.add(st);
		System.out.println("paso 3");
		st.executeQuery();
		System.out.println("paso 4");
	}


	public Boolean borrarCliente(Long idCliente) throws SQLException, Exception {
		String sql = "DELETE FROM CLIENTES WHERE CEDULA = " + idCliente;
		System.out.println(sql);
		System.out.println("paso 1");
		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println("paso 2");
		recursos.add(st);
		System.out.println("paso 3");
		st.executeQuery();
		System.out.println("paso 4");
		return true;
	}


	public void modificarCliente(Long idCliente, Cliente nuevo, Long idNodo)throws SQLException, Exception {
		String sql = String.format("UPDATE CLIENTES SET NOMBRE = '%1$s', DIRECCION = '%2$s', EMAIL = '%3$s', OCTETO4 = %4$s, TELEFONO = %5$s, IDNODO = %6$s, IDPLAN = %7$s, ESTADO = %8$s WHERE CEDULA = %9$s",
							nuevo.getNombre(), 
							nuevo.getDireccion(),
							nuevo.getEmail(),
							nuevo.getOcteto4(),
							nuevo.getTelefono(),
							idNodo,
							nuevo.getPlan().getId(),
							(nuevo.getEstado().equalsIgnoreCase("Activo") ? 1 : 0),
							idCliente);
							
							
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
