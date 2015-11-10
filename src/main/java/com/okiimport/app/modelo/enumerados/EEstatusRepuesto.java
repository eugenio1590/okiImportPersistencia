package com.okiimport.app.modelo.enumerados;
import java.util.ArrayList;
import java.util.List;

public enum EEstatusRepuesto {


	SOLICITADO("SO", "Solicitado"),
	ENVIADO_PROVEEDOR("EP", "Enviado a Proveedores"),
	CON_COTIZACIONES_A("CT", "Con Cotizaciones Asignadas"),
	CON_OFERTAS_A("CO", "Con Ofertas Asignadas"),
	OFERTADO("O", "Ofertado"),
	PEDIDO("P", "Pedido") // cuando el cliente ha seleccionado este requerimiento en alguna de las ofertas y ha generado una solicitud de pedido.
	;
	
	private String value;
	private String nombre;
	
	EEstatusRepuesto(String value, String nombre){
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