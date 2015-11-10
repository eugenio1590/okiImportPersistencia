package com.okiimport.app.modelo.enumerados;
import java.util.ArrayList;
import java.util.List;

public enum EEstatusPersona {

	// Estatus para Proveedores
	ACTIVO("AC", "Activo"),
	SOLICITANTE("SO", "Solicitante");
	
	private String value;
	private String nombre;
	
	EEstatusPersona(String value, String nombre){
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
