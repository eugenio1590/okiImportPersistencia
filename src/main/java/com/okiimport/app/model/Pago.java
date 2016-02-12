package com.okiimport.app.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"depositos"})
public abstract class Pago extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pago_id_seq")
	@SequenceGenerator(name="pago_id_seq", sequenceName="pago_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id", unique=true, nullable=false)
	protected Integer id;

	protected Date fechaPago;

	private Float monto;

	protected String Descripcion; 

	protected String estatus;

	//bi-directional many-to-one association to FormaPago
	@ManyToOne
	@JoinColumn(name="id_forma_pago")
	protected FormaPago formaPago;

	//bi-directional many-to-one association to Banco
	@ManyToOne
	@JoinColumn(name="id_banco")
	protected Banco banco;

	//bi-directional one-to-many association to Deposito
	@OneToMany(mappedBy="pago", fetch=FetchType.LAZY)
	private List<Deposito> depositos;

	public Pago() {
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
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

	public List<Deposito> getDepositos() {
		return depositos;
	}

	public void setDepositos(List<Deposito> depositos) {
		this.depositos = depositos;
	}

	public Deposito addDeposito(Deposito deposito){
		getDepositos().add(deposito);
		deposito.setPago(this);
		
		return deposito;
	}
	
	public Deposito removeDeposito(Deposito deposito){
		getDepositos().remove(deposito);
		deposito.setPago(null);
		
		return deposito;
	}
}
