package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;



public class Nodo {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("nombre") 
	private String nombre;
	
	@JsonProperty("octeto1")
	private Integer octeto1;
	
	@JsonProperty("octeto2")
	private Integer octeto2;
	
	@JsonProperty("octeto3")
	private Integer octeto3;
	
	@JsonProperty("clientes")
	private List<Cliente> clientes; 
	
	public Nodo() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Nodo(
			@JsonProperty("id") Long id,
			@JsonProperty("nombre") String nombre,
			@JsonProperty("octeto1") Integer octeto1, 
			@JsonProperty("octeto2") Integer octeto2, 
			@JsonProperty("octeto3") Integer octeto3) {
		this.id = id;
		this.nombre = nombre;
		this.octeto3 = octeto3;
		this.octeto1 = octeto1;
		this.octeto2 = octeto2;
		clientes = new ArrayList<Cliente>();
		
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Integer getOcteto3() {
		return octeto3;
	}


	public void setOcteto3(Integer octeto3) {
		this.octeto3 = octeto3;
	}


	public List<Cliente> getClientes() {
		return clientes;
	}


	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
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

	public Integer tieneIpLibre() {
		
		for(Integer i=1; i < clientes.size(); i++) {
			Cliente act = clientes.get(i);
			if(act == null)
				return i;
		}
		
		return 0;
	}
	
	public void agregarCliente(Cliente nuevo) {
		clientes.add(nuevo);
	}
	 
}
