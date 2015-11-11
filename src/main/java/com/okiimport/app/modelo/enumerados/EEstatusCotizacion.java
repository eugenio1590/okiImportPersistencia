package com.okiimport.app.modelo.enumerados;

public enum EEstatusCotizacion {
	
	SOLICITUD_COTIZACION("SC", "Solicitud de Cotizacion"),
	COTIZACION_PARA_EDITAR("EC", "Cotizacion Incompleta"),
	COMPLETADA("C", "Cotizacion Completa");
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
