package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Cliente {
	

	@JsonProperty("estado")
	private String estado;
	
	@JsonProperty("cedula")
	private Long cedula;
	
	@JsonProperty("octeto4")
	private Integer octeto4;
	
	@JsonProperty("nombre")
	private String nombre;
	
	@JsonProperty("octeto1")
	private Integer octeto1;
	
	@JsonProperty("octeto2")
	private Integer octeto2;
	
	@JsonProperty("octeto3")
	private Integer octeto3;
	
	@JsonProperty("direccion")
	private String direccion;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("telefono")
	private Long telefono;
	
	@JsonProperty("nombreNodo")
	private String nombreNodo;
	
	@JsonProperty("ordenes")
	private List<Orden> ordenes;
	
	
	public String getNombreNodo() {
		return nombreNodo;
	}

	public void setNombreNodo(String nombreNodo) {
		this.nombreNodo = nombreNodo;
	}

	@JsonProperty("plan")
	private Plan plan;
	
	@JsonProperty("dispositivos")
	private List<Dispositivo> dispositivos;
	
	public Cliente() {
		super();
	}

	public Cliente(
			@JsonProperty("cedula") Long cedula, 
			@JsonProperty("octeto4") Integer octeto4, 
			@JsonProperty("nombre") String nombre, 
			@JsonProperty("direccion") String direccion, 
			@JsonProperty("email") String email,
			@JsonProperty("telefono") Long telefono,
			@JsonProperty("plan") Plan plan,
			@JsonProperty("ordenes")List<Orden> ordenes) {
		super();
		this.estado = "Inactivo";
		this.cedula = cedula;
		this.octeto4 = octeto4;
		this.nombre = nombre;
		this.direccion = direccion;
		this.email = email;
		this.telefono = telefono;
		this.plan = plan;
		this.ordenes = ordenes;
	}



	
	
	public List<Orden> getOrdenes() {
		return ordenes;
	}

	public void setOrdenes(List<Orden> ordenes) {
		this.ordenes = ordenes;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	public List<Dispositivo> getDispositivos() {
		return dispositivos;
	}

	public void setDispositivos(List<Dispositivo> dispositivos) {
		this.dispositivos = dispositivos;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Integer getOcteto1() {
		return octeto1;
	}

	public void setOcteto1(Integer octeto1) {
		this.octeto1 = octeto1;
	}

	public Integer getOcteto2() {
		return octeto2;
	}

	public void setOcteto2(Integer octeto2) {
		this.octeto2 = octeto2;
	}

	public Integer getOcteto3() {
		return octeto3;
	}

	public void setOcteto3(Integer octeto3) {
		this.octeto3 = octeto3;
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


	public Integer getOcteto4() {
		return octeto4;
	}

	public void setOcteto4(Integer octeto4) {
		this.octeto4 = octeto4;
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
