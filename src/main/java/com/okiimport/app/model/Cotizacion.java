package com.okiimport.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.enumerados.EEstatusCotizacion;
import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the cotizacion database table.
 * 
 */
@Entity
@Table(name="cotizacion")
@NamedQuery(name="Cotizacion.findAll", query="SELECT c FROM Cotizacion c")
@JsonIgnoreProperties({"detalleCotizacions"})
public class Cotizacion extends AbstractEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cotizacion_id_seq")
	@SequenceGenerator(name="cotizacion_id_seq", sequenceName="cotizacion_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_cotizacion")
	private Integer idCotizacion;
	
	@Column(name="fecha_vencimiento")
	private Date fechaVencimiento;
	
	private String condiciones;
	
	@Enumerated(EnumType.STRING)
	private EEstatusCotizacion estatus;
	
	private String mensaje;
	
	private Boolean tipo;
	
	@Column(name="precio_flete", scale=2)
	private Float precioFlete;
	
	@Transient
	private Float totalPrecioVenta=new Float(0);
	
	@Transient
	private Float totalFlete=new Float(0);
	
	@Transient
	private Float totalCotizacion=new Float(0);
	
	@Transient
	private Float totalFleteCalculado=new Float(0);
	
	//bi-directional one-to-one association to Oferta
	@OneToOne(mappedBy="cotizacion")
	private Oferta oferta;
	
	//bi-directional many-to-one association to Proveedor
	@ManyToOne
	@JoinColumn(name="id_proveedor")
	private Proveedor proveedor;
	
	//bi-directional many-to-one association to HistoricoMoneda
	@ManyToOne
	@JoinColumn(name="id_historico_moneda")
	private HistoricoMoneda historicoMoneda;
	
	//bi-directional one-to-many association to DetalleCotizacion
	@OneToMany(mappedBy="cotizacion",fetch=FetchType.LAZY) //cascade=CascadeType.REMOVE, orphanRemoval=true
	private List<DetalleCotizacion> detalleCotizacions;

	public Cotizacion() {
		this.detalleCotizacions = new ArrayList<DetalleCotizacion>();
		this.historicoMoneda = new HistoricoMoneda();
	}
	
	public Cotizacion(Boolean tipo){
		this();
		this.tipo = tipo;
	}
	
	public Cotizacion(Date fechaCreacion){
		this();
		this.fechaCreacion = fechaCreacion;
	}
	
	public Cotizacion(String mensaje, Boolean tipo){
		this(tipo);
		this.mensaje = mensaje;
	}

	public Cotizacion(Integer idCotizacion,	Date fechaCreacion, Date fechaVencimiento, 
			EEstatusCotizacion estatus, String mensaje) {
		this();
		this.idCotizacion = idCotizacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaVencimiento = fechaVencimiento;
		this.estatus = estatus;
		this.mensaje = mensaje;
	}

	public Cotizacion(Integer idCotizacion, Date fechaCreacion, Date fechaVencimiento, 
			EEstatusCotizacion estatus, String mensaje, Proveedor proveedor, HistoricoMoneda historicoMoneda) {
		this(idCotizacion, fechaCreacion, fechaVencimiento, estatus, mensaje);
		this.proveedor = proveedor;
		this.historicoMoneda = historicoMoneda;
	}
	
	public Cotizacion(Proveedor proveedor){
		this();
		this.proveedor = proveedor;
	}

	public Integer getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(Integer idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getCondiciones() {
		return condiciones;
	}

	public void setCondiciones(String condiciones) {
		this.condiciones = condiciones;
	}

	public EEstatusCotizacion getEstatus() {
		return estatus;
	}

	public void setEstatus(EEstatusCotizacion estatus) {
		this.estatus = estatus;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Boolean getTipo() {
		return tipo;
	}

	public void setTipo(Boolean tipo) {
		this.tipo = tipo;
	}

	public Float getPrecioFlete() {
		return precioFlete;
	}

	public void setPrecioFlete(Float precioFlete) {
		this.precioFlete = precioFlete;
	}

	public Oferta getOferta() {
		return oferta;
	}

	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public HistoricoMoneda getHistoricoMoneda() {
		return historicoMoneda;
	}

	public void setHistoricoMoneda(HistoricoMoneda historicoMoneda) {
		this.historicoMoneda = historicoMoneda;
	}

	public List<DetalleCotizacion> getDetalleCotizacions() {
		return detalleCotizacions;
	}

	public void setDetalleCotizacions(List<DetalleCotizacion> detalleCotizacions) {
		try {
			if(detalleCotizacions!=null && !detalleCotizacions.isEmpty())
				for(DetalleCotizacion detalle : detalleCotizacions)
					this.addDetalleCotizacion(detalle);
		} catch(Exception e){
			this.detalleCotizacions = detalleCotizacions;
		}
	}

	public DetalleCotizacion addDetalleCotizacion(DetalleCotizacion detalleCotizacion) {
		getDetalleCotizacions().add(detalleCotizacion);
		detalleCotizacion.setCotizacion(this);

		return detalleCotizacion;
	}

	public DetalleCotizacion removeDetalleCotizacion(DetalleCotizacion detalleCotizacion) {
		getDetalleCotizacions().remove(detalleCotizacion);
		detalleCotizacion.setCotizacion(null);

		return detalleCotizacion;
	}
	
	//Trasient
	public Float getTotalPrecioVenta() {
		return totalPrecioVenta;
	}

	public void setTotalPrecioVenta(Float totalPrecioVenta) {
		this.totalPrecioVenta = totalPrecioVenta;
	}

	public Float getTotalFlete() {
		return totalFlete;
	}

	public void setTotalFlete(Float totalFlete) {
		this.totalFlete = totalFlete;
	}
	
	public Float getTotalCotizacion() {
		return totalCotizacion;
	}

	public void setTotalCotizacion(Float totalCotizacion) {
		this.totalCotizacion = totalCotizacion;
	}
	
	public Float getTotalFleteCalculado() {
		return totalFleteCalculado;
	}

	public void setTotalFleteCalculado(Float totalFleteCalculado) {
		this.totalFleteCalculado = totalFleteCalculado;
	}

	/**EVENTOS*/
	@PrePersist
	public void prePersist(){
		if(this.historicoMoneda!=null && this.historicoMoneda.getIdHistoria()==null)
			this.historicoMoneda = null;
	}

	@PreUpdate
	public void preUpdate(){
		if(this.historicoMoneda!=null && this.historicoMoneda.getIdHistoria()==null)
			this.historicoMoneda = null;
	}
}
