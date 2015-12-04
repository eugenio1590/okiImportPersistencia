package com.okiimport.app.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.factory.persona.EstatusPersonaFactory;
import com.okiimport.app.model.factory.persona.EstatusPersonaFactory.IEstatusPersona;
import com.okiimport.app.resource.model.AbstractEntity;


/**
 * The persistent class for the persona database table.
 * 
 */
@Entity
@Table(name="persona")
@NamedQuery(name="Persona.findAll", query="SELECT p FROM Persona p")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="person_type")
@JsonIgnoreProperties({"tipoMenu", "usuario", "pagoCompras"})
public abstract class Persona extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="persona_id_seq")
	@SequenceGenerator(name="persona_id_seq", sequenceName="persona_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id", unique=true, nullable=false)
	protected Integer id;

	protected String apellido;

	@Column(name="cedula", nullable=false)
	protected String cedula;

	protected String correo;

	protected String direccion;

	protected String nombre;
	
	protected String telefono;
	
	@Column(name="tipo_menu")
	protected Integer tipoMenu;
	
	@Column(length=50)
	protected String estatus;
	
	@Transient
	protected IEstatusPersona iEstatus;
	
	@Transient
	protected EstatusPersonaFactory factoryEstatus;
	
	//bi-directional one-to-one association to Usuario (Relacion Poliformica)
	@OneToOne(mappedBy="persona")
	protected Usuario usuario;
	
	//bi-directional many-to-one association to Ciudad
	@ManyToOne
	@JoinColumn(name="id_ciudad")
	protected Ciudad ciudad;
	
	//bi-directional many-to-one association to PagoCompra
	@OneToMany(mappedBy="persona", fetch=FetchType.LAZY)
	private List<PagoCompra> pagoCompras;

	public Persona() {
	}
	
	public Persona(Persona persona){
		this(persona.getId(), persona.getApellido(), persona.getCedula(), persona.getCorreo(), persona.getDireccion(), 
				persona.getNombre(), persona.getTelefono(), persona.getUsuario(), persona.getEstatus());
	}

	public Persona(Integer id, String apellido, String cedula, String correo,
			String direccion, String nombre, String telefono, Usuario usuario, String estatus) {
		super();
		this.id = id;
		this.apellido = apellido;
		this.cedula = cedula;
		this.correo = correo;
		this.direccion = direccion;
		this.nombre = nombre;
		this.telefono = telefono;
		this.usuario = usuario;
		this.estatus = estatus;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}
	
	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public IEstatusPersona getiEstatus() {
		return iEstatus;
	}

	public void setiEstatus(IEstatusPersona iEstatus) {
		this.iEstatus = iEstatus;
		this.estatus = iEstatus.getValue();
	}

	public List<PagoCompra> getPagoCompras() {
		return pagoCompras;
	}

	public void setPagoCompras(List<PagoCompra> pagoCompras) {
		this.pagoCompras = pagoCompras;
	}
	
	public PagoCompra addPagoCompra(PagoCompra pagoCompra){
		getPagoCompras().add(pagoCompra);
		pagoCompra.setPersona(this);
		
		return pagoCompra;
	}
	
	public PagoCompra removePagoCompra(PagoCompra pagoCompra){
		getPagoCompras().remove(pagoCompra);
		pagoCompra.setPersona(null);
		
		return pagoCompra;
	}

	/**METODOS PROPIOS DE LA CLASE*/
	/**METODOS ESTATICOS DE LA CLASE*/
	public static Persona getNewInstance(){
		return new Persona() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getTipoMenu() {
				return null;
			}

			@Override
			public void postLoad(String estatus) {
				
			}
		};
	}
	
	public static <T extends Persona> Comparator<T> getComparator(){
		return new Comparator<T>(){
			public int compare(T pers1, T pers2) {
				return pers1.getId().compareTo(pers2.getId());
			}
		};
	}
	
	/**METODOS ABSTRACTOS DE LA CLASE*/
	public abstract Integer getTipoMenu();
	public abstract void postLoad(String estatus);
	
	/**EVENTOS*/
	@PostLoad
	public void postLoad(){
		postLoad(estatus);
	}
}