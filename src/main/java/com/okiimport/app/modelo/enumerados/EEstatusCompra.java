package com.okiimport.app.modelo.enumerados;

public enum EEstatusCompra {

	SOLICITUD_PEDIDO ("solicitada", "Solicitud de Pedido"),
	COMPRA_REALIZADA_ENVIADA("enviada", "Compra Realizada y Enviada a Proveedores");
	
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
