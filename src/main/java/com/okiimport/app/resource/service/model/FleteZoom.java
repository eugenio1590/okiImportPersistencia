package com.okiimport.app.resource.service.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import com.google.gson.annotations.SerializedName;

public class FleteZoom {
	
	private static DecimalFormat decimalFormat;
	
	@SerializedName("flete")
	private String flete;
	
	@SerializedName("seguro")
	private String seguro;
	
	@SerializedName("combustible")
	private String combustible;
	
	@SerializedName("subtotal")
	private String subtotal;
	
	@SerializedName("franqueo_postal")
	private String franqueoPostal;
	
	@SerializedName("iva")
	private String iva;
	
	@SerializedName("total")
	private String total;
	
	@SerializedName("errormessage")
	private String errorMessage;
	
	public FleteZoom() {
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
	
	public String getFranqueoPostal() {
		return franqueoPostal;
	}

	public void setFranqueoPostal(String franqueoPostal) {
		this.franqueoPostal = franqueoPostal;
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
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	/**METODOS ESTATICOS DE LA CLASE*/
	public static DecimalFormat getDecimalFormat(){
        if (decimalFormat == null){
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            symbols.setGroupingSeparator(',');
            decimalFormat = new DecimalFormat("#.##", symbols);
        }

        return decimalFormat;
    }

	/**METODOS PROPIOS DE LA CLASE*/
	public Float totalReal(){
		Float totalReal = new Float(0);
		if(this.errorMessage==null)
			try {
				totalReal = getDecimalFormat().parse(this.total).floatValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return totalReal;
	}
}
