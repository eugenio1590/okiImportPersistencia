package com.okiimport.app.model;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The persistent class for the pago_cliente database table.
 * 
 */
@Entity
@Table(name="pago_cliente")
@NamedQuery(name="PagoCliente.findAll", query="SELECT pc FROM PagoCliente pc")
@PrimaryKeyJoinColumn(name="id_pago_cliente")
@JsonIgnoreProperties({"depositos"})
public class PagoCliente  extends Pago implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//bi-directional one-to-one association to Venta
//	@OneToOne
//	@JoinColumn(name="id_venta")
//	protected Venta venta;
//	
//	public PagoCliente() {
//	}
//
//	public Venta getVenta() {
//		return venta;
//	}
//
//	public void setVenta(Venta venta) {
//		this.venta = venta;
//	}
}
