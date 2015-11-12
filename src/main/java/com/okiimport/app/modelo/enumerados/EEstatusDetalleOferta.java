package com.okiimport.app.modelo.enumerados;

public enum EEstatusDetalleOferta {
	//NO ESTAN DEFINIDAS
	SELECCION("SE", "Seleccionada")
	;
	
	private String value;
	private String nombre;
	
	EEstatusDetalleOferta(String value, String nombre){
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
