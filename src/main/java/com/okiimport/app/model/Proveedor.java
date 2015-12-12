package com.okiimport.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.factory.persona.EstatusPersonaFactory.IEstatusPersona;
import com.okiimport.app.model.factory.persona.EstatusProveedorFactory;


/**
 * The persistent class for the proveedor database table.
 * 
 */
@Entity
@Table(name="proveedor")
@NamedQuery(name="Proveedor.findAll", query="SELECT p FROM Proveedor p")
@PrimaryKeyJoinColumn(name="id_proveedor")
@JsonIgnoreProperties({"tipoMenu", "cotizacions", "marcaVehiculos", "clasificacionRepuestos"})
public class Proveedor extends Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="tipo_proveedor")
	private Boolean tipoProveedor;
	
	//bi-directional one-to-many association to Pais
	@ManyToOne
	@JoinColumn(name="id_pais")
	private Pais pais;
	
	//bi-directional one-to-many association to Cotizacion
	@OneToMany(mappedBy="proveedor", fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Cotizacion> cotizacions;
		
	//bi-directional many-to-many association to MarcaRepuesto
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
		name="proveedor_marca_vehiculo"
		, joinColumns={
				@JoinColumn(name="id_proveedor")
		}
		, inverseJoinColumns={
				@JoinColumn(name="id_marca_vehiculo")
		}
	)
	private List<MarcaVehiculo> marcaVehiculos;


	//bi-directional many-to-many association to MarcaRepuesto
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
		name="proveedor_clasificacion_repuesto"
		, joinColumns={
				@JoinColumn(name="id_proveedor")
		}
		, inverseJoinColumns={
				@JoinColumn(name="id_clasificacion_repuesto")
		}
	)
	private List<ClasificacionRepuesto> clasificacionRepuestos;
	
	@Transient
	private String ubicacion;
		
	public Proveedor() {
		marcaVehiculos = new ArrayList<MarcaVehiculo>();
		clasificacionRepuestos = new ArrayList<ClasificacionRepuesto>();
	}
	
	public Proveedor(Persona persona) {
		super(persona);
		marcaVehiculos = new ArrayList<MarcaVehiculo>();
		clasificacionRepuestos = new ArrayList<ClasificacionRepuesto>();
	}
	
	public Proveedor(String cedula) {
		super.cedula = cedula;
		marcaVehiculos = new ArrayList<MarcaVehiculo>();
		clasificacionRepuestos = new ArrayList<ClasificacionRepuesto>();
	}
	
	public Proveedor(Integer id,String cedula, String correo, String direccion, 
			String nombre, String telefono, String estatus, Boolean tipoProveedor) {
		super(id, null, cedula, correo, direccion, nombre, telefono, null, estatus);
		this.tipoProveedor = tipoProveedor;
	}

	public Boolean getTipoProveedor() {
		return tipoProveedor;
	}

	public void setTipoProveedor(Boolean tipoProveedor) {
		this.tipoProveedor = tipoProveedor;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public List<Cotizacion> getCotizacions() {
		return cotizacions;
	}

	public void setCotizacions(List<Cotizacion> cotizacions) {
		this.cotizacions = cotizacions;
	}
	
	public Cotizacion addCotizacion(Cotizacion cotizacion) {
		getCotizacions().add(cotizacion);
		cotizacion.setProveedor(this);

		return cotizacion;
	}

	public Cotizacion removeDetalleRequerimiento(Cotizacion cotizacion) {
		getCotizacions().remove(cotizacion);
		cotizacion.setProveedor(null);

		return cotizacion;
	}

	public List<MarcaVehiculo> getMarcaVehiculos() {
		return marcaVehiculos;
	}

	public void setMarcaVehiculos(List<MarcaVehiculo> marcaVehiculos) {
		this.marcaVehiculos = marcaVehiculos;
	}

	public List<ClasificacionRepuesto> getClasificacionRepuestos() {
		return clasificacionRepuestos;
	}

	public void setClasificacionRepuestos(
			List<ClasificacionRepuesto> clasificacionRepuestos) {
		this.clasificacionRepuestos = clasificacionRepuestos;
	}
	
	//Transient
	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	/**METODOS OVERRIDE*/
	@Override
	public Integer getTipoMenu() {
		return this.tipoMenu=3;
	}
	
	@Override
	public void postLoad(String estatus) {
		factoryEstatus = new EstatusProveedorFactory();
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	public String ubicacion(String separador){
		StringBuilder ubicacion = new StringBuilder("");
		if(ciudad!=null && isNacional()){
			ubicacion.append(ciudad.getEstado().getNombre());
			ubicacion.append(separador).append(ciudad.getNombre());
		}
		else
			ubicacion.append(this.pais.getNombre());
		return ubicacion.toString();
	}
	
	@Transient
	public boolean isNacional(){
		if(this.tipoProveedor!=null)
			return this.tipoProveedor;
		else if(this.getPais()!=null)
			return this.getPais().getNombre().equalsIgnoreCase("Venezuela");
		else
			return false;
	}
	
	@Transient
	@SuppressWarnings("static-access")
	public boolean isSolicitante(){
		IEstatusPersona solicitante = ((EstatusProveedorFactory) factoryEstatus).getEstatusSolicitante();
		return this.estatus.equals(solicitante.getValue());
	}
}