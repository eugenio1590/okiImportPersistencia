package com.okiimport.app.service.transaccion.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;
import com.okiimport.app.dao.transaccion.*;
import com.okiimport.app.dao.transaccion.detalle.cotizacion.*;
import com.okiimport.app.dao.transaccion.detalle.oferta.DetalleOfertaRepository;
import com.okiimport.app.dao.transaccion.detalle.requerimiento.DetalleRequerimientoRepository;
import com.okiimport.app.dao.transaccion.impl.*;
import com.okiimport.app.dao.transaccion.impl.detalle.cotizacion.*;
import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.DetalleCotizacionInternacional;
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.resource.service.AbstractServiceImpl;


public class STransaccionImpl extends AbstractServiceImpl implements STransaccion {
	
	private static List<String> ESTATUS_EMITIDOS;
	private static List<String> ESTATUS_PROCESADOS;
	private static List<String> ESTATUS_OFERTADOS;
	
	@Autowired
	private CotizacionRepository cotizacionRepository;
	
	@Autowired
	private RequerimientoRepository requerimientoRepository;
	
	@Autowired
	private DetalleRequerimientoRepository detalleRequerimientoRepository;
	
	@Autowired
	private DetalleCotizacionRepository detalleCotizacionRepository;
	
	@Autowired
	private DetalleCotizacionInternacionalRepository detalleCotizacionInternacionalRepository;
	
	@Autowired
	private OfertaRepository ofertaRepository;
	
	@Autowired
	private DetalleOfertaRepository detalleOfertaRepository;
	
	@Autowired
	private CompraRepository compraRepository;

	public STransaccionImpl() {
		super();
		
		ESTATUS_EMITIDOS = new ArrayList<String>();
		ESTATUS_EMITIDOS.add("CR");
		ESTATUS_EMITIDOS.add("E");
		ESTATUS_EMITIDOS.add("EP");
		
		ESTATUS_PROCESADOS = new ArrayList<String>();
		ESTATUS_PROCESADOS.add("CT");
		ESTATUS_PROCESADOS.add("EC");
		
		ESTATUS_OFERTADOS = new ArrayList<String>();
		ESTATUS_OFERTADOS.add("O");
		ESTATUS_OFERTADOS.add("Z");
	}

