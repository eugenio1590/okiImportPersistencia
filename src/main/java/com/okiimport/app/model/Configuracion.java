package com.okiimport.app.model;

import java.io.Serializable;

import javax.persistence.*;

import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the configuracion database table.
 * 
 */
@Entity
@Table(name="configuracion")
@NamedQuery(name="Configuracion.findAll", query="SELECT c FROM Configuracion c")
public class Configuracion extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_configuracion")
	private Integer idConfiguracion;
	
	@Column(name="valor_libra", scale=2)
	private Float valorLibra;
	
	@Column(name="porct_iva", scale=2)
	private Float porctIva;

	@Column(name="porct_ganancia", scale=2)
	private Float porctGanancia;
	
	public Configuracion() {
	}

	public Integer getIdConfiguracion() {
		return idConfiguracion;
	}

	public void setIdConfiguracion(Integer idConfiguracion) {
		this.idConfiguracion = idConfiguracion;
	}

	public Float getValorLibra() {
		return valorLibra;
	}

	public void setValorLibra(Float valorLibra) {
		this.valorLibra = valorLibra;
	}

	public Float getPorctIva() {
		return porctIva;
	}

	public void setPorctIva(Float porctIva) {
		this.porctIva = porctIva;
	}

	public Float getPorctGanancia() {
		return porctGanancia;
	}

	public void setPorctGanancia(Float porctGanancia) {
		this.porctGanancia = porctGanancia;
	}
}
