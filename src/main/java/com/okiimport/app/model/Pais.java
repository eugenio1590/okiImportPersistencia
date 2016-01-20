package com.okiimport.app.model;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The persistent class for the pais database table.
 * 
 */
@Entity
@Table(name="pais")
@NamedQuery(name="Pais.findAll", query="SELECT p FROM Pais p")
@JsonIgnoreProperties({"proveedors"})
public class Pais {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pais_id_seq")
	@SequenceGenerator(name="pais_id_seq", sequenceName="pais_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_pais")
	private Integer idPais;
	
	private String nombre;
	
	//bi-directional many-to-one association to Moneda
	@ManyToOne
	@JoinColumn(name="id_moneda")
	private Moneda moneda;
	
	//bi-directional one-to-many association to PagoCompra
	@OneToMany(mappedBy="pais", fetch=FetchType.LAZY)
	private List<Proveedor> proveedors;

	public Pais() {
	}
	
	public Pais(String nombre){
		this.nombre = nombre;
	}

	public Integer getIdPais() {
		return idPais;
	}

	public void setIdPais(Integer idPais) {
		this.idPais = idPais;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public List<Proveedor> getProveedors() {
		return proveedors;
	}

	public void setProveedors(List<Proveedor> proveedors) {
		this.proveedors = proveedors;
	}
	
	public Proveedor addProveedor(Proveedor proveedor){
		getProveedors().add(proveedor);
		proveedor.setPais(this);
		
		return proveedor;
	}
	
	public Proveedor removeProveedor(Proveedor proveedor){
		getProveedors().remove(proveedor);
		proveedor.setPais(null);
		
		return proveedor;
	}

}
