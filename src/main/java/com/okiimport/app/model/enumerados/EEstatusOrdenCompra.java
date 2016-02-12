package com.okiimport.app.model.enumerados;

public enum EEstatusOrdenCompra {
	CREADA("CR", "Orden de Compra Creada");
	
	private String value;
	private String nombre;
	
	EEstatusOrdenCompra(String value, String nombre){
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
