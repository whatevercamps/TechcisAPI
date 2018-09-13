package rest;

import java.sql.SQLException;
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
import vos.Dispositivo;





/**
 * Clase que expone servicios REST con ruta base: http://<ip o nombre del host>:8080/RotondAndes/rest/clientes/...
 * @author David Bautista
 */

@Path("planes")
public class PlanResource {

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
	public Response crearPlan(Nodo nodo) throws SQLException, Exception{
		System.out.println("entreeeeee");
		
		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			Nodo nodoNew = tm.crearNodo(nodo);
			return Response.status( 200 ).entity( nodoNew ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response darPlanes() throws SQLException, Exception{
		RedetekApiTM tm = new RedetekApiTM(getPath());
		try {
			
			return Response.status( 200 ).entity( tm.darPlanesPor(0, "hola") ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
}
