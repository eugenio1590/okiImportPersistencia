package com.okiimport.app.model.enumerados;

public enum EEStatusDetalleCotizacion {
	
	SOLICITADO("SO", "Solicitado"),
	ENCONTRADO("EN", "Encontrado"),
	COMPRADO("CO","Comprado");
	
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
