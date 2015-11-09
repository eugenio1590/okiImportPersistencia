package com.okiimport.app.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.okiimport.app.resource.model.AbstractEntity;
import com.okiimport.app.resource.model.JsonDateSerializer;


/**
 * The persistent class for the requerimiento database table.
 * 
 */
@Entity
@Table(name="requerimiento")
@NamedQuery(name="Requerimiento.findAll", query="SELECT r FROM Requerimiento r")
@JsonIgnoreProperties({"detalleRequerimientos", "compras"})
public class Requerimiento extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="requerimiento_id_seq")
	@SequenceGenerator(name="requerimiento_id_seq", sequenceName="requerimiento_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_requerimiento")
	private Integer idRequerimiento;

	@Column(name="anno_v")
	private Integer annoV;

	private String estatus;

	@Column(name="fecha_cierre", columnDefinition="date")
	private Date fechaCierre;

	@Column(name="fecha_creacion", columnDefinition="date")
	private Timestamp fechaCreacion;
	
	@Column(name="fecha_solicitud")
	private Timestamp fechaSolicitud;

	@Column(name="fecha_vencimiento", columnDefinition="date")
	private Date fechaVencimiento;

	@Column(name="modelo_v")
	private String modeloV;

	@Column(name="serial_carroceria_v")
	private String serialCarroceriaV;

	@Column(name="transmision_v")
	private Boolean transmisionV;
	
	@Column(name="traccion_v")
	private Boolean traccionV;
	
	@Column(name="tipo_repuesto")
	private Boolean tipoRepuesto;
	
	@Transient
	private Integer nroOfertas;
	
	//bi-directional many-to-one association to Analista
	@ManyToOne
	@JoinColumn(name="id_analista")
	private Analista analista;
	
	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	//bi-directional many-to-one association to MarcaVehiculo
	@ManyToOne
	@JoinColumn(name="id_marca_v")
	private MarcaVehiculo marcaVehiculo;

	//bi-directional many-to-one association to Motor
	@ManyToOne
	@JoinColumn(name="id_motor_v")
	private Motor motor;
	
	//bi-directional one-to-many association to DetalleRequerimiento
	@OneToMany(mappedBy="requerimiento", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<DetalleRequerimiento> detalleRequerimientos;
	
	//bi-directional one-to-many association to Compra
	@OneToMany(mappedBy="requerimiento", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Compra> compras;

	public Requerimiento() {
		detalleRequerimientos = new ArrayList<DetalleRequerimiento>();
	}
	
	public Requerimiento(Integer idRequerimiento){
		this.idRequerimiento = idRequerimiento;
	}
	
	public Requerimiento(Cliente cliente){
		this.cliente = cliente;
	}
	
	public Requerimiento(MarcaVehiculo marcaVehiculo){
		this.marcaVehiculo = marcaVehiculo;
	}
	
	public Requerimiento(Cliente cliente, MarcaVehiculo marcaVehiculo){
		this.cliente = cliente;
		this.marcaVehiculo = marcaVehiculo;
	}

	public Requerimiento(Integer idRequerimiento, String estatus, Date fechaCreacion, Date fechaVencimiento,
			String modeloV, Boolean tipoRepuesto, Analista analista, Cliente cliente, MarcaVehiculo marcaVehiculo, Motor motor) {
		super();
		this.idRequerimiento = idRequerimiento;
		this.estatus = estatus;
		this.fechaCreacion = new Timestamp(fechaCreacion.getTime());
		this.fechaVencimiento = fechaVencimiento;
		this.modeloV = modeloV;
		this.tipoRepuesto = tipoRepuesto;
		this.analista = analista;
		this.cliente = cliente;
		this.marcaVehiculo = marcaVehiculo;
		this.motor = motor;
	}

	public Integer getIdRequerimiento() {
		return this.idRequerimiento;
	}

	public void setIdRequerimiento(Integer idRequerimiento) {
		this.idRequerimiento = idRequerimiento;
	}

	public Integer getAnnoV() {
		return this.annoV;
	}

	public void setAnnoV(Integer annoV) {
		this.annoV = annoV;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Timestamp getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Timestamp getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Timestamp fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getFechaVencimiento() {
		return this.fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getModeloV() {
		return this.modeloV;
	}

	public void setModeloV(String modeloV) {
		this.modeloV = modeloV;
	}

	public String getSerialCarroceriaV() {
		return this.serialCarroceriaV;
	}

	public void setSerialCarroceriaV(String serialCarroceriaV) {
		this.serialCarroceriaV = serialCarroceriaV;
	}

	public Boolean getTransmisionV() {
		return this.transmisionV;
	}

	public Analista getAnalista() {
		return analista;
	}

	public void setAnalista(Analista analista) {
		this.analista = analista;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setTransmisionV(Boolean transmisionV) {
		this.transmisionV = transmisionV;
	}

	public MarcaVehiculo getMarcaVehiculo() {
		return this.marcaVehiculo;
	}

	public Boolean getTraccionV() {
		return traccionV;
	}

	public void setTraccionV(Boolean traccionV) {
		this.traccionV = traccionV;
	}

	public Boolean getTipoRepuesto() {
		return tipoRepuesto;
	}

	public void setTipoRepuesto(Boolean tipoRepuesto) {
		this.tipoRepuesto = tipoRepuesto;
	}

	public void setMarcaVehiculo(MarcaVehiculo marcaVehiculo) {
		this.marcaVehiculo = marcaVehiculo;
	}

	public Motor getMotor() {
		return this.motor;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
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
		detalleRequerimiento.setRequerimiento(this);

		return detalleRequerimiento;
	}

	public DetalleRequerimiento removeDetalleRequerimiento(DetalleRequerimiento detalleRequerimiento) {
		getDetalleRequerimientos().remove(detalleRequerimiento);
		detalleRequerimiento.setRequerimiento(null);

		return detalleRequerimiento;
	}
	
	public void removeAllDetalleRequerimiento(){
		for(DetalleRequerimiento detalle : detalleRequerimientos)
			detalle.setRequerimiento(null);
		detalleRequerimientos.clear();
	}
	
	public List<Compra> getCompras() {
		return compras;
	}

	public void setCompras(List<Compra> compras) {
		this.compras = compras;
	}
	
	public Compra addCompra(Compra compra){
		getCompras().add(compra);
		compra.setRequerimiento(this);
		
		return compra;
	}
	
	public Compra removeCompra(Compra compra){
		getCompras().remove(compra);
		compra.setRequerimiento(null);
		
		return compra;
	}

	public Integer getNroOfertas() {
		return nroOfertas;
	}

	public void setNroOfertas(Integer nroOfertas) {
		this.nroOfertas = nroOfertas;
	}

	/**METODOS PROPIOS DE LA CLASE*/
	@Transient
	public static Comparator<Requerimiento> getComparator(){
		return new Comparator<Requerimiento>(){
			public int compare(Requerimiento requer1, Requerimiento requer2) {
				return requer1.getIdRequerimiento().compareTo(requer2.getIdRequerimiento());
			}
		};
	}
	
	public String determinarTransmision(){
		String texto = null;
		if(transmisionV!=null)
			texto = (transmisionV) ? "Automatico" : "Sincronico";
		return texto;
	}
	
	public String determinarTraccion(){
		String texto = null;
		if(traccionV!=null)
			texto = (traccionV) ? "4x2" : "4x4";
		return texto;
	}
	
	public String determinarTipoRepuesto(){
		String texto = null;
		if(tipoRepuesto!=null)
			texto = (tipoRepuesto) ? "Reemplazo" : "Original";
		else
			texto = "Indistinto";
		return texto;
	}
	
	public String determinarEstatus(){
		if(this.estatus.equalsIgnoreCase("CR"))
			return "Emitido";
		else if(this.estatus.equalsIgnoreCase("E"))
			return "Recibido y Editado";
		else if(this.estatus.equalsIgnoreCase("EP"))
			return "Enviado a Proveedores";
		else if(this.estatus.equalsIgnoreCase("CT"))
			return "Con Cotizaciones Asignadas";
		else if(this.estatus.equalsIgnoreCase("EC"))
			return "Con Cotizaciones Incompletas";
		else if(this.estatus.equalsIgnoreCase("O"))
			return "Ofertado";
		else if(this.estatus.equalsIgnoreCase("CC"))
			return "Concretado";
		else if(this.estatus.equalsIgnoreCase("CP"))
			return "Concretado y Enviado a Proveedores";
		else if(this.estatus.equalsIgnoreCase("C"))
			return "Cerrado";
		return "";
	}

	public void especificarInformacionVehiculo(){
		String especificacion = "No Especificado";
		if (this.marcaVehiculo==null) 
			this.marcaVehiculo = new MarcaVehiculo(especificacion);
		
		if(this.modeloV==null)
			this.modeloV = especificacion;
		
		if(this.motor==null)
			this.motor = new Motor(especificacion);
	}
	
	public boolean editar(){
		return (this.estatus.equalsIgnoreCase("CR") || this.estatus.equalsIgnoreCase("E")) ? true : false;
	}
	
	public boolean cotizar(){
		return (this.estatus.equalsIgnoreCase("EC") || this.estatus.equalsIgnoreCase("EP") || this.estatus.equalsIgnoreCase("CT"));
	}
	
	public boolean editarCotizacion(){
		return this.estatus.equalsIgnoreCase("EC");
	}
	
	public boolean verOfertas(){
		return this.estatus.equalsIgnoreCase("O");
	}
	
	public boolean seleccionarCotizacion(){
		if(this.nroOfertas!=null && this.nroOfertas<3)
			return true;
		return false;
	}
	
	public void cerrarSolicitud(){
		this.setEstatus("C");
	}
	
	@Transient
	public boolean isCerrarSolicitud(){
		boolean solicitud = false;
		if(this.estatus.equalsIgnoreCase("C"))
			solicitud = true;
		else if(this.fechaSolicitud!=null)
			solicitud = (diferenciaHoras(fechaSolicitud, Calendar.getInstance().getTime()) >= 48) 
					? true : false;
		return solicitud;
	}	
}
