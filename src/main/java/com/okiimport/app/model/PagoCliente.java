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
@NamedQuery(name="PagoCliente.findAll", query="SELECT pc FROM PagoCliente pc")
@PrimaryKeyJoinColumn(name="id_pagoCliente")
@JsonIgnoreProperties({""})
public class PagoCliente  extends Pago implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column
	private Integer idPagoCliente;
	
	private String estatus;
	

	public PagoCliente() {
	}


	public Integer getIdPagoCliente() {
		return idPagoCliente;
	}

	public void setIdPagoCliente(Integer idPagoCliente) {
		this.idPagoCliente = idPagoCliente;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	
}