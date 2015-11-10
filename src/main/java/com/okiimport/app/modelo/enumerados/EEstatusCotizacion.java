package com.okiimport.app.modelo.enumerados;

public enum EEstatusCotizacion {
	
	// Los que estan por la Base de Datos
	SOLICITUD_COTIZACION("SC", "Solicitud Cotizada"),
	REQUERIMIENTO_COTIZADO("C", "Requerimiento Cotizado");
	
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
