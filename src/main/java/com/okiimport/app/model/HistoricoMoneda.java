package com.okiimport.app.model;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.model.AbstractEntity;
import com.okiimport.app.resource.model.ICoverterMoneda;

/**
 * Entity implementation class for Entity: HistoricoMoneda
 *
 */
@Entity
@Table(name="historico_moneda")
@NamedQuery(name="HistoricoMoneda.findAll", query="SELECT h FROM HistoricoMoneda h")
@JsonIgnoreProperties({"cotizacions", "compras"})
public class HistoricoMoneda extends AbstractEntity implements Serializable, ICoverterMoneda {
	private static final long serialVersionUID = 1L;
	   
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="historico_moneda_id_seq")
	@SequenceGenerator(name="historico_moneda_id_seq", sequenceName="historico_moneda_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_historia", nullable=false, unique=true)
	private Integer idHistoria;
	
	@Column(name="monto_conversion")
	private Float montoConversion;
	
	@Enumerated(EnumType.STRING)
	private EEstatusGeneral estatus;
	
	//bi-directional many-to-one association to Moneda
	@ManyToOne
	@JoinColumn(name="id_moneda")
	private Moneda moneda;
	
	//bi-directional one-to-many association to Cotizacion
	@OneToMany(mappedBy="historicoMoneda")
	private List<Cotizacion> cotizacions;
	
	//bi-directional one-to-many association to Cotizacion
	@OneToMany(mappedBy="historicoMoneda")
	private List<Compra> compras;

	public HistoricoMoneda() {
		this.moneda = new Moneda();
	}   
	public Integer getIdHistoria() {
		return this.idHistoria;
	}

	public void setIdHistoria(Integer idHistoria) {
		this.idHistoria = idHistoria;
	}   
	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}   
	public Float getMontoConversion() {
		return this.montoConversion;
	}

	public void setMontoConversion(Float montoConversion) {
		this.montoConversion = montoConversion;
	}
		
	public EEstatusGeneral getEstatus() {
		return estatus;
	}
	
	public void setEstatus(EEstatusGeneral estatus) {
		this.estatus = estatus;
	}
	
	public List<Cotizacion> getCotizacions() {
		return cotizacions;
	}
	
	public void setCotizacions(List<Cotizacion> cotizacions) {
		this.cotizacions = cotizacions;
	}
	
	public Cotizacion addCotizacion(Cotizacion cotizacion){
		getCotizacions().add(cotizacion);
		cotizacion.setHistoricoMoneda(this);
		
		return cotizacion;
	}
	
	public Cotizacion removeCotizacion(Cotizacion cotizacion){
		getCotizacions().remove(cotizacion);
		cotizacion.setHistoricoMoneda(null);
		
		return cotizacion;
	}
	
	public List<Compra> getCompras() {
		return compras;
	}

	public void setCompras(List<Compra> compras) {
		this.compras = compras;
	}
	
	public Compra addCompra(Compra compra){
		getCompras().add(compra);
		compra.setHistoricoMoneda(this);
		
		return compra;
	}
	
	public Compra removeCompra(Compra compra){
		getCompras().remove(compra);
		compra.setHistoricoMoneda(null);
		
		return compra;
	}
	
	/**METODOS OVERRIDE*/
	//1. ICoverterMoneda
	@Override
	@Transient
	public Number getMontoPorUnidadBase() {
		return getMontoConversion();
	}
	
	@Override
	@Transient
	public String getSimboloMoneda(){
		return getMoneda().getSimbolo();
	}
	
	@Override
	@Transient
	public String withSimbolo(Object val) {
		return getMoneda().withSimbolo(val);
	}
	
	@Override
	public Number convert(Number val){
		final Number montoPorUnidadBase = getMontoPorUnidadBase();
		if(!montoPorUnidadBase.toString().equalsIgnoreCase("0")){
			return ((Number) val).floatValue()/montoPorUnidadBase.floatValue();
		}
		return 0;
	}
	
	@Override
	public Number deconvert(Number val){
		final Number montoPorUnidadBase = getMontoPorUnidadBase();
		return ((Number) val).floatValue()*montoPorUnidadBase.floatValue();
	}
}
