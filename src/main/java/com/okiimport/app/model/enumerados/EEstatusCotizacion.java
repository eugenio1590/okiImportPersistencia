package com.okiimport.app.model.enumerados;

public enum EEstatusCotizacion {
	
	SOLICITADA("SC", "Solicitud de Cotizacion"),
	EMITIDA("E", "Cotizacion Emitida"),
	INCOMPLETA("I", "Cotizacion Incompleta"), //Cotizacion para Editar //Este estatus tambien aplica para el caso en que no se ha enviado la cotizacion al proveedor
	CONCRETADA("C", "Cotizacion Completa");
	//FALTAN ESTATUS
	
	private String value;
	private String nombre;
	
	EEstatusCotizacion(String value, String nombre){
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
