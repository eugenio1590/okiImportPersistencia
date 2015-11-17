package com.okiimport.app.model.enumerados;

public enum EEstatusCompra {

	SOLICITADA ("SO", "Solicitud de Pedido"),
	ENVIADA("EN", "Compra Realizada y Enviada a Proveedores"),
	PAGADA ("PA", "Pagada"),
	RECHAZADA("RE", "Rechazada"),
	EN_ESPERA("ES", "En Espera"),
	CANCELADA("CA", "Cancelada");
	
	
	//Faltan estatus
	
	private String value;
	private String nombre;
	
	EEstatusCompra(String value, String nombre){
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
