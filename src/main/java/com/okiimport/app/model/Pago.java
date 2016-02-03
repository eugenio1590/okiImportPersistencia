package com.okiimport.app.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.factory.persona.EstatusPersonaFactory;
import com.okiimport.app.model.factory.persona.EstatusPersonaFactory.IEstatusPersona;
import com.okiimport.app.resource.model.AbstractEntity;


/**
 * The persistent class for the pago database table.
 * 
 */

@Entity
@Table(name="pago")
@NamedQuery(name="Pago.findAll", query="SELECT p FROM Pago p")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="pago_type")


@JsonIgnoreProperties({"", "", ""}) //Duda


public abstract class Pago extends AbstractEntity implements Serializable {

	
private static final long serialVersionUID = 1L;
	
//Secuencia
    

protected Integer idPago;

protected Timestamp fechaCreacion;

protected Timestamp fechaPago;

private Float monto;

protected String Descripcion; 

protected String estatus;

	
 //duda con esta relacion bidireccional con Compra



	//bi-directional one-to-one association to Compra
	@OneToOne
	@JoinColumn(name="id_compra")
	protected Venta compra;
	
	
	//bi-directional many-to-one association to FormaPago
	@ManyToOne
	@JoinColumn(name="id_forma_pago")
	protected FormaPago formaPago;
	
	//bi-directional many-to-one association to Banco
	@ManyToOne
	@JoinColumn(name="id_banco")
	protected Banco banco;
	
	//bi-directional many-to-one association to Deposito
	@ManyToOne
	@JoinColumn(name="id_deposito")
	private Deposito deposito;
	

	public Pago() {
	}

	public Integer getIdPago() {
		return idPago;
	}

	public void setIdPago(Integer idPago) {
		this.idPago = idPago;
	}

	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Timestamp getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Timestamp fechaPago) {
		this.fechaPago = fechaPago;
	}

	public String getDescripcion() {
		return Descripcion;
	}

	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}

	public Float getMonto() {
		return monto;
	}

	public void setMonto(Float monto) {
		this.monto = monto;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Venta getCompra() {
		return compra;
	}

	public void setCompra(Venta compra) {
		this.compra = compra;
	}
	
	public FormaPago getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPago formaPago) {
		this.formaPago = formaPago;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Deposito getDeposito() {
		return deposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
}
