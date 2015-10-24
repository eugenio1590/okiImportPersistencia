package com.okiimport.app.modelo.enumerados;

import java.util.ArrayList;
import java.util.List;

public enum EEstatusRequerimiento {
	EMITIDO("CR", "Emitido"),
	RECIBIDO_EDITADO("E", "Recibido y Editado"),
	ENVIADO_PROVEEDOR("EP", "Enviado a Proveedores"),
	CON_COTIZACIONES_A("CT", "Con Cotizaciones Asignadas"),
	CON_COTIZACIONES_I("EC", "Con Cotizaciones Incompletas"),
	OFERTADO("O", "Ofertado"),
	CONCRETADO("CC", "Concretado"),
	COMPRADO("CP", "Comprado"),
	CERRADO("C", "Cerrado")
	;
	
	private String value;
	private String nombre;
	
	EEstatusRequerimiento(String value, String nombre){
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
	
	/**METODOS PROPIOS DE LA CLASE*/
	public static List<EEstatusRequerimiento> getEstatusGeneral(){
		ArrayList<EEstatusRequerimiento> estatusGeneral = new ArrayList<EEstatusRequerimiento>();
		estatusGeneral.addAll(getEstatusEmitidos());
		estatusGeneral.addAll(getEstatusProcesados());
		estatusGeneral.addAll(getEstatusOfertados());
		return estatusGeneral;
	}
	
	public static List<EEstatusRequerimiento> getEstatusEmitidos(){
		ArrayList<EEstatusRequerimiento> estatusEmitidos = new ArrayList<EEstatusRequerimiento>();
		estatusEmitidos.add(EMITIDO);
		estatusEmitidos.add(RECIBIDO_EDITADO);
		estatusEmitidos.add(ENVIADO_PROVEEDOR);
		return estatusEmitidos;
	}
	
	public static List<EEstatusRequerimiento> getEstatusProcesados(){
		ArrayList<EEstatusRequerimiento> estatusProcesados = new ArrayList<EEstatusRequerimiento>();
		estatusProcesados.add(CON_COTIZACIONES_A);
		estatusProcesados.add(CON_COTIZACIONES_I);
		return estatusProcesados;
	}
	
	public static List<EEstatusRequerimiento> getEstatusOfertados(){
		ArrayList<EEstatusRequerimiento> estatusOfertados = new ArrayList<EEstatusRequerimiento>();
		estatusOfertados.add(OFERTADO);
		estatusOfertados.add(CONCRETADO);
		estatusOfertados.add(COMPRADO);
		estatusOfertados.add(CERRADO);
		return estatusOfertados;
	}
}
