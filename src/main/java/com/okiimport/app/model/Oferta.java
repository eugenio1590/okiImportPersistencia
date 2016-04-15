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
	
	@Column(name="porct_iva", scale=2)
	private Float porctIva = new Float(0);

	@Column(name="porct_ganancia", scale=2)
	private Float porctGanancia = new Float(0);
	
	@Enumerated(EnumType.STRING)
	private EEstatusOferta estatus;
	
	@Transient
	private Float total;
	
	@Transient
	private Boolean updateForDecorator;
	
	@Transient
	private Integer nroOferta;
	
	@Transient
	private List<DetalleOferta> detalleOfertasAux;
	
	//bi-directional one-to-one association to Cotizacion
	@OneToOne
	@JoinColumn(name="id_re_cotizacion")
	private Cotizacion cotizacion;
	
	//bi-directional one-to-many association to DetalleOferta
	@OneToMany(mappedBy="oferta", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<DetalleOferta> detalleOfertas;
	
	public Oferta() {
		this.estatus = EEstatusOferta.CREADA;
		this.updateForDecorator = true;
		this.detalleOfertas = new ArrayList<DetalleOferta>();
	}
	
	public Oferta(Integer nroOferta, Float porctIva, Float porctGanancia, Boolean updateForDecorator){
		this();
		this.nroOferta = nroOferta;
		this.porctIva = porctIva;
		this.porctGanancia = porctGanancia;
		this.updateForDecorator = updateForDecorator;
	}

	public Oferta(Integer idOferta, Date fechaCreacion, EEstatusOferta estatus) {
		this();
		this.idOferta = idOferta;
		this.fechaCreacion = new Timestamp(fechaCreacion.getTime());
		this.estatus = estatus;
	}

	public Integer getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(Integer idOferta) {
		this.idOferta = idOferta;
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
	
	//Transient
	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}
	
	public Boolean getUpdateForDecorator() {
		return updateForDecorator;
	}

	public void setUpdateForDecorator(Boolean updateForDecorator) {
		this.updateForDecorator = updateForDecorator;
	}
	
	public Integer getNroOferta() {
		return nroOferta;
	}

	public void setNroOferta(Integer nroOferta) {
		this.nroOferta = nroOferta;
	}

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
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
		return (estatus!=null) ? estatus.getNombre() : "";
	}
	
	public boolean enviar(){
		return this.estatus.equals(EEstatusOferta.SELECCIONADA);
	}
	
	@Transient
	public boolean isCreada(){
		return this.estatus.equals(EEstatusOferta.CREADA);
	}
	
	@Transient
	public boolean isInvalida(){
		return this.estatus.equals(EEstatusOferta.INVALIDA);
	}
	
	@Transient
	public boolean isReCotizacion(){
		return (cotizacion != null);
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
	
	public Float calcularCosto(){
		float total = 0;
		if ( detalleOfertas != null && !detalleOfertas.isEmpty()){
			for(DetalleOferta detalleOferta : detalleOfertas ){
				total += detalleOferta.calcularCostoConverter();
			}
		}
		return total;
	}
	
	public Float calcularTotal(){
		float total = 0;
		if ( detalleOfertas != null && !detalleOfertas.isEmpty()){
			for(DetalleOferta detalleOferta : detalleOfertas ){
				total += detalleOferta.calcularPrecioVentaConverter();
			}
		}
		return total;
	}
	
	@Transient
	public boolean isNotEmpty(){
		return !this.getDetalleOfertas().isEmpty();
	}
	
	@Transient
	public boolean isAprobar(){
		boolean aprobar = true;
		if ( detalleOfertas != null && !detalleOfertas.isEmpty()){
			Boolean aprobado;
			for(DetalleOferta detalleOferta : detalleOfertas ){
				aprobado = detalleOferta.getAprobado();
				aprobar &= (aprobado != null) ? aprobado : false;
				if(!aprobar)
					break;
			}
		}
		else
			aprobar = false;
		
		return aprobar;
	}
	
	@Transient
	public String getTitleNroOferta(){
		return "Oferta Nro. "+String.valueOf(getNroOferta());
	}
	
	@Transient
	public boolean isParaReCotizacion(){
		if(isReCotizacion())
			return false;
		
		if(isNotEmpty()){
			DetalleOferta detalleOferta = detalleOfertas.get(0);
			for(int i = 1; i<detalleOfertas.size(); i++){
				if(!detalleOferta.getProveedor().equals(detalleOfertas.get(i).getProveedor()))
					return false;
				
			}
		}
		return true;
	}
	
	@Transient
	public boolean validoParaRecotizar(){
		boolean valido = true;
		if(isNotEmpty()){
			for(DetalleOferta detalleOferta : detalleOfertas)
				valido &= (detalleOferta.getAprobado() == null) ? false : true;
			
		}
		else
			valido = false;
		return valido;
	}
	
	@Transient
	public List<DetalleCotizacion> getDetallesCotizacionParaRecotizacion(boolean nullCotizacion){
		List<DetalleCotizacion> detalles = new ArrayList<DetalleCotizacion>();
		if(isNotEmpty()){
			for(DetalleOferta detalleOferta : detalleOfertas)
				detalles.add(detalleOferta.getDetalleCotizacionParaRecotizacion(nullCotizacion));
		}
		return detalles;
	}
}
