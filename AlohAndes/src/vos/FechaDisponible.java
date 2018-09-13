package vos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;



public class FechaDisponible {
		
		@JsonProperty("dia")
		private Date dia;
		
		@JsonProperty("emplDisp")
		private List<Empleado> emplDisp;
		
		public FechaDisponible(@JsonProperty("dia") Date dia, @JsonProperty("emplDisp") List<Empleado> emplDisp) {
			super();
			this.dia = dia;
			this.emplDisp = emplDisp;
		}
		
		public FechaDisponible() {
			super();
		}

		public Date getDia() {
			return dia;
		}

		public void setDia(Date dia) {
			this.dia = dia;
		}

		public List<Empleado> getEmplDisp() {
			return emplDisp;
		}

		public void setEmplDisp(List<Empleado> emplDisp) {
			this.emplDisp = emplDisp;
		}
		
		
	}
	
	
	
	
	 

