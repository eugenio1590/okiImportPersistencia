package com.okiimport.app.modelo.enumerados;

public enum EEstatusProveedor {

	// Estatus para Proveedores
	ACTIVO("AC", "Activo"),
	SOLICITANTE("SO", "Solicitante");

	private String value;
	private String nombre;

	EEstatusProveedor(String value, String nombre){
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
