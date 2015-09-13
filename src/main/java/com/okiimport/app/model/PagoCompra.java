package com.okiimport.app.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the pago_compra database table.
 * 
 */
@Entity
@Table(name="pago_compra")
@NamedQuery(name="PagoCompra.findAll", query="SELECT p FROM PagoCompra p")
public class PagoCompra extends AbstractEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pago_compra_id_seq")
	@SequenceGenerator(name="pago_compra_id_seq", sequenceName="pago_compra_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_pago_compra")
	private Integer idPagoCompra;
	
	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;
	
	@Column(name="fecha_pago")
	private Timestamp fechaPago;
	
	private Float monto;
	
	@Column(name="nro_deposito")
	private String nroDeposito;
	
	private String estatus;
	
	//bi-directional one-to-one association to Compra
	@OneToOne
	@JoinColumn(name="id_compra")
	private Compra compra;
	
	//bi-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="id_persona")
	private Persona persona;
	
	//bi-directional many-to-one association to FormaPago
	@ManyToOne
	@JoinColumn(name="id_forma_pago")
	private FormaPago formaPago;
	
	//bi-directional many-to-one association to Banco
	@ManyToOne
	@JoinColumn(name="id_banco")
	private Banco banco;

	public PagoCompra() {
	}

	public Integer getIdPagoCompra() {
		return idPagoCompra;
	}

	public void setIdPagoCompra(Integer idPagoCompra) {
		this.idPagoCompra = idPagoCompra;
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

	public Float getMonto() {
		return monto;
	}

	public void setMonto(Float monto) {
		this.monto = monto;
	}

	public String getNroDeposito() {
		return nroDeposito;
	}

	public void setNroDeposito(String nroDeposito) {
		this.nroDeposito = nroDeposito;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}
	
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
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
}
