package com.okiimport.app.model.enumerados;

public enum EEStatusDetalleCotizacion {
	//NO ESTAN DEFINIDAS
	;
	
	private String value;
	private String nombre;
	
	EEStatusDetalleCotizacion(String value, String nombre){
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
