package com.okiimport.app.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the deposito database table.
 * 
 */
@Entity
@Table(name="deposito")
@NamedQuery(name="Deposito.findAll", query="SELECT d FROM Deposito d")
@JsonIgnoreProperties({""})

public class Deposito extends AbstractEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="deposito_id_seq")
	//@SequenceGenerator(name="deposito_id_seq", sequenceName="deposito_id_seq", initialValue=1, allocationSize=1)
	
	@Column(name="id_deposito")
	private Integer idDeposito;
	
	private String numDeposito;
	
	private String descripcion;
	
	private Float monto;
	
	private Timestamp fechaDeposito;
	
    private String estatus;
	
	//bi-directional one-to-many association to Deposito
	@OneToMany(mappedBy="deposito", fetch=FetchType.LAZY)
	private List<Pago> pago;

	public Deposito() {
	}

	public Integer getIdDeposito() {
		return idDeposito;
	}

	public void setIdDeposito(Integer idDeposito) {
		this.idDeposito = idDeposito;
	}

	public String getNumDeposito() {
		return numDeposito;
	}

	public void setNumDeposito(String numDeposito) {
		this.numDeposito = numDeposito;
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

	public Timestamp getFechaDeposito() {
		return fechaDeposito;
	}

	public void setFechaDeposito(Timestamp fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public List<Pago> getPago() {
		return pago;
	}

	public void setPago(List<Pago> pago) {
		this.pago = pago;
	}

	public Pago addPago(Pago pago){
		getPago().add(pago);
		pago.setDeposito(this);
		
		return pago;
	}
	
	public Pago removePagoCompra(Pago pago){
		getPago().remove(pago);
		pago.setDeposito(null);
		
		return pago;
	}
	
}