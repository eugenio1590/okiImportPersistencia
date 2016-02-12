package com.okiimport.app.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
import com.okiimport.app.model.enumerados.EEstatusOrdenCompra;
import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the orden_compra database table.
 * 
 */
@Entity
@Table(name="orden_compra")
@NamedQuery(name="OrdenCompra.findAll", query="SELECT o FROM OrdenCompra o")
@JsonIgnoreProperties({"detalleOfertas"})
public class OrdenCompra extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="orden_compra_id_seq")
	@SequenceGenerator(name="orden_compra_id_seq", sequenceName="orden_compra_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_orden_compra")
	private Integer idOrdenCompra;
	
	private String observacion;
	
	private Float iva; //Preguntar
	
	@Enumerated(EnumType.STRING)
	private EEstatusOrdenCompra estatus;
	
	//bi-directional many-to-one association to PagoProveedor
	@ManyToOne
	@JoinColumn(name="id_pago_proveedor")
	private PagoProveedor pagoProveedor;
	
	//bi-directional one-to-many association to DetalleOferta
	@OneToMany(mappedBy="ordenCompra", fetch=FetchType.LAZY)
	private List<DetalleOferta> detalleOfertas;
	
	@Transient
	private Proveedor proveedor;

	public OrdenCompra() {
	}
	
	public Integer getIdOrdenCompra() {
		return idOrdenCompra;
	}
	
	public void setIdOrdenCompra(Integer idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Float getIva() {
		return iva;
	}

	public void setIva(Float iva) {
		this.iva = iva;
	}

	public PagoProveedor getPagoProveedor() {
		return pagoProveedor;
	}

	public void setPagoProveedor(PagoProveedor pagoProveedor) {
		this.pagoProveedor = pagoProveedor;
	}

	public List<DetalleOferta> getDetalleOfertas() {
		return detalleOfertas;
	}

	public void setDetalleOfertas(List<DetalleOferta> detalleOfertas) {
		this.detalleOfertas = detalleOfertas;
	}
	
	public DetalleOferta addDetalleOferta(DetalleOferta detalleOferta){
		getDetalleOfertas().add(detalleOferta);
		detalleOferta.setOrdenCompra(this);
		
		return detalleOferta;
	}
	
	public DetalleOferta removeDetalleOferta(DetalleOferta detalleOferta){
		getDetalleOfertas().remove(detalleOferta);
		detalleOferta.setOrdenCompra(null);
		
		return detalleOferta;
	}
	
	//Transient
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	@Transient
	public Float total(){
		return new Float(0);
	}

}
