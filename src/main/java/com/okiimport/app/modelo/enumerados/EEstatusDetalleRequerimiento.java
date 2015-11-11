package com.okiimport.app.modelo.enumerados;

public enum EEstatusDetalleRequerimiento {
	
	SOLICITADO("SC", "Solicitado"),
	ENVIADO_PROVEEDOR("EP", "Enviado a Proveedores"),
	CON_COTIZACIONES_A("CT", "Con Cotizaciones Asignadas"),
	OFERTADO("O", "Ofertado"),
	COMPRADO("CP", "Comprado"),
	CERRADO("C", "Cerrado")
	;
	
	private String value;
	private String nombre;
	
	EEstatusDetalleRequerimiento (String value, String nombre){
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
