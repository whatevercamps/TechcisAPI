package vos;


import org.codehaus.jackson.annotate.JsonProperty;

public class TipoDispositivo {
	

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("nombre")
	private String nombre;
	


	
	public TipoDispositivo() {
		super();
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



	public TipoDispositivo(@JsonProperty("id") Long id, 
			@JsonProperty("nombre") String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;

	}	
	





	
	
}
