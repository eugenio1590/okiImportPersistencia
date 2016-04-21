package com.okiimport.app.resource.service.model;

import com.google.gson.annotations.SerializedName;

public class FleteZoom {
	
	
	@SerializedName("tipo_tarifa")
	private String flete;
	
	@SerializedName("modalidad_tarifa;")
	private String seguro;
	
	@SerializedName("ciudad_remitente")
	private String combustible;
	
	@SerializedName("ciudad_destinatartio")
	private String subtotal;
	
	@SerializedName("oficina_retirar")
	private String franqueo_postal;
	
	@SerializedName("oficina_retirar")
	private String iva;
	
	@SerializedName("oficina_retirar")
	private String total;
	
	
	public FleteZoom() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getFlete() {
		return flete;
	}


	public void setFlete(String flete) {
		this.flete = flete;
	}


	public String getSeguro() {
		return seguro;
	}


	public void setSeguro(String seguro) {
		this.seguro = seguro;
	}


	public String getCombustible() {
		return combustible;
	}


	public void setCombustible(String combustible) {
		this.combustible = combustible;
	}


	public String getSubtotal() {
		return subtotal;
	}


	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}


	public String getFranqueo_postal() {
		return franqueo_postal;
	}


	public void setFranqueo_postal(String franqueo_postal) {
		this.franqueo_postal = franqueo_postal;
	}


	public String getIva() {
		return iva;
	}


	public void setIva(String iva) {
		this.iva = iva;
	}


	public String getTotal() {
		return total;
	}


	public void setTotal(String total) {
		this.total = total;
	}
}
	

