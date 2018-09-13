/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: RotondAndes 
 * Autor: David Bauista - dj.bautista10@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package tm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Properties;



import dao.DAOTablaClientes;
import dao.DAOTablaNodos;
import dao.DAOTablaOrdenes;
import dao.DAOTablaPlanes;
import dao.DAOTablaDispositivos;
import dao.DAOTablaEmpleados;
import vos.Cliente;
import vos.Nodo;
import vos.Orden;
import vos.Plan;
import vos.TipoDispositivo;
import vos.Dispositivo;
import vos.Empleado;
import vos.FechaDisponible;

import java.text.SimpleDateFormat;


/**
 * Transaction Manager de la aplicacion (TM)
 * Fachada en patron singleton de la aplicacion
 * @author David Bautista
 */
public class RedetekApiTM {


	/**
	 * Atributo estatico que contiene el path relativo del archivo que tiene los datos de la conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los datos de la conexion
	 */
	private  String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;

	/**
	 * conexion a la base de datos
	 */
	private Connection conn;

	/**
	 * Atributo que dará el último estado correcto de la base de datos.
	 */
	private Savepoint savepoint;

	/**
	 * Metodo constructor de la clase RotondAndesMaster, esta clase modela y contiene cada una de las 
	 * transacciones y la logica de negocios que estas conllevan.
	 * <b>post: </b> Se crea el objeto RotondAndesMaster, se inicializa el path absoluto del archivo de conexion y se
	 * inicializa los atributos que se usan par la conexion a la base de datos.
	 * @param contextPathP - path absoluto en el servidor del contexto del deploy actual
	 */
	public RedetekApiTM(String contextPathP) {
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
	}

