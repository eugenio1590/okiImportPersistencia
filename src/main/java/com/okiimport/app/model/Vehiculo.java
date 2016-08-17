package com.okiimport.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the vehiculo database table.
 * 
 */
@Entity
@Table(name="vehiculo")
@NamedQuery(name="Vehiculo.findAll", query="SELECT v FROM Vehiculo v")
public class Vehiculo extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="vehiculo_id_seq")
	@SequenceGenerator(name="vehiculo_id_seq", sequenceName="vehiculo_id_seq", initialValue=1, allocationSize=1)
	@Column(unique=true, nullable=false)
	private Integer id;
	
	@Column(name="anno")
	private Integer anno;
	
	@Column(name="modelo")
	private String modelo;

	@Column(name="serial_carroceria")
	private String serialCarroceria;

	@Column(name="transmision")
	private Boolean transmision;
	
	@Column(name="traccion")
	private Boolean traccion;
	
	@Enumerated(EnumType.STRING)
	private EEstatusGeneral estatus;
	
	@Transient
	private String name;
	
	//bi-directional many-to-one association to MarcaVehiculo
	@ManyToOne
	@JoinColumn(name="id_marca")
	private MarcaVehiculo marcaVehiculo;
	
	//bi-directional many-to-one association to Motor
	@ManyToOne
	@JoinColumn(name="id_motor")
	private Motor motor;
	
	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	public Vehiculo() {
		// TODO Auto-generated constructor stub
	}
	
	public String determinarTransmision(){
		String texto = null;
		if(transmision!=null)
			texto = (transmision) ? "Automatico" : "Sincronico";
		return texto;
	}
	
	public String determinarTraccion(){
		String texto = null;
		if(traccion!=null)
			texto = (traccion) ? "4x2" : "4x4";
		return texto;
	}
	
	public Vehiculo(Cliente cliente){
		this.cliente = cliente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getSerialCarroceria() {
		return serialCarroceria;
	}

	public void setSerialCarroceria(String serialCarroceria) {
		this.serialCarroceria = serialCarroceria;
	}

	public Boolean getTransmision() {
		return transmision;
	}

	public void setTransmision(Boolean transmision) {
		this.transmision = transmision;
	}

	public Boolean getTraccion() {
		return traccion;
	}

	public void setTraccion(Boolean traccion) {
		this.traccion = traccion;
	}

	public EEstatusGeneral getEstatus() {
		return estatus;
	}

	public void setEstatus(EEstatusGeneral estatus) {
		this.estatus = estatus;
	}

	public MarcaVehiculo getMarcaVehiculo() {
		return marcaVehiculo;
	}

	public void setMarcaVehiculo(MarcaVehiculo marcaVehiculo) {
		this.marcaVehiculo = marcaVehiculo;
	}

	public Motor getMotor() {
		return motor;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	//Transient
	public void setName(String name){
		this.name = name;
	}
		
	public String getName(){
		StringBuilder builder = new StringBuilder("");
		builder.append(anno).append(" - ").append(this.modelo).append(" - ").append(this.marcaVehiculo.getNombre());
		setName(builder.toString());
		return this.name;
	}

}
