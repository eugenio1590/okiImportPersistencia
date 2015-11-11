package com.okiimport.app.modelo.enumerados;

public enum EEstatusOferta {

	SOLICITADO ("SO", "Enviada al Cliente"),
	ENVIADA ("EN", "Con Respuesta del Cliente"),
	RECIBIDA ("RE", "Recibida por el Analista");
	
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
