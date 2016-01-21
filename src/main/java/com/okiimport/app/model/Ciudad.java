package com.okiimport.app.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.okiimport.app.resource.model.AbstractEntity;

import java.util.List;


/**
 * The persistent class for the ciudad database table.
 * 
 */
@Entity
@Table(name="ciudad")
@NamedQuery(name="Ciudad.findAll", query="SELECT c FROM Ciudad c")
@JsonIgnoreProperties({"personas"})
public class Ciudad extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_ciudad")
	private Integer idCiudad;

	@Column(name="nombre")
	private String nombre;

	//bi-directional many-to-one association to Estado
	@ManyToOne()
	@JoinColumn(name="id_estado")
	private Estado estado;

	//bi-directional one-to-many association to Persona
	@OneToMany(mappedBy="ciudad", cascade=CascadeType.ALL)
	private List<Persona> personas;

	public Ciudad() {
	}
	
	public Ciudad(String nombre, Estado estado){
		this.nombre = nombre;
		this.estado = estado;
	}

	public Integer getIdCiudad() {
		return this.idCiudad;
	}

	public void setIdCiudad(Integer idCiudad) {
		this.idCiudad = idCiudad;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	public Persona addPersona(Persona persona) {
		getPersonas().add(persona);
		persona.setCiudad(this);

		return persona;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setCiudad(null);

		return persona;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	@Transient
	public String ubicacion(String separador){
		StringBuilder ubicacion = new StringBuilder(getNombre());
		if(separador!=null)
			ubicacion.append(separador).append(getEstado().getNombre());
		return ubicacion.toString();
	}
}