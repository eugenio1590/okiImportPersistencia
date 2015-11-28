package com.okiimport.app.model;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.model.AbstractEntity;

/**
 * Entity implementation class for Entity: Moneda
 *
 */
@Entity
@Table(name="moneda")
@NamedQuery(name="Moneda.findAll", query="SELECT m FROM Moneda m")
@JsonIgnoreProperties({"historicoMonedas"})
public class Moneda extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	   
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="moneda_id_seq")
	@SequenceGenerator(name="moneda_id_seq", sequenceName="moneda_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_moneda", nullable=false, unique=true)
	private Integer idMoneda;
	
	private String nombre;
	
	
	@ManyToOne
	@JoinColumn(name="id_pais")
	private Pais pais;
	
	private String simbolo;
	
	@Enumerated(EnumType.STRING)
	private EEstatusGeneral estatus;
	
	//bi-directional one-to-many association to HistoricoMoneda
	@OneToMany(mappedBy="moneda", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<HistoricoMoneda> historicoMonedas;

	public Moneda() {
	}   
	
	public Integer getIdMoneda() {
		return this.idMoneda;
	}

	public void setIdMoneda(Integer idMoneda) {
		this.idMoneda = idMoneda;
	}   
	
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}  
	
	  
	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public String getSimbolo() {
		return this.simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
	
	public EEstatusGeneral getEstatus() {
		return estatus;
	}
	
	public void setEstatus(EEstatusGeneral estatus) {
		this.estatus = estatus;
	}
	
	public List<HistoricoMoneda> getHistoricoMonedas() {
		return historicoMonedas;
	}
	
	public void setHistoricoMonedas(List<HistoricoMoneda> historicoMonedas) {
		this.historicoMonedas = historicoMonedas;
	}
	
}
