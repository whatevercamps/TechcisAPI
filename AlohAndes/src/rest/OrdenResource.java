package rest;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import vos.Nodo;
import vos.Orden;
import vos.Plan;
import vos.Dispositivo;





/**
 * Clase que expone servicios REST con ruta base: http://<ip o nombre del host>:8080/RotondAndes/rest/clientes/...
 * @author David Bautista
 */

@Path("ordenes")
public class OrdenResource {



	@Context
	private ServletContext context;

	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}

	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response darOrdenesPor(@QueryParam("filtro") Integer filtro, @QueryParam("parametro") String parametro) throws SQLException, Exception{
		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			
			return Response.status( 200 ).entity( tm.darOrdenesPor(filtro, parametro) ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	
	@GET
	@Path("{id: \\d+}/descargarfotosftp")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response descargarFotosOrden(@PathParam("id") Long id) throws SQLException, Exception{
		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			System.out.println("ENTRANDO AL GET........");
			return Response.status( 200 ).entity( tm.descargarFotosOrden(id)).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	
	@GET
	@Path("{id: \\d+}/descargarfotoftp/{fname: \\d+}")
	public Response cargarFotosOrden(@PathParam("id") Long id, @PathParam("fname") Integer name) throws SQLException, Exception{
		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			
			return Response.status( 200 ).entity( tm.descargarFotoOrden(id, name.toString())).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@GET
	@Path("{id: \\d+}/nombresFotosftp")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response darNumFotosOrden(@PathParam("id") Long id) throws SQLException, Exception{
		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			return Response.status( 200 ).entity( tm.darNombreFotos(id)).build();
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	
	@POST
	@Path("crearOrden")
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearOrden(Orden Orden, @QueryParam("idCliente") Long idCliente) throws SQLException, Exception{
		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			return Response.status( 200 ).entity(tm.crearOrden(Orden, idCliente)).build();
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}	
	
	
	
}
