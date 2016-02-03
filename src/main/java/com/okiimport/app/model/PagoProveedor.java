package com.okiimport.app.model;


import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The persistent class for the pagoCliente database table.
 * 
 */
@Entity
@NamedQuery(name="PagoProveedor.findAll", query="SELECT p FROM PagoProveedor p")
@PrimaryKeyJoinColumn(name="id_pagoProveedor")
@JsonIgnoreProperties({""})
public class PagoProveedor  extends Pago implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column
	private Integer idPagoProveedor;
	
	private String estatus;
	

	public PagoProveedor() {
	}


	public Integer getIdPagoProveedor() {
		return idPagoProveedor;
	}

	public void setIdPagoProveedor(Integer idPagoProveedor) {
		this.idPagoProveedor = idPagoProveedor;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	
}