	/**
	 * Metodo que  inicializa los atributos que se usan para la conexion a la base de datos.
	 * <b>post: </b> Se han inicializado los atributos que se usan par la conexion a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que  retorna la conexion a la base de datos
	 * @return Connection - la conexion a la base de datos
	 * @throws SQLException - Cualquier error que se genere durante la conexion a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		return DriverManager.getConnection(url, user, password);
	}



	////////////////////////////////////////
	///////Transacciones////////////////////
	////////////////////////////////////////



	public Nodo crearNodo(Nodo nuevo) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaNodos dao = new DAOTablaNodos();
		List<Nodo> ret = null;
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio
			String octetos = nuevo.getOcteto1() + ":" + nuevo.getOcteto2() + ":" + nuevo.getOcteto3();

			if (!darNodosPor(DAOTablaNodos.BUSQUEDA_POR_OCTETOS, octetos).isEmpty()) {
				throw new Exception("Ya hay un nodo con el los octetos iniciales " + octetos);
			}

			nuevo.setClientes(new ArrayList<Cliente>());

			dao.setConn(conn);
			dao.crearNodo(nuevo);

			this.savepoint = this.conn.setSavepoint();
			System.out.println("lo creo");
			//verificar 

			ret = darNodosPor(DAOTablaNodos.BUSQUEDA_POR_OCTETOS, octetos);

			if(ret.isEmpty()) {
				throw new Exception("No se guardo correctamente el nodo, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret.get(0);
	}



	public Cliente crearCliente(Cliente nuevo, Long idNodo) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaClientes dao = new DAOTablaClientes();
		List<Cliente> ret = null;
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio
			if (!darClientesPor(DAOTablaClientes.BUSQUEDA_POR_CEDULA, nuevo.getCedula().toString()).isEmpty()) {
				throw new Exception("Ya hay un cliente con la cedula " + nuevo.getCedula());
			}

			Integer octeto4disp = buscarOcteto4Disponible(idNodo);

			if(octeto4disp == null)
				throw new Exception("El nodo no tiene una ip disponible");


			nuevo.setOcteto4(octeto4disp);


			List<Plan> plan = darPlanesPor(DAOTablaPlanes.BUSQUEDA_POR_ID, nuevo.getPlan().getId().toString());

			if(plan.isEmpty())
				throw new Exception("El plan no existe");

			nuevo.setPlan(plan.get(0));
			dao.setConn(conn);
			dao.crearCliente(nuevo, idNodo);

			this.savepoint = this.conn.setSavepoint();
			System.out.println("lo creo");
			//verificar 

			ret = darClientesPor(DAOTablaClientes.BUSQUEDA_POR_CEDULA,nuevo.getCedula().toString());

			if(ret.isEmpty()) {
				throw new Exception("No se guardo correctamente el cliente, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret.get(0);
	}




	public Integer buscarOcteto4Disponible(Long idNodo) throws SQLException, Exception{
		Integer octeto4disp = null;
		boolean conexionPropia = false; 
		DAOTablaNodos dao = new DAOTablaNodos();
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			if(darNodosPor(DAOTablaNodos.BUSQUEDA_POR_ID, idNodo.toString()).isEmpty())
				throw new Exception("No existe el nodo");

			octeto4disp = dao.buscarOcteto4Disponible(idNodo);


		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return octeto4disp;
	}



	public List<Cliente> darClientesPor(int filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Cliente> clientes = new ArrayList<>(); 
		DAOTablaClientes dao = new DAOTablaClientes();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			clientes = dao.darClientesPor(filtro, parametro);

			if(clientes.size() == 1) {
				Cliente nuevo = clientes.get(0);
				nuevo.setOrdenes(darOrdenesPor(DAOTablaOrdenes.BUSQUEDA_POR_CLIENTE, nuevo.getCedula().toString()));
				List<Orden> nuevas = new ArrayList<Orden>();
				for(Orden or : nuevo.getOrdenes()) {
					or.setFotos(darNombreFotos(or.getId()));
					nuevas.add(or);
				}
				nuevo.setOrdenes(nuevas);
				nuevo.setPlan(darPlanesPor(DAOTablaPlanes.BUSQUEDA_POR_ID, nuevo.getPlan().getId().toString()).get(0));
				nuevo.setDispositivos(darDispositivosPor(DAOTablaDispositivos.BUSQUEDA_POR_CLIENTE, nuevo.getCedula().toString()));
				clientes.clear();
				clientes.add(nuevo);
			}

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clientes; 
	}


	public List<Dispositivo> darDispositivosPor(int filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Dispositivo> dispositivos = new ArrayList<>(); 
		DAOTablaDispositivos dao = new DAOTablaDispositivos();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			dispositivos = dao.darDispositivosPor(filtro, parametro);



		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return dispositivos; 
	}

	public List<Plan> darPlanesPor(int filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Plan> planes = new ArrayList<Plan>(); 
		DAOTablaPlanes dao = new DAOTablaPlanes();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			planes = dao.darPlanesPor(filtro, parametro);


		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return planes; 
	}



	public List<Nodo> darNodosPor(int filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Nodo> nodos = new ArrayList<>(); 
		DAOTablaNodos dao = new DAOTablaNodos();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			nodos = dao.darNodosPor(filtro, parametro);

			if(nodos.size() == 1) {
				Nodo n = nodos.remove(0);
				n.setClientes(darClientesPor(DAOTablaClientes.BUSQUEDA_POR_ID_NODO, n.getId().toString()));
				nodos.add(n);
			}

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return nodos; 
	}

	public Cliente modificarCliente(Long idCliente, Cliente nuevo, Long idNodo) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaClientes dao = new DAOTablaClientes();
		Cliente ret = null;
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio

			List<Cliente> cli = darClientesPor(DAOTablaClientes.BUSQUEDA_POR_CEDULA, idCliente.toString());
			if (cli.isEmpty()) {
				throw new Exception("No hay un cliente con la cedula " + idCliente);
			}



			Nodo nd = darNodosPor(DAOTablaNodos.BUSQUEDA_POR_ID, idNodo.toString()).get(0);
			Cliente cl = cli.get(0);

			if(nd.getOcteto1() != cl.getOcteto1() || nd.getOcteto2() != cl.getOcteto2() || nd.getOcteto3() != cl.getOcteto3()) {
				Integer octeto4disp = buscarOcteto4Disponible(idNodo);

				if(octeto4disp == null)
					throw new Exception("El nodo no tiene una ip disponible");

				nuevo.setOcteto4(octeto4disp);

			}


			if(!idCliente.toString().equalsIgnoreCase(nuevo.getCedula().toString())) {
				System.out.println("cedula anterior:" + idCliente.toString());
				System.out.println("cedula nueva:" + nuevo.getCedula().toString());
				ret = crearCliente(nuevo, idNodo);
				trasladarDispositivos(idCliente, ret.getCedula());
				if(conexionPropia)
					this.savepoint = this.conn.setSavepoint();

				borrarCliente(idCliente);
			} else {
				dao.setConn(this.conn);
				dao.modificarCliente(idCliente, nuevo, idNodo);
				ret = nuevo;
			}



			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}

	public Boolean trasladarDispositivos(Long anteriorClienteId, Long nuevoClienteId) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaDispositivos dao = new DAOTablaDispositivos();
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio


			dao.setConn(this.conn);

			dao.trasladarDispositivo(anteriorClienteId, nuevoClienteId);




			if(!darDispositivosPor(DAOTablaDispositivos.BUSQUEDA_POR_CLIENTE, anteriorClienteId.toString()).isEmpty())
				throw new Exception("No se logró independizar completamente los dispositivos de su antiguo dueño");


			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;

		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;

		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return true;
	}


	public Boolean borrarCliente(Long idCliente) throws SQLException, Exception{
		Boolean ret = false;
		boolean conexionPropia = false; 
		DAOTablaClientes dao = new DAOTablaClientes();
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio
			if(conexionPropia) {
				System.out.println("idCliente a borrar: " + idCliente);
				if (darClientesPor(DAOTablaClientes.BUSQUEDA_POR_CEDULA, idCliente.toString()).isEmpty()){
					throw new Exception("No hay un cliente con la cedula " + idCliente);
				}

				if(!darDispositivosPor(DAOTablaDispositivos.BUSQUEDA_POR_CLIENTE, idCliente.toString()).isEmpty())
					throw new Exception("No se puede eliminar el cliente porque aún tiene dispositivos asociados con su cédula.");

			}



			dao.setConn(conn);
			ret = dao.borrarCliente(idCliente);

			if(conexionPropia)
				this.conn.commit();
			System.out.println("lo creo");
			//verificar 



			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}

	public void agregarDispositivoCliente(Long idCliente, Dispositivo disp) throws SQLException, Exception{

		boolean conexionPropia = false; 
		DAOTablaDispositivos dao = new DAOTablaDispositivos();
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio
			if(conexionPropia) {
				System.out.println("idCliente: " + idCliente);
				if (darClientesPor(DAOTablaClientes.BUSQUEDA_POR_CEDULA, idCliente.toString()).isEmpty()){
					throw new Exception("No hay un cliente con la cedula " + idCliente);
				}
			}

			String mac = disp.getMac1_1()+":"+disp.getMac1_2()+":"+
					disp.getMac2_1()+":"+disp.getMac2_2()+":"+
					disp.getMac3_1()+":"+disp.getMac3_2()+":"+
					disp.getMac4_1()+":"+disp.getMac4_2();

			if(!darDispositivosPor(DAOTablaDispositivos.BUSQUEDA_POR_MACS, mac).isEmpty()) {
				throw new Exception("Ya existe un dispositivo con dicha Mac");
			}


			dao.setConn(conn);
			dao.crearDispositivo(disp);
			if(darDispositivosPor(DAOTablaDispositivos.BUSQUEDA_POR_MACS, mac).isEmpty()) {
				throw new Exception("El dispositivo no fue creado");
			}

			dao.trasladarDispositivo(0L, idCliente);
			if(conexionPropia)
				this.conn.commit();
			System.out.println("lo creo");
			//verificar 

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public List<TipoDispositivo> darTiposDispositivoPor(int filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<TipoDispositivo> tipos = new ArrayList<TipoDispositivo>(); 
		DAOTablaDispositivos dao = new DAOTablaDispositivos();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			tipos = dao.darTiposDispositivoPor(filtro, parametro);


		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return tipos; 
	}

	public List<Orden> darOrdenesPor(Integer filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Orden> ordenes = new ArrayList<Orden>(); 
		DAOTablaOrdenes dao = new DAOTablaOrdenes();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			ordenes = dao.darOrdenesPor(filtro, parametro);

			for (Orden or : ordenes) {
				or.setTecnicos(darTecnicosDeOrden(or.getId()));
			}

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ordenes; 
	}


	public List<String> darNombreFotos(Long idOrden)throws SQLException, Exception{


		List<String> nombres = new ArrayList<String>();
		ClienteFTP cliente = new ClienteFTP("ftp.techcis.com.co", 21, "usuario1@techcis.com.co", "clave1");

		try {
			if(cliente.conectar()) {
				System.out.println("se logró conectar");
				nombres = cliente.darNombreArchivosDirectorio("/"+idOrden, idOrden);
			}else {
				System.out.println("paila al conectarse");
				throw new Exception("la conexión no se logró");
			}

		}catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			cliente.desconectar();
			System.out.println("desconexftp: " + cliente.estaConectado());
		}
		return nombres;
	}



	public List<String> descargarFotosOrden(Long idOrden)throws SQLException, Exception{

		System.out.println("ENTRANDO AL TM........");
		List<String> nombres = new ArrayList<String>(); 
		ClienteFTP cliente = new ClienteFTP("ftp.techcis.com.co", 21, "usuario1@techcis.com.co", "clave1");

		try {
			if(cliente.conectar()) {
				System.out.println("se logró conectar");
				nombres = cliente.bajarArchivosDirectorio("/"+idOrden, "/Users/whatevercamps/wildfly-10.0.0.Final/standalone/deployments/RedetekAPIRest.war/resources/img/ordenes", idOrden);
				for(String no : nombres) {
					System.out.println(no);
				}
			}else {
				System.out.println("paila al conectarse");
				throw new Exception("la conexión no se logró");
			}

		}catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			cliente.desconectar();
			System.out.println("desconexftp: " + cliente.estaConectado());
		}
		return nombres;
	}
	
	public String descargarFotoOrden(Long idOrden, String nombre)throws SQLException, Exception{

		System.out.println(nombre);
		nombre += ".jpg";
		System.out.println(nombre);
		ClienteFTP cliente = new ClienteFTP("ftp.techcis.com.co", 21, "usuario1@techcis.com.co", "clave1");

		try {
			if(cliente.conectar()) {
				System.out.println("se logró conectar");
				String src = "/"+idOrden+"/"+nombre;
				String des = "/Users/whatevercamps/wildfly-10.0.0.Final/standalone/deployments/RedetekAPIRest.war/resources/img/ordenes"+"/"+idOrden+"_"+nombre;
				if(!cliente.bajarArchivo(src, des)) {
					throw new Exception("Falló la descarga del archivo con la ruta de origen " + src + " y la ruta de destino " + des);
				}
			}else {
				System.out.println("paila al conectarse");
				throw new Exception("la conexión no se logró");
			}

		}catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			cliente.desconectar();
			System.out.println("desconexftp: " + cliente.estaConectado());
		}
		return nombre;
	}
	
	public List<Empleado> darTecnicosDisponibles(String sDia) throws SQLException, Exception{
		boolean conexionPropia = false; 	
		
		
		List<Empleado> empleados = new ArrayList<Empleado>(); 
		DAOTablaEmpleados dao = new DAOTablaEmpleados();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			empleados = dao.darEmpleadosPor(DAOTablaEmpleados.DISPONIBLE_EN_TAL_DIA, sDia);

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return empleados; 
	}


	private List<Empleado> darTecnicosDeOrden(Long id) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Empleado> empleados = new ArrayList<Empleado>(); 
		DAOTablaEmpleados dao = new DAOTablaEmpleados();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			empleados = dao.darEmpleadosPor(DAOTablaEmpleados.TEC_DE_ORDEN, id.toString());

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return empleados; 
	}
	
	
	
	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}

	public Boolean crearOrden(Orden orden, Long idCliente) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaOrdenes dao = new DAOTablaOrdenes();
		Boolean ret = null;
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}
			
			//verificar reglas de negocio
			if (!darOrdenesPor(DAOTablaOrdenes.BUSQUEDA_POR_ID, orden.getId().toString()).isEmpty()) {
				throw new Exception("Ya hay una con orden con el id  " + orden.getId());
			}

			if (!darOrdenesPor(DAOTablaOrdenes.BUSQUEDA_POR_CLIENTE_ACTIVAS, idCliente.toString()).isEmpty()) {
				throw new Exception("El cliente con la cédula " + idCliente +  " ya tiene una orden pendiente.");
			}

			
			dao.setConn(conn);
			dao.crearOrden(orden, idCliente);

			
			System.out.println("lo creo");
			//verificar 

			List<Orden> ord= darOrdenesPor(DAOTablaOrdenes.BUSQUEDA_POR_ID, orden.getId().toString());
			
			
			
			if(ord.isEmpty()) {
				throw new Exception("No se guardó correctamente la orden, intente de nuevo...");
			}else {
				for(Empleado em : ord.get(0).getTecnicos()) {
					dao.asignarTecnico(ord.get(0).getId(), em.getId(), 1L);
				}
			}
			
			ClienteFTP cliente = new ClienteFTP("ftp.techcis.com.co", 21, "usuario1@techcis.com.co", "clave1");
			
			if(cliente.conectar()) {
				if(cliente.crearDirectorio("/" + ord.get(0).getId() )) {

					if(conexionPropia)
						this.conn.commit();
					
				}else {
					throw new Exception("No se logró crear el directorio");
				}
			}else {
				throw new Exception("No se logró conectar al servidor ftp");
			}
			


			ret = true;

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}
	
	
	
}