	public Requerimiento registrarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros) {
		Date fechaCreacion = calendar.getTime();
		Date fechaVencimiento = sumarORestarFDia(fechaCreacion, 15);
		asignarRequerimiento(requerimiento, sMaestros);
		requerimiento.setFechaCreacion(new Timestamp(fechaCreacion.getTime()));
		requerimiento.setFechaVencimiento(fechaVencimiento);
		requerimiento.setEstatus("CR");
		for(DetalleRequerimiento detalle:requerimiento.getDetalleRequerimientos())
			detalle.setEstatus("activo");
		return requerimiento = actualizarRequerimiento(requerimiento);
	}
	
	public Requerimiento actualizarRequerimiento(Requerimiento requerimiento){
		return this.requerimientoRepository.save(requerimiento);
	}
	
	public void guardarSeleccionRequerimiento(List<DetalleCotizacion> detalleCotizaciones) {
		Date fechaCreacion = calendar.getTime();
		Oferta oferta = new Oferta();
		oferta.setFechaCreacion(new Timestamp(fechaCreacion.getTime()));
		oferta = actualizarOferta(oferta);
		for (DetalleCotizacion detalleCotizacion: detalleCotizaciones){
			DetalleOferta detalleOferta = new DetalleOferta();
			detalleOferta.setDetalleCotizacion(detalleCotizacion);
			detalleOferta.setOferta(oferta);
			detalleOferta.setEstatus("seleccion");
			this.detalleOfertaRepository.save(detalleOferta);
		}
	}
	
	public void asignarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros) {
		List<String> estatus=new ArrayList<String>();
		estatus.addAll(ESTATUS_EMITIDOS);
//		estatus.add("CR");
//		estatus.add("E");
//		estatus.add("EP");
		estatus.add("CT");
		estatus.add("O");
		List<Analista> analistas = sMaestros.consultarCantRequerimientos(estatus, 0, 1);
		if(analistas.size()>0)
			requerimiento.setAnalista(analistas.get(0));
	}
	
	public Map<String, Object> consultarRequerimientosGeneral(Requerimiento regFiltro, String fieldSort, Boolean sortDirection,
			int page, int limit){
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Requerimiento> requerimientos = null;
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO()).consultarRequerimientosGeneral(regFiltro, fieldSort, sortDirection);
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository.findAll(specfRequerimiento, new PageRequest(page, limit));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento);
			total = requerimientos.size();
		}
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;
	}
	
	public Map<String, Object> consultarMisRequerimientosEmitidos(Requerimiento regFiltro,  String fieldSort, Boolean sortDirection, 
			Integer idusuario, int page, int limit) {
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Requerimiento> requerimientos = null;
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientoUsuario(regFiltro, fieldSort, sortDirection, idusuario, ESTATUS_EMITIDOS);
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository.findAll(specfRequerimiento, new PageRequest(page, limit));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento);
			total = requerimientos.size();
		}
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;
	}
	
	public Map<String, Object> consultarMisRequerimientosProcesados(Requerimiento regFiltro,  String fieldSort, Boolean sortDirection, 
			Integer idusuario, int page, int limit) {
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Requerimiento> requerimientos = null;
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientoUsuario(regFiltro, fieldSort, sortDirection, idusuario, ESTATUS_PROCESADOS);
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository.findAll(specfRequerimiento, new PageRequest(page, limit));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento);
			total = requerimientos.size();
		}
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;
	}
	
	public Map <String, Object> consultarMisRequerimientosOfertados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, 
			Integer idusuario, int page, int limit){
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0, nroOfertas = 0;
		List<Requerimiento> requerimientos = null;
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientoUsuario(regFiltro, fieldSort, sortDirection, idusuario, ESTATUS_OFERTADOS);
		
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository.findAll(specfRequerimiento, new PageRequest(page, limit));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento);
			total = requerimientos.size();
		}
		
		for(Requerimiento requerimiento : requerimientos){
			nroOfertas = (Integer) this.consultarOfertasPorRequerimiento(requerimiento.getIdRequerimiento(), null, null, 0, 1).get("total");
			requerimiento.setNroOfertas(nroOfertas);
		}
		
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;
	}
	
	public Map<String, Object> ConsultarRequerimientosCliente (Requerimiento regFiltro, String fieldSort, 
			Boolean sortDirection, String cedula, int page, int limit) {
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Requerimiento> requerimientos = null;
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientosCliente(regFiltro, fieldSort, sortDirection, cedula);
		
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository.findAll(specfRequerimiento, new PageRequest(page, limit));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento);
			total = requerimientos.size();
		}
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;
	}
	
	public Map<String, Object> RequerimientosCotizados(Requerimiento regFiltro,  String fieldSort, Boolean sortDirection, 
			Integer idusuario, int page, int limit) {
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Requerimiento> requerimientos = null;
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientosCotizados(regFiltro, fieldSort, sortDirection, idusuario);
		
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository.findAll(specfRequerimiento, new PageRequest(page, limit));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento);
			total = requerimientos.size();
		}
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;
	}
	
	public Map<String, Object> ConsultarCotizacionesRequerimiento(Cotizacion cotFiltro, String fieldSort, Boolean sortDirection, 
			Integer idrequerimiento, int page, int limit) {
		List<String> estatus=new ArrayList<String>();
		estatus.add("C");
		
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Cotizacion> cotizaciones = null;
		Specification<Cotizacion> specfCotizacion = (new CotizacionDAO())
				.consultarCotizacionesAsignadas(cotFiltro, fieldSort, sortDirection, idrequerimiento,estatus);
		
		if(limit>0){
			Page<Cotizacion> pageCotizacion = this.cotizacionRepository.findAll(specfCotizacion, new PageRequest(page, limit));
			total = Long.valueOf(pageCotizacion.getTotalElements()).intValue();
			cotizaciones = pageCotizacion.getContent();
		}
		else {
			cotizaciones = this.cotizacionRepository.findAll(specfCotizacion);
			total = cotizaciones.size();
		}
		parametros.put("total", total);
		parametros.put("cotizaciones", cotizaciones);
		return parametros;
	}
	
	public Map<String, Object> ConsultarRequerimientosConSolicitudesCotizacion(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, 
			Integer idProveedor, int page, int limit) {
		List<String> estatus=new ArrayList<String>();
		estatus.add("O");
		estatus.add("CC");
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Requerimiento> requerimientos = null;
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientosConSolicitudesCotizacion(regFiltro, fieldSort, sortDirection, idProveedor, estatus);
		
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository.findAll(specfRequerimiento, new PageRequest(page, limit));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento);
			total = requerimientos.size();
		}
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;

	}
	
	public DetalleRequerimiento registrarDetalleRequerimiento(int idRequerimiento, DetalleRequerimiento detalleRequerimiento){
		Requerimiento requerimiento = this.requerimientoRepository.findOne(idRequerimiento);
		if(requerimiento!=null){
			detalleRequerimiento.setRequerimiento(requerimiento);
			detalleRequerimiento=this.detalleRequerimientoRepository.save(detalleRequerimiento);
		}
		return detalleRequerimiento;
	}
	
	public Map<String, Object> ConsultarDetalleCotizacion(Integer idcotizacion, int page, int limit) {
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleCotizacion> detallesCotizacion = null;
		Specification<DetalleCotizacion> specfDetalleCotizacion = (new DetalleCotizacionDAO()).consultarDetalleCotizacion(idcotizacion);
		
		if(limit>0){
			Page<DetalleCotizacion> pageDetalleCotizacion = this.detalleCotizacionRepository.findAll(specfDetalleCotizacion, new PageRequest(page, limit));
			total = Long.valueOf(pageDetalleCotizacion.getTotalElements()).intValue();
			detallesCotizacion = pageDetalleCotizacion.getContent();
		}
		else {
			detallesCotizacion = this.detalleCotizacionRepository.findAll(specfDetalleCotizacion);
			total = detallesCotizacion.size();
		}
		parametros.put("total", total);
		parametros.put("detalleCotizacion", detallesCotizacion);
        return parametros;
	}
	
	//Cotizaciones
	public Cotizacion ActualizarCotizacion(Cotizacion cotizacion) {
		return this.cotizacionRepository.save(cotizacion);
	}

	public Map<String, Object> consultarSolicitudCotizaciones(Cotizacion cotizacionF, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, int idProveedor, int page, int limit){
		List<String> estatus=new ArrayList<String>();
		estatus.add("SC");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Cotizacion> cotizaciones = null;
		Specification<Cotizacion> specfCotizacion = (new CotizacionDAO())
				.consultarSolicitudCotizaciones(cotizacionF, fieldSort, sortDirection, idRequerimiento, idProveedor, estatus);
		
		if(limit>0){
			Page<Cotizacion> pageCotizacion = this.cotizacionRepository.findAll(specfCotizacion, new PageRequest(page, limit));
			total = Long.valueOf(pageCotizacion.getTotalElements()).intValue();
			cotizaciones = pageCotizacion.getContent();
		}
		else {
			cotizaciones = this.cotizacionRepository.findAll(specfCotizacion);
			total = cotizaciones.size();
		}
		parametros.put("total", total);
		parametros.put("cotizaciones", cotizaciones);
		return parametros;
	}
	
	public Cotizacion registrarSolicitudCotizacion(Cotizacion cotizacion, List<DetalleCotizacion> detalleCotizacions) {
		cotizacion.setEstatus("SC");
		cotizacion.setFechaCreacion(calendar.getTime());
		cotizacion = cotizacionRepository.save(cotizacion);
		for(DetalleCotizacion detalleCotizacion : detalleCotizacions){
			detalleCotizacion.getDetalleRequerimiento().setEstatus("EP");
			detalleCotizacion.setCotizacion(cotizacion); 
			this.detalleCotizacionRepository.save(detalleCotizacion);
			
			DetalleRequerimiento detalleRequerimiento = detalleCotizacion.getDetalleRequerimiento();
			detalleRequerimiento.setEstatus("EP");
			this.detalleRequerimientoRepository.save(detalleRequerimiento);
			
			Requerimiento requerimiento = detalleRequerimiento.getRequerimiento();
			if(requerimiento.getFechaSolicitud()==null){
				requerimiento.setEstatus("EP");
				requerimiento.setFechaSolicitud(new Timestamp(Calendar.getInstance().getTime().getTime()));
				this.requerimientoRepository.save(requerimiento);
			}
			
		}
		cotizacion.setDetalleCotizacions(detalleCotizacions);
		return cotizacion;
	}
	
	@SuppressWarnings("unchecked")
	public Cotizacion registrarCotizacion(Cotizacion cotizacion, Requerimiento requerimiento) {
		String estatusRequerimiento = "CT";
		List<Cotizacion> cotizaciones = (List<Cotizacion>) consultarCotizacionesParaEditar(null, null, null, requerimiento.getIdRequerimiento(), 0, 1).get("cotizaciones");
		if(cotizacion.getEstatus()==null)
			cotizacion.setEstatus("C");
		
		if(cotizacion.getEstatus().equalsIgnoreCase("EC")) //Incompleto
			estatusRequerimiento = "EC";
		else if(!cotizaciones.isEmpty()) //Completo
			estatusRequerimiento = "EC";
		
		requerimiento.setEstatus(estatusRequerimiento);
		this.requerimientoRepository.save(requerimiento);
		
		List<DetalleCotizacion> detalles = cotizacion.getDetalleCotizacions();
		cotizacion = cotizacionRepository.save(cotizacion);
		for(DetalleCotizacion detalle : detalles){
			this.detalleCotizacionRepository.save(detalle);
			DetalleRequerimiento detalleRequerimiento = detalle.getDetalleRequerimiento();
			
			detalleRequerimiento.setEstatus("CT");
			this.detalleRequerimientoRepository.save(detalleRequerimiento);
		}
		return cotizacion;
	}
	
	public Map<String, Object> consultarCotizacionesParaEditar(Cotizacion cotizacionF, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, int page, int limit){
		List<String> estatus = new ArrayList<String>();
		estatus.add("EC");
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Cotizacion> cotizaciones = null;
		Specification<Cotizacion> specfCotizacion = (new CotizacionDAO())
				.consultarCotizacionesParaEditar(cotizacionF, fieldSort, sortDirection, idRequerimiento, estatus);
		
		if(limit>0){
			Page<Cotizacion> pageCotizacion = this.cotizacionRepository.findAll(specfCotizacion, new PageRequest(page, limit));
			total = Long.valueOf(pageCotizacion.getTotalElements()).intValue();
			cotizaciones = pageCotizacion.getContent();
		}
		else {
			cotizaciones = this.cotizacionRepository.findAll(specfCotizacion);
			total = cotizaciones.size();
		}
		parametros.put("total", total);
		parametros.put("cotizaciones", cotizaciones);
		return parametros;
	}
	
	//Detalles Cotizacion
	public Map<String, Object> consultarDetallesCotizacion(DetalleCotizacion detalleF, int idCotizacion, 
			String fieldSort, Boolean sortDirection, int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleCotizacion> detallesCotizacion = null;
		Specification<DetalleCotizacion> specfDetalleCotizacion = (new DetalleCotizacionDAO())
				.consultarDetallesCotizacion(detalleF, idCotizacion, null, false, true, fieldSort, sortDirection);
		
		if(limit>0){
			Page<DetalleCotizacion> pageDetalleCotizacion = this.detalleCotizacionRepository.findAll(specfDetalleCotizacion, new PageRequest(page, limit));
			total = Long.valueOf(pageDetalleCotizacion.getTotalElements()).intValue();
			detallesCotizacion = pageDetalleCotizacion.getContent();
		}
		else {
			detallesCotizacion = this.detalleCotizacionRepository.findAll(specfDetalleCotizacion);
			total = detallesCotizacion.size();
		}
		parametros.put("total", total);
		parametros.put("detallesCotizacion", detallesCotizacion);
		return parametros;
	}
	
	public Map<String, Object> consultarDetallesCotizacion(DetalleCotizacion detalleF, Integer idRequerimiento,
			String fieldSort, Boolean sortDirection, int page, int limit){
		boolean nuloCantidad = false;
		if(detalleF!=null){
			detalleF.getCotizacion().setEstatus("C");
			if(detalleF.getCantidad()==null){
				nuloCantidad = true;
				detalleF.setCantidad(new Long(0));
			}
		}
		Integer total = 0;
		List<DetalleCotizacion> detallesCotizacion = null;
		Specification<DetalleCotizacion> specfDetalleCotizacion = (new DetalleCotizacionDAO())
				.consultarDetallesCotizacion(detalleF, null, idRequerimiento, true, false, fieldSort, sortDirection);
		if(limit>0){
			Page<DetalleCotizacion> pageDetalleCotizacion = this.detalleCotizacionRepository.findAll(specfDetalleCotizacion, new PageRequest(page, limit));
			total = Long.valueOf(pageDetalleCotizacion.getTotalElements()).intValue();
			detallesCotizacion = pageDetalleCotizacion.getContent();
		}
		else {
			detallesCotizacion = this.detalleCotizacionRepository.findAll(specfDetalleCotizacion);
			total = detallesCotizacion.size();
		}
		
		for(int i=0; i<detallesCotizacion.size(); i++){
			DetalleCotizacion detalle = detallesCotizacion.get(i);
			DetalleCotizacionInternacional detalleInter = this.detalleCotizacionInternacionalRepository.findOne(detalle.getIdDetalleCotizacion());
			if(detalleInter != null){
				detallesCotizacion.remove(i);
				detallesCotizacion.add(i, detalleInter);
			}
		}
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", total);
		parametros.put("detallesCotizacion", detallesCotizacion);
		if(nuloCantidad)
			detalleF.setCantidad(null);
		return parametros;
	}
	
		//Internacional
	public Map<String, Object> consultarDetallesCotizacion(DetalleCotizacionInternacional detalleF, int idCotizacion, 
			String fieldSort, Boolean sortDirection, int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleCotizacionInternacional> detallesCotizacion = null;
		Specification<DetalleCotizacionInternacional> specfDetalleCotizacion = (new DetalleCotizacionInternacionalDAO())
				.consultarDetallesCotizacion(detalleF, idCotizacion, null, false, true, fieldSort, sortDirection);
		
		if(limit>0){
			Page<DetalleCotizacionInternacional> pageDetalleCotizacion = 
					this.detalleCotizacionInternacionalRepository.findAll(specfDetalleCotizacion, new PageRequest(page, limit));
			total = Long.valueOf(pageDetalleCotizacion.getTotalElements()).intValue();
			detallesCotizacion = pageDetalleCotizacion.getContent();
		}
		else {
			detallesCotizacion = this.detalleCotizacionInternacionalRepository.findAll(specfDetalleCotizacion);
			total = detallesCotizacion.size();
		}
		parametros.put("total", total);
		parametros.put("detallesCotizacion", detallesCotizacion);
		return parametros;
	}

	//Ofertas
	public Map<String, Object> consultarOfertasPorRequerimiento(int idRequerimiento, String fieldSort, Boolean sortDirection,
			int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Oferta> ofertas = null;
		Specification<Oferta> specfOferta = (new OfertaDAO()).consultarOfertasPorRequerimiento(idRequerimiento, null, fieldSort, sortDirection);
		
		if(limit>0){
			Page<Oferta> pageOferta = this.ofertaRepository.findAll(specfOferta, new PageRequest(page, limit));
			total = Long.valueOf(pageOferta.getTotalElements()).intValue();
			ofertas = pageOferta.getContent();
		}
		else {
			ofertas = this.ofertaRepository.findAll(specfOferta);
			total = ofertas.size();
		}
		parametros.put("total", total);
		parametros.put("ofertas", ofertas);
		return parametros;
	}
	
	public Map<String, Object> consultarOfertasRecibidasPorRequerimiento(int idRequerimiento, int page, int limit){
		List<String> estatus = new ArrayList<String>();
		estatus.add("recibida");
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Oferta> ofertas = null;
		Specification<Oferta> specfOferta = (new OfertaDAO()).consultarOfertasPorRequerimiento(idRequerimiento, estatus, "fechaCreacion", true);
		
		if(limit>0){
			Page<Oferta> pageOferta = this.ofertaRepository.findAll(specfOferta, new PageRequest(page, limit));
			total = Long.valueOf(pageOferta.getTotalElements()).intValue();
			ofertas = pageOferta.getContent();
		}
		else {
			ofertas = this.ofertaRepository.findAll(specfOferta);
			total = ofertas.size();
		}
		parametros.put("total", total);
		parametros.put("ofertas", ofertas);
		return parametros;
	}
	
	public Oferta consultarOfertaEnviadaPorRequerimiento(int idRequerimiento) {
		Oferta oferta = null;
		List<String> estatus = new ArrayList<String>();
		estatus.add("enviada");
		Specification<Oferta> specfOferta = (new OfertaDAO()).consultarOfertasPorRequerimiento(idRequerimiento, estatus, "fechaCreacion", true);
		List<Oferta> ofertas = ofertaRepository.findAll(specfOferta);
		if(ofertas!=null && !ofertas.isEmpty()){
			oferta = ofertas.get(0);
			oferta.setDetalleOfertas(consultarDetallesOferta(oferta.getIdOferta()));
		}
		return oferta;
	}
	
	public List<DetalleOferta> consultarDetallesOferta(Integer idOferta){
		return detalleOfertaRepository.findByOferta_IdOferta(idOferta);
	}
	
	public Oferta actualizarOferta(Oferta oferta) {
		if(oferta.getIdOferta()==null)
			oferta.setEstatus("solicitado");
		return oferta = ofertaRepository.save(oferta);
	}

	//Compras
	public Map<String, Object> consultarComprasPorRequerimiento(Compra compraF, int idRequerimiento, String fieldSort, Boolean sortDirection,
			int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Compra> compras = null;
		Specification<Compra> specfCompra = (new CompraDAO()).consultarComprasPorRequerimiento(compraF, idRequerimiento, fieldSort, sortDirection);
		
		if(limit>0){
			Page<Compra> pageCompra = this.compraRepository.findAll(specfCompra, new PageRequest(page, limit));
			total = Long.valueOf(pageCompra.getTotalElements()).intValue();
			compras = pageCompra.getContent();
		}
		else {
			compras = this.compraRepository.findAll(specfCompra);
			total = compras.size();
		}
		
		parametros.put("total", total);
		parametros.put("compras", compras);
		return parametros;
	}
	
	public Compra registrarOActualizarCompra(Compra compra){
		if(compra.getIdCompra()==null)
			compra.setFechaCreacion(new Timestamp(this.calendar.getTime().getTime()));
			
		return compra=this.compraRepository.save(compra);
	}
	
	public Compra registrarSolicitudCompra(Compra compra) {
		compra.setEstatus("solicitada");
		return registrarOActualizarCompra(compra);
	}
	
	public Compra registrarCompra(Compra compra) {
		List<DetalleOferta> detalleCompra = compra.getDetalleOfertas();
		compra.setDetalleOfertas(null);
		compra.setEstatus("enviada");
		compra = registrarOActualizarCompra(compra);
		if(detalleCompra!=null && !detalleCompra.isEmpty()){
			for(DetalleOferta detalle : detalleCompra){
				detalle = detalleOfertaRepository.findOne(detalle.getIdDetalleOferta());
				detalle.setCompra(compra);
				detalleOfertaRepository.save(detalle);
			}
		}
		compra.setDetalleOfertas(detalleCompra);
		return compra;
	}

	//DetalleCompra
	public Map<String, Object> consultarDetallesCompra(int idCompra, String fieldSort, Boolean sortDirection, 
			int page, int limit) {
		fieldSort = (fieldSort==null) ? "idDetalleOferta" : fieldSort;
		sortDirection = (sortDirection==null) ? true : sortDirection;
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleOferta> detallesCompra = null;
		Sort sortCompra = new Sort((sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC, fieldSort);	
		if(limit>0){
			Page<DetalleOferta> pageDetallesCompra = this.detalleOfertaRepository
					.findByCompra_IdCompra(idCompra, new PageRequest(page, limit, sortCompra));
			total = Long.valueOf(pageDetallesCompra.getTotalElements()).intValue();
			detallesCompra = pageDetallesCompra.getContent();
		}
		else {
			detallesCompra = this.detalleOfertaRepository.findByCompra_IdCompra(idCompra, sortCompra);
			total = detallesCompra.size();
		}
		parametros.put("total", total);
		parametros.put("detallesCompra", detallesCompra);
		return parametros;
	}
}
