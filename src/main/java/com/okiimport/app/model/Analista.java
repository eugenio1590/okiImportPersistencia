package com.okiimport.app.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.factory.persona.EstatusAnalistaFactory;
import com.okiimport.app.model.factory.persona.EstatusProveedorFactory;
import com.okiimport.app.model.factory.persona.EstatusPersonaFactory.IEstatusPersona;

/**
 * The persistent class for the analista database table.
 * 
 */
@Entity
@NamedQuery(name="Analista.findAll", query="SELECT a FROM Analista a")
@PrimaryKeyJoinColumn(name="id_analista")
@JsonIgnoreProperties({"tipoMenu", "requerimientos", "usuario", "pagoCompras"})
public class Analista extends Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column
	private Boolean administrador;
	
	@Transient
	private Long cantRequerimientos; 
	
	//bi-directional one-to-many association to Requerimiento
	@OneToMany(mappedBy="analista", fetch=FetchType.LAZY)
	private List<Requerimiento> requerimientos;

	public Analista() {
		this.ciudad=new Ciudad();
	}
	
	public Analista(Persona persona) {
		super(persona);
	}

	public Boolean getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

	public Analista(Long cantRequerimientos, Integer id) {
		super();
		this.cantRequerimientos = cantRequerimientos;
		this.id = id;
	}
	
	public List<Requerimiento> getRequerimientos() {
		return requerimientos;
	}

	public void setRequerimientos(List<Requerimiento> requerimientos) {
		this.requerimientos = requerimientos;
	}
	
	public Requerimiento addRequerimiento(Requerimiento requerimiento) {
		getRequerimientos().add(requerimiento);
		requerimiento.setAnalista(this);

		return requerimiento;
	}

	public Requerimiento removeRequerimiento(Requerimiento requerimiento) {
		getRequerimientos().remove(requerimiento);
		requerimiento.setAnalista(null);

		return requerimiento;
	}

	//@Transient
	public Long getCantRequerimientos() {
		return cantRequerimientos;
	}

	public void setCantRequerimientos(Long cantRequerimientos) {
		this.cantRequerimientos = cantRequerimientos;
	}

	/**METODOS OVERRIDE*/
	@Override
	public Integer getTipoMenu() {
		if(this.administrador)
			this.tipoMenu=1;
		else
			this.tipoMenu=2;
		return this.tipoMenu;
	}
	
	@Override
	public void postLoad(String estatus) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	@SuppressWarnings("static-access")
	public boolean isEliminar() {
		IEstatusPersona eliminado = ((EstatusAnalistaFactory) factoryEstatus).getEstatusInactivo();
		return this.estatus.equals(eliminado.getValue());
	}
	
	
	
}