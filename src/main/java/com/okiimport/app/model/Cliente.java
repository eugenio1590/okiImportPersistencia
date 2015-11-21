package com.okiimport.app.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The persistent class for the cliente database table.
 * 
 */
@Entity
@Table(name="cliente")
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
@PrimaryKeyJoinColumn(name="id_cliente")
@JsonIgnoreProperties({"tipoMenu", "requerimientos", "usuario", "pagoCompras"})
public class Cliente extends Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//bi-directional one-to-many association to Proveedor
	@OneToMany(mappedBy="cliente", fetch=FetchType.LAZY)
	private List<Requerimiento> requerimientos;

	public Cliente() {
	}
	
	public Cliente(Persona persona) {
		super(persona);
	}
	
	public Cliente(String cedula){
		super.cedula = cedula;
	}

	public List<Requerimiento> getRequerimientos() {
		return requerimientos;
	}

	public void setRequerimientos(List<Requerimiento> requerimientos) {
		this.requerimientos = requerimientos;
	}
	
	public Requerimiento addRequerimiento(Requerimiento requerimiento) {
		getRequerimientos().add(requerimiento);
		requerimiento.setCliente(this);

		return requerimiento;
	}

	public Requerimiento removeRequerimiento(Requerimiento requerimiento) {
		getRequerimientos().remove(requerimiento);
		requerimiento.setCliente(null);

		return requerimiento;
	}

	/**METODOS OVERRIDE*/
	@Override
	public Integer getTipoMenu() {
		return null;
	}
	
	@Override
	public void postLoad(String estatus) {
		// TODO Auto-generated method stub
		
	}

}