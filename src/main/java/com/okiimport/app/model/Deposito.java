package com.okiimport.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the deposito database table.
 * 
 */
@Entity
@Table(name="deposito")
@NamedQuery(name="Deposito.findAll", query="SELECT d FROM Deposito d")
public class Deposito extends AbstractEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="deposito_id_seq")
	@SequenceGenerator(name="deposito_id_seq", sequenceName="deposito_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_deposito")
	private Integer idDeposito;
	
	private String numero;
	
	private String descripcion;
	
	private Float monto;
	
	@Column(name="fecha_deposito")
	private Date fechaDeposito;
	
    private String estatus;
	
	//bi-directional many-to-one association to Pago
	@ManyToOne
	@JoinColumn(name="id_pago")
	private Pago pago;

	public Deposito() {
	}

	public Integer getIdDeposito() {
		return idDeposito;
	}

	public void setIdDeposito(Integer idDeposito) {
		this.idDeposito = idDeposito;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Float getMonto() {
		return monto;
	}

	public void setMonto(Float monto) {
		this.monto = monto;
	}

	public Date getFechaDeposito() {
		return fechaDeposito;
	}

	public void setFechaDeposito(Date fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}
	
}