package com.okiimport.app.model.enumerados;

public enum EEstatusOferta {
	
	CREADA("CE", "Oferta Creada"),
	SELECCIONADA ("SE", "Oferta para enviar al Cliente"),
	ENVIADA ("EN", "Oferta Enviada al Cliente"),
	RECIBIDA ("RE", "Recibida por el Analista"),
	INVALIDA ("IN", "No Mostrada al cliente"),
	RECHAZADA("RC", "La oferta fue rechazada");
	
	private String value;
	private String nombre;
	
	EEstatusOferta(String value, String nombre){
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
