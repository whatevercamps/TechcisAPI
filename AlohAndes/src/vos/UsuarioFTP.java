package vos;


import org.codehaus.jackson.annotate.JsonProperty;

public class UsuarioFTP {
	

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("usuario")
	private String usuario;
	
	@JsonProperty("clave")
	private String clave;


	
	public UsuarioFTP() {
		super();
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getUsuario() {
		return usuario;
	}



	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}



	public String getClave() {
		return clave;
	}



	public void setClave(String clave) {
		this.clave = clave;
	}



	public UsuarioFTP(@JsonProperty("id") Long id, 
			@JsonProperty("usuario") String usuario, 
			@JsonProperty("clave") String clave) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.clave = clave;
	}	
	





	
	
}
