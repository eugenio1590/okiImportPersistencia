package com.okiimport.app.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.enumerados.EEstatusOferta;
import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the oferta database table.
 * 
 */
@Entity
@Table(name="oferta")
@NamedQuery(name="Oferta.findAll", query="SELECT o FROM Oferta o")
@JsonIgnoreProperties({"detalleOfertas"})
public class Oferta extends AbstractEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="oferta_id_seq")
	@SequenceGenerator(name="oferta_id_seq", sequenceName="oferta_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_oferta")
	private Integer idOferta;
	
	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;
	
	@Column(name="porct_iva", scale=2)
	private Float porctIva = new Float(0);

	@Column(name="porct_ganancia", scale=2)
	private Float porctGanancia = new Float(0);
	
	@Enumerated(EnumType.STRING)
	private EEstatusOferta estatus;
	
	@Transient
	private Float total;
	
	@OneToMany(mappedBy="oferta", fetch=FetchType.LAZY)
	private List<DetalleOferta> detalleOfertas;
	
	@Transient
	private List<DetalleOferta> detalleOfertasAux;

	public Oferta() {
		this.detalleOfertas = new ArrayList<DetalleOferta>();
	}

	public Oferta(Integer idOferta, Date fechaCreacion, EEstatusOferta estatus) {
		super();
		this.idOferta = idOferta;
		this.fechaCreacion = new Timestamp(fechaCreacion.getTime());
		this.estatus = estatus;
		this.detalleOfertas = new ArrayList<DetalleOferta>();
	}

	public Integer getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(Integer idOferta) {
		this.idOferta = idOferta;
	}

	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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

	
	public EEstatusOferta getEstatus() {
		return estatus;
	}

	public void setEstatus(EEstatusOferta estatus) {
		this.estatus = estatus;
	}

	public List<DetalleOferta> getDetalleOfertas() {
		return detalleOfertas;
	}

	public void setDetalleOfertas(List<DetalleOferta> detalleOfertas) {
		this.detalleOfertas = new ArrayList<DetalleOferta>();
		if(detalleOfertas!=null && !detalleOfertas.isEmpty())
			for (Iterator<DetalleOferta> iterator=detalleOfertas.iterator();iterator.hasNext();)
				this.addDetalleOferta(iterator.next());
	}
	
	public List<DetalleOferta> removeAll(List<DetalleOferta> remove){
		if(remove!=null && !remove.isEmpty())
			this.getDetalleOfertas().removeAll(remove);
		return this.getDetalleOfertas();
	}
	
	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public DetalleOferta addDetalleOferta(DetalleOferta detalleOferta){
		getDetalleOfertas().add(detalleOferta);
		detalleOferta.setOferta(this);
		
		return detalleOferta;
	}
	
	public DetalleOferta removeDetalleOferta(DetalleOferta detalleOferta){
		getDetalleOfertas().remove(detalleOferta);
		detalleOferta.setOferta(null);
		
		return detalleOferta;
	}

	/**METODOS PROPIOS DE LA CLASE*/
	public String determinarEstatus(){
		return (estatus!=null) ? estatus.getValue() : "";
	}
	
	public boolean enviar(){
		return this.estatus.equals(EEstatusOferta.SELECCION);
	}
	
	public void copyDetallesOfertas(){
		detalleOfertasAux = new ArrayList<DetalleOferta>(detalleOfertas);
	}
	
	public void recoveryCopyDetallesOfertas(){
		if(detalleOfertasAux!=null && !detalleOfertasAux.isEmpty()){
			detalleOfertas = new ArrayList<DetalleOferta>(detalleOfertasAux);
			detalleOfertasAux.clear();
		}
	}
	
	public Float calcularTotal(){
		float total = 0;
		if ( detalleOfertas != null && !detalleOfertas.isEmpty()){
			for(DetalleOferta detalleOferta : detalleOfertas ){
				total = total + detalleOferta.calcularPrecioVenta();
			}
		}
		return total;
	}
}
