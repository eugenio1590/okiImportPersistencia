package com.okiimport.app.modelo.enumerados;

public enum EEstatusBanco {
	
	 ACTIVO("AC","Activo"),
	 INACTIVO ("IN", "Inactivo");
	 
	    private String value;
		private String nombre;
		
		EEstatusBanco(String value, String nombre){
			this.value = value;
			this.nombre = nombre;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

}
