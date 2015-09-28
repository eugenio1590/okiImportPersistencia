package com.okiimport.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.resource.model.AbstractEntity;


/**
 * The persistent class for the clasificacion_repuesto database table.
 * 
 */
@Entity
@Table(name="clasificacion_repuesto")
@NamedQuery(name="ClasificacionRepuesto.findAll", query="SELECT c FROM ClasificacionRepuesto c")
@JsonIgnoreProperties({"detalleRequerimientos", "proveedores"})
public class ClasificacionRepuesto extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="clasificacion_repuesto_id_seq")
	@SequenceGenerator(name="clasificacion_repuesto_id_seq", sequenceName="clasificacion_repuesto_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_clasificacion_repuesto")
	private Integer idClasificacionRepuesto;

	private String descripcion;
	
	private String estatus;
	
	//bi-directional one-to-many association to DetalleRequerimiento
	@OneToMany(mappedBy="clasificacionRepuesto", fetch=FetchType.LAZY, orphanRemoval=true)
	private List<DetalleRequerimiento> detalleRequerimientos;
	
	//bi-directional many-to-many association to Proveedor
	@ManyToMany(mappedBy="clasificacionRepuestos", fetch=FetchType.LAZY)
	private List<Proveedor> proveedores;

	public ClasificacionRepuesto() {
		proveedores = new ArrayList<Proveedor>();
	}

	public Integer getIdClasificacionRepuesto() {
		return idClasificacionRepuesto;
	}

	public void setIdClasificacionRepuesto(Integer idClasificacionRepuesto) {
		this.idClasificacionRepuesto = idClasificacionRepuesto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public List<DetalleRequerimiento> getDetalleRequerimientos() {
		return detalleRequerimientos;
	}

	public void setDetalleRequerimientos(
			List<DetalleRequerimiento> detalleRequerimientos) {
		this.detalleRequerimientos = detalleRequerimientos;
	}

	public DetalleRequerimiento addDetalleRequerimiento(DetalleRequerimiento detalleRequerimiento) {
		getDetalleRequerimientos().add(detalleRequerimiento);
		detalleRequerimiento.setClasificacionRepuesto(this);

		return detalleRequerimiento;
	}

	public DetalleRequerimiento removeDetalleRequerimiento(DetalleRequerimiento detalleRequerimiento) {
		getDetalleRequerimientos().remove(detalleRequerimiento);
		detalleRequerimiento.setClasificacionRepuesto(null);

		return detalleRequerimiento;
	}

	public List<Proveedor> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<Proveedor> proveedores) {
		this.proveedores = proveedores;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	public static Comparator<ClasificacionRepuesto> getComparator(){
		return new Comparator<ClasificacionRepuesto>(){
			public int compare(ClasificacionRepuesto clasificacion1, ClasificacionRepuesto clasificacion2) {
				return clasificacion1.getIdClasificacionRepuesto().compareTo(clasificacion2.getIdClasificacionRepuesto());
			}
			
		};
	}
}