package com.okiimport.app.model;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The persistent class for the pago_proveedor database table.
 * 
 */
@Entity
@Table(name="pago_proveedor")
@NamedQuery(name="PagoProveedor.findAll", query="SELECT p FROM PagoProveedor p")
@PrimaryKeyJoinColumn(name="id_pago_proveedor")
@JsonIgnoreProperties({"depositos", "ordenCompras"})
public class PagoProveedor  extends Pago implements Serializable {
	private static final long serialVersionUID = 1L;
		
	//bi-directional one-to-many association to OrdenCompra
	@OneToMany(mappedBy="pagoProveedor", fetch=FetchType.LAZY)
	protected List<OrdenCompra> ordenCompras;
	
	public PagoProveedor() {
	}

	public List<OrdenCompra> getOrdenCompras() {
		return ordenCompras;
	}

	public void setOrdenCompras(List<OrdenCompra> ordenCompras) {
		this.ordenCompras = ordenCompras;
	}
	
	public OrdenCompra addOrdenCompra(OrdenCompra ordenCompra){
		getOrdenCompras().add(ordenCompra);
		ordenCompra.setPagoProveedor(this);
		
		return ordenCompra;
	}
	
	public OrdenCompra removeOrdenCompra(OrdenCompra ordenCompra){
		getOrdenCompras().remove(ordenCompra);
		ordenCompra.setPagoProveedor(null);
		
		return ordenCompra;
	}
	
}