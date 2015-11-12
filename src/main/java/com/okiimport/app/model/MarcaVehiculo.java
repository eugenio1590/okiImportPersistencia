package com.okiimport.app.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.model.AbstractEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * The persistent class for the marca_vehiculo database table.
 * 
 */
@Entity
@Table(name="marca_vehiculo")
@NamedQuery(name="MarcaVehiculo.findAll", query="SELECT m FROM MarcaVehiculo m")
@JsonIgnoreProperties({"requerimientos", "proveedores"})
public class MarcaVehiculo extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="marca_vehiculo_id_seq")
	@SequenceGenerator(name="marca_vehiculo_id_seq", sequenceName="marca_vehiculo_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_marca_vehiculo")
	private Integer idMarcaVehiculo;

	private String nombre;
	
	@Enumerated(EnumType.STRING)
	private EEstatusGeneral estatus;
	
	//bi-directional one-to-many association to Requerimiento
	@OneToMany(mappedBy="marcaVehiculo", fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Requerimiento> requerimientos;
	
	//bi-directional many-to-many association to Proveedor
	@ManyToMany(mappedBy="marcaVehiculos", fetch=FetchType.LAZY)
	private List<Proveedor> proveedores;

	public MarcaVehiculo() {
		proveedores=new ArrayList<Proveedor>();
	}
	
	public MarcaVehiculo(String nombre){
		this.nombre = nombre;
		proveedores=new ArrayList<Proveedor>();
	}

	public Integer getIdMarcaVehiculo() {
		return this.idMarcaVehiculo;
	}

	public void setIdMarcaVehiculo(Integer idMarcaVehiculo) {
		this.idMarcaVehiculo = idMarcaVehiculo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Requerimiento> getRequerimientos() {
		return this.requerimientos;
	}

	public void setRequerimientos(List<Requerimiento> requerimientos) {
		this.requerimientos = requerimientos;
	}

	public Requerimiento addRequerimiento(Requerimiento requerimiento) {
		getRequerimientos().add(requerimiento);
		requerimiento.setMarcaVehiculo(this);

		return requerimiento;
	}

	public Requerimiento removeRequerimiento(Requerimiento requerimiento) {
		getRequerimientos().remove(requerimiento);
		requerimiento.setMarcaVehiculo(null);

		return requerimiento;
	}
	
	public EEstatusGeneral getEstatus() {
		return estatus;
	}

	public void setEstatus(EEstatusGeneral estatus) {
		this.estatus = estatus;
	}

	public List<Proveedor> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<Proveedor> proveedores) {
		this.proveedores = proveedores;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	public static Comparator<MarcaVehiculo> getComparator(){
		return new Comparator<MarcaVehiculo>(){
			public int compare(MarcaVehiculo marca1, MarcaVehiculo marca2) {
				return marca1.getIdMarcaVehiculo().compareTo(marca2.getIdMarcaVehiculo());
			}
		};
	}
	
}