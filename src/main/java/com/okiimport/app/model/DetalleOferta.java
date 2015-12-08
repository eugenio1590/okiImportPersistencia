package com.okiimport.app.model;

import java.io.Serializable;

import javax.persistence.*;

import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the detalle_oferta database table.
 * 
 */
@Entity
@Table(name="detalle_oferta")
@NamedQuery(name="DetalleOferta.findAll", query="SELECT d FROM DetalleOferta d")
public class DetalleOferta extends AbstractEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="detalle_oferta_id_seq")
	@SequenceGenerator(name="detalle_oferta_id_seq", sequenceName="detalle_oferta_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_detalle_oferta")
	private Integer idDetalleOferta;
	
	@Enumerated(EnumType.STRING)
	private String estatus;
	
	@Transient
	private Boolean aprobado = false;
		
	//bi-directional many-to-one association to Oferta
	@ManyToOne
	@JoinColumn(name="id_oferta")
	private Oferta oferta;
	
	//bi-directional many-to-one association to Compra
	@ManyToOne
	@JoinColumn(name="id_compra")
	private Compra compra;
	
	//bi-directional many-to-one association to DetalleCotizacion
	@ManyToOne
	@JoinColumn(name="id_detalle_cotizacion")
	private DetalleCotizacion detalleCotizacion;

	public DetalleOferta() {
	}

	public Integer getIdDetalleOferta() {
		return idDetalleOferta;
	}

	public void setIdDetalleOferta(Integer idDetalleOferta) {
		this.idDetalleOferta = idDetalleOferta;
	}
	
	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Boolean getAprobado() {
		return aprobado;
	}

	public void setAprobado(Boolean aprobado) {
		this.aprobado = aprobado;
	}
	
	public Oferta getOferta() {
		return oferta;
	}

	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}
	
	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public DetalleCotizacion getDetalleCotizacion() {
		return detalleCotizacion;
	}

	public void setDetalleCotizacion(DetalleCotizacion detalleCotizacion) {
		this.detalleCotizacion = detalleCotizacion;
	}
	
	/**METODOS OVERRIDE*/
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DetalleOferta)
			return (
					getIdDetalleOferta().equals(((DetalleOferta) obj).getIdDetalleOferta())
					|| getDetalleCotizacion().getDetalleRequerimiento().getIdDetalleRequerimiento()
						.equals(((DetalleOferta) obj).getDetalleCotizacion().getDetalleRequerimiento().getIdDetalleRequerimiento())
			);
		else
			return super.equals(obj);
	}

	/**METODOS PROPIOS DE LA CLASE*/
	public Float calcularPrecioVenta(){
		Float costo = this.detalleCotizacion.calcularTotal();
		Float porctGanancia = this.oferta.getPorctGanancia();
//		Float costo = (detalleCotizacion instanceof DetalleCotizacion) 
//				? this.detalleCotizacion.calcularTotal() : 
//					((DetalleCotizacionInternacional) this.detalleCotizacion).calcularTotal();
		return (porctGanancia!=0) ? (costo*(1+this.oferta.getPorctIva()))/porctGanancia : new Float(0);
	}
}
