package rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dao.DAOTablaClientes;
import tm.RedetekApiTM;
import vos.Cliente;
import vos.Dispositivo;
import vos.Plan;





/**
 * Clase que expone servicios REST con ruta base: http://<ip o nombre del host>:8080/RotondAndes/rest/clientes/...
 * @author David Bautista
 */

@Path("clientes")
public class ClienteResorce {

	@XmlRootElement
	public static class DatosPago {
		@XmlElement Double CantPago;
		@XmlElement Long[] idReservas;
	}


	@Context
	private ServletContext context;

	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}

	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearCliente(Cliente cliente, @QueryParam("idNodo")Long idNodo) throws SQLException, Exception{
		System.out.println("entreeeeee");

		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			Cliente clienteNew = tm.crearCliente(cliente, idNodo);
			return Response.status( 200 ).entity( clienteNew ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	

	@GET
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response darClientes() throws SQLException, Exception{
		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {	
			return Response.status( 200 ).entity( tm.darClientesPor(0, "hola") ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	@GET
	@Path("{id: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response darCliente(@PathParam("id") Long id) throws SQLException, Exception{
		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			List<Cliente> encontrado = tm.darClientesPor(DAOTablaClientes.BUSQUEDA_POR_CEDULA, id.toString());
			if(encontrado.isEmpty())
				throw new Exception("No existe un cliente con la cédula " + id);
			return Response.status( 200 ).entity( encontrado.get(0) ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	@PUT
	@Path("{id: \\d+}")
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response modificarCliente(Cliente cliente, @QueryParam("idNodo")Long idNodo, @PathParam("id") Long idCliente)throws SQLException, Exception{
		System.out.println("entreeeeee");

		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			Cliente clienteNew = tm.modificarCliente(idCliente, cliente, idNodo);
			return Response.status( 200 ).entity( clienteNew ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	@POST
	@Path("{id: \\d+}/agregarDispositivo")
	@Consumes({ MediaType.APPLICATION_JSON } )
	public Response agregarDispositivoCliente(@PathParam("id") Long idCliente, Dispositivo disp)throws SQLException, Exception{
		System.out.println("entreeeeee");

		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			tm.agregarDispositivoCliente(idCliente, disp);
			System.out.println("si, si lo creo");
			return Response.status( 200 ).entity( "ok" ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@DELETE
	@Path("{id: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response borrarCliente(@PathParam("id") Long idCliente)throws SQLException, Exception{
		System.out.println("entreeeeee");

		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			tm.borrarCliente(idCliente);
			return Response.status( 200 ).entity( "ok" ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


}
