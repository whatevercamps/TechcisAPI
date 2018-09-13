package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Plan;
import vos.UsuarioFTP;
import vos.Dispositivo;
import vos.Empleado;


public class DAOTablaEmpleados {


	public static final int BUSQUEDA_POR_ID = 1;
	public static final int TEC_DE_ORDEN = 2;
	public static final int DISPONIBLE_EN_TAL_DIA = 3;
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaEmpleados() {
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



	public List<Empleado> darEmpleadosPor(int filtro, String parametro) throws SQLException, Exception {
		List<Empleado> empleados = new ArrayList<Empleado>();
		String sql = "SELECT EMP.*, ID_USER_FTP, USUARIO, CLAVE FROM EMPLEADOS EMP LEFT OUTER JOIN (TEC_ORD JOIN USUARIOS_FTP UFTP ON ID_USER_FTP = UFTP.ID) ON EMP.ID = ID_TECNICO WHERE 1 = 1" ;

		switch(filtro) {

		case BUSQUEDA_POR_ID:
			sql += " AND EMP.ID = " + parametro + " AND ROWNUM <= 1";
			break;

		case TEC_DE_ORDEN:
			sql += " AND ID_ORDEN = " + parametro; 
			break;
			
		case DISPONIBLE_EN_TAL_DIA:
			sql += " AND EMP.ID NOT IN "
					+ "(SELECT EMP.ID FROM EMPLEADOS EMP, ORDENES ORD, TEC_ORD, USUARIOS_FTP UF "
					+ "WHERE ORD.ID = ID_ORDEN AND ID_TECNICO = EMP.ID AND ID_USER_FTP = UF.ID "
					+ "AND FECHA = TO_DATE('"+parametro+"', 'yyyy-mm-dd'))";
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
			Empleado act = new Empleado();
			act.setId(rs.getLong("ID"));
			act.setCedula(rs.getLong("CEDULA"));
			act.setCumpleanios(rs.getDate("CUMPLEANIOS"));
			act.setDireccion(rs.getString("DIRECCION"));
			act.setEmail(rs.getString("EMAIL"));
			act.setEps(rs.getString("EPS"));
			int es = rs.getInt("ESTADO");
			act.setEstado(es == 0? "Activo" : "Inactivo");
			act.setNombre(rs.getString("NOMBRE"));
			act.setTelefono(rs.getLong("TELEFONO"));
			act.setTipo(rs.getInt("TIPO"));
			act.setUsuarioFTP(new UsuarioFTP(rs.getLong("ID_USER_FTP"), rs.getString("USUARIO"), rs.getString("CLAVE")));
			empleados.add(act);
		}
		return empleados;
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
