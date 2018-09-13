package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Dispositivo {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("tipo")
	private TipoDispositivo tipo;
	
	@JsonProperty("descripcion")
	private String descripcion;
	
	@JsonProperty("mac1_1")
	private String mac1_1;
	
	@JsonProperty("mac1_2")
	private String mac1_2;
	
	@JsonProperty("mac2_1")
	private String mac2_1;
	
	@JsonProperty("mac2_2")
	private String mac2_2;
	
	@JsonProperty("mac3_1")
	private String mac3_1;
	
	@JsonProperty("mac3_2")
	private String mac3_2;
	
	@JsonProperty("mac4_1")
	private String mac4_1;
	
	@JsonProperty("mac4_2")
	private String mac4_2;

	public Dispositivo() {
		super();
	}
	
	public Dispositivo(
			@JsonProperty("id") Long id, 
			@JsonProperty("tipo") TipoDispositivo tipo,
			@JsonProperty("descripcion") String descripcion,
			@JsonProperty("mac1_1") String mac1_1, 
			@JsonProperty("mac1_2") String mac1_2, 
			@JsonProperty("mac2_1") String mac2_1, 
			@JsonProperty("mac2_2") String mac2_2, 
			@JsonProperty("mac3_1") String mac3_1, 
			@JsonProperty("mac3_2") String mac3_2,
			@JsonProperty("mac4_1") String mac4_1, 
			@JsonProperty("mac4_2") String mac4_2) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.mac1_1 = mac1_1;
		this.mac1_2 = mac1_2;
		this.mac2_1 = mac2_1;
		this.mac2_2 = mac2_2;
		this.mac3_1 = mac3_1;
		this.mac3_2 = mac3_2;
		this.mac4_1 = mac4_1;
		this.mac4_2 = mac4_2;
	}


	public TipoDispositivo getTipo() {
		return tipo;
	}

	public void setTipo(TipoDispositivo tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMac1_1() {
		return mac1_1;
	}

	public void setMac1_1(String mac1_1) {
		this.mac1_1 = mac1_1;
	}

	public String getMac1_2() {
		return mac1_2;
	}

	public void setMac1_2(String mac1_2) {
		this.mac1_2 = mac1_2;
	}

	public String getMac2_1() {
		return mac2_1;
	}

	public void setMac2_1(String mac2_1) {
		this.mac2_1 = mac2_1;
	}

	public String getMac2_2() {
		return mac2_2;
	}

	public void setMac2_2(String mac2_2) {
		this.mac2_2 = mac2_2;
	}

	public String getMac3_1() {
		return mac3_1;
	}

	public void setMac3_1(String mac3_1) {
		this.mac3_1 = mac3_1;
	}

	public String getMac3_2() {
		return mac3_2;
	}

	public void setMac3_2(String mac3_2) {
		this.mac3_2 = mac3_2;
	}

	public String getMac4_1() {
		return mac4_1;
	}

	public void setMac4_1(String mac4_1) {
		this.mac4_1 = mac4_1;
	}

	public String getMac4_2() {
		return mac4_2;
	}

	public void setMac4_2(String mac4_2) {
		this.mac4_2 = mac4_2;
	}
	
	
	

}
