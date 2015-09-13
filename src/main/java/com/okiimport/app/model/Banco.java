package com.okiimport.app.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the banco database table.
 * 
 */
@Entity
@Table(name="banco")
@NamedQuery(name="Banco.findAll", query="SELECT b FROM Banco b")
@JsonIgnoreProperties({"pagoCompras"})
public class Banco extends AbstractEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="banco_id_seq")
	@SequenceGenerator(name="banco_id_seq", sequenceName="banco_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_banco")
	private Integer idBanco;
	
	private String nombre;
	
	private String estatus;
	
	//bi-directional one-to-many association to PagoCompra
	@OneToMany(mappedBy="banco", fetch=FetchType.LAZY)
	private List<PagoCompra> pagoCompras;

	public Banco() {
	}

	public Integer getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Integer idBanco) {
		this.idBanco = idBanco;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public List<PagoCompra> getPagoCompras() {
		return pagoCompras;
	}

	public void setPagoCompras(List<PagoCompra> pagoCompras) {
		this.pagoCompras = pagoCompras;
	}
	
	public PagoCompra addPagoCompra(PagoCompra pagoCompra){
		getPagoCompras().add(pagoCompra);
		pagoCompra.setBanco(this);
		
		return pagoCompra;
	}
	
	public PagoCompra removePagoCompra(PagoCompra pagoCompra){
		getPagoCompras().remove(pagoCompra);
		pagoCompra.setBanco(null);
		
		return pagoCompra;
	}
}
