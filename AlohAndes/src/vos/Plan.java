package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Plan {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("nombre")
	private String nombre;
	
	@JsonProperty("descripcion")
	private String descripcion;
	
	@JsonProperty("velocidadInternet")
	private Double velocidadInternet;
	
	@JsonProperty("cantidadCanales")
	private Integer cantidadCanales;

	public Plan() {
		super();
	}
	
	public Plan(
			@JsonProperty("id") Long id, 
			@JsonProperty("nombre") String nombre, 
			@JsonProperty("descripcion") String descripcion, 
			@JsonProperty("velocidadInternet") Double velocidadInternet, 
			@JsonProperty("cantidadCanales") Integer cantidadCanales) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.velocidadInternet = velocidadInternet;
		this.cantidadCanales = cantidadCanales;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getVelocidadInternet() {
		return velocidadInternet;
	}

	public void setVelocidadInternet(Double velocidadInternet) {
		this.velocidadInternet = velocidadInternet;
	}

	public Integer getCantidadCanales() {
		return cantidadCanales;
	}

	public void setCantidadCanales(Integer cantidadCanales) {
		this.cantidadCanales = cantidadCanales;
	}


}
