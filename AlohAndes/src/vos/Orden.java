package vos;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Orden {
	

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("fecha")
	private Date fecha;
	
	@JsonProperty("tipo")
	private Integer tipo;
	
	@JsonProperty("estado")
	private Integer estado;
	
	@JsonProperty("plan")
	private Plan plan;
	
	@JsonProperty("tecnicos")
	private List<Empleado> tecnicos;

	@JsonProperty("fotos")
	private List<String> fotos;
	
	public Orden() {
		super();
	}	
	
	public Orden(
			@JsonProperty("id") Long id,
			@JsonProperty("fecha") Date fecha,
			@JsonProperty("tipo") Integer tipo, 
			@JsonProperty("nombre") String nombre, 
			@JsonProperty("tecnicos") List<Empleado> tecnicos,
			@JsonProperty("plan") Plan plan,
			@JsonProperty("fotos") List<String> fotos,
			@JsonProperty("estado")Integer estado) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.tipo = tipo;
		this.estado = estado;
		this.plan = plan;
		this.tecnicos = tecnicos;
		this.fotos = fotos;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public List<String> getFotos() {
		return fotos;
	}

	public void setFotos(List<String> fotos) {
		this.fotos = fotos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public List<Empleado> getTecnicos() {
		return tecnicos;
	}

	public void setTecnicos(List<Empleado> tecnicos) {
		this.tecnicos = tecnicos;
	}






	
	
}
