package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Empleado {
	
	@JsonProperty("id")
	private Long id;

	@JsonProperty("nombre")
	private String nombre;
	
	@JsonProperty("estado")
	private String estado;
	
	@JsonProperty("cedula")
	private Long cedula;

	@JsonProperty("tipo")
	private Integer tipo;
	
	@JsonProperty("direccion")
	private String direccion;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("telefono")
	private Long telefono;
	
	
	@JsonProperty("cumpleanios")
	private Date cumpleanios;
	
	@JsonProperty("eps")
	private String eps;
	
	@JsonProperty("usuarioFTP")
	private UsuarioFTP usuarioFTP;

	
	public Empleado() {
		super();
	}

	public Empleado(
			@JsonProperty("id") Long id,
			@JsonProperty("estado") String estado,
			@JsonProperty("tipo") Integer tipo,
			@JsonProperty("cumpleanios") Date cumpleanios,
			@JsonProperty("eps") String eps,
			@JsonProperty("cedula") Long cedula, 
			@JsonProperty("nombre") String nombre, 
			@JsonProperty("direccion") String direccion, 
			@JsonProperty("email") String email,
			@JsonProperty("telefono") Long telefono,
			@JsonProperty("usuarioFTP") UsuarioFTP usuarioFTP) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.estado = estado;
		this.cedula = cedula;
		this.tipo = tipo;
		this.direccion = direccion;
		this.email = email;
		this.telefono = telefono;
		this.cumpleanios = cumpleanios;
		this.eps = eps;
		this.usuarioFTP = usuarioFTP;
	}

	public UsuarioFTP getUsuarioFTP() {
		return usuarioFTP;
	}

	public void setUsuarioFTP(UsuarioFTP usuarioFTP) {
		this.usuarioFTP = usuarioFTP;
	}

	public String getEstado() {
		return estado;
	}

	public void activarEstado() {
		this.estado = "Activo";
	}
	
	public void desactivarEstado() {
		this.estado = "Inactivo";
	}


	public String getNombre() {
		return nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	public Long getTelefono() {
		return telefono;
	}



	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}


	public Date getCumpleanios() {
		return cumpleanios;
	}


	public void setCumpleanios(Date cumpleanios) {
		this.cumpleanios = cumpleanios;
	}


	public String getEps() {
		return eps;
	}


	public void setEps(String eps) {
		this.eps = eps;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getCedula() {
		return cedula;
	}

	public void setCedula(Long cedula) {
		this.cedula = cedula;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	
	
}
