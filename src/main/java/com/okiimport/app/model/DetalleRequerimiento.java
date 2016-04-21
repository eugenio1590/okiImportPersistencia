package com.okiimport.app.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.enumerados.EEstatusDetalleRequerimiento;
import com.okiimport.app.resource.model.AbstractEntity;


/**
 * The persistent class for the detalle_requerimiento database table.
 * 
 */
@Entity
@Table(name="detalle_requerimiento")
@NamedQuery(name="DetalleRequerimiento.findAll", query="SELECT d FROM DetalleRequerimiento d")
@JsonIgnoreProperties({"detalleCotizacions", "foto"})
public class DetalleRequerimiento extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="detalle_requerimiento_id_seq")
	@SequenceGenerator(name="detalle_requerimiento_id_seq", sequenceName="detalle_requerimiento_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_detalle_requerimiento")
	private Integer idDetalleRequerimiento;

	private Long cantidad;

	@Column(name="codigo_oem")
	private String codigoOem;

	private EEstatusDetalleRequerimiento estatus;

	private byte[] foto;
	
	private String descripcion;
	
	private Float peso = new Float(0);

	//bi-directional many-to-one association to ClasificacionRepuesto
	@ManyToOne
	@JoinColumn(name="id_clasificacion_repuesto")
	private ClasificacionRepuesto clasificacionRepuesto;

	//bi-directional many-to-one association to Requerimiento
	@ManyToOne
	@JoinColumn(name="id_requerimiento")
	private Requerimiento requerimiento;
	
	//bi-directional one-to-many association to DetalleCotizacion
	@OneToMany(mappedBy="detalleRequerimiento", fetch=FetchType.LAZY)
			/*orphanRemoval=true, 
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REFRESH})*/
	private List<DetalleCotizacion> detalleCotizacions;

	public DetalleRequerimiento() {
	}

	public Integer getIdDetalleRequerimiento() {
		return this.idDetalleRequerimiento;
	}

	public void setIdDetalleRequerimiento(Integer idDetalleRequerimiento) {
		this.idDetalleRequerimiento = idDetalleRequerimiento;
	}

	public Long getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	public String getCodigoOem() {
		return this.codigoOem;
	}

	public void setCodigoOem(String codigoOem) {
		this.codigoOem = codigoOem;
	}

	public EEstatusDetalleRequerimiento getEstatus() {
		return estatus;
	}

	public void setEstatus(EEstatusDetalleRequerimiento estatus) {
		this.estatus = estatus;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Float getPeso() {
		return peso;
	}

	public void setPeso(Float peso) {
		this.peso = peso;
	}

	public ClasificacionRepuesto getClasificacionRepuesto() {
		return clasificacionRepuesto;
	}

	public void setClasificacionRepuesto(ClasificacionRepuesto clasificacionRepuesto) {
		this.clasificacionRepuesto = clasificacionRepuesto;
	}

	public Requerimiento getRequerimiento() {
		return requerimiento;
	}

	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}

	public List<DetalleCotizacion> getDetalleCotizacions() {
		return detalleCotizacions;
	}

	public void setDetalleCotizacions(List<DetalleCotizacion> detalleCotizacions) {
		this.detalleCotizacions = detalleCotizacions;
	}
	
	/**METODOS OVERRIDE*/
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DetalleRequerimiento && this.idDetalleRequerimiento != null)
			return this.getIdDetalleRequerimiento().equals(((DetalleRequerimiento) obj).getIdDetalleRequerimiento());
		else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return (this.idDetalleRequerimiento == null) ? super.hashCode() : this.idDetalleRequerimiento;
	}

	/**METODOS PROPIOS DE LA CLASE*/
	@Transient
	public boolean fotoVacia(){
		return (foto==null);
	}

	@Transient
	public String getFoto64(){
		return decodificarImagen(foto);
	}
}