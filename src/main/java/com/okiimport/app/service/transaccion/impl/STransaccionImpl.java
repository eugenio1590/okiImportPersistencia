package com.okiimport.app.service.transaccion.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.dao.transaccion.VentaRepository;
import com.okiimport.app.dao.transaccion.CotizacionRepository;
import com.okiimport.app.dao.transaccion.OfertaRepository;
import com.okiimport.app.dao.transaccion.RequerimientoRepository;
import com.okiimport.app.dao.transaccion.detalle.cotizacion.DetalleCotizacionInternacionalRepository;
import com.okiimport.app.dao.transaccion.detalle.cotizacion.DetalleCotizacionRepository;
import com.okiimport.app.dao.transaccion.detalle.oferta.DetalleOfertaRepository;
import com.okiimport.app.dao.transaccion.detalle.requerimiento.DetalleRequerimientoRepository;
import com.okiimport.app.dao.transaccion.impl.VentaDAO;
import com.okiimport.app.dao.transaccion.impl.CotizacionDAO;
import com.okiimport.app.dao.transaccion.impl.OfertaDAO;
import com.okiimport.app.dao.transaccion.impl.RequerimientoDAO;
import com.okiimport.app.dao.transaccion.impl.detalle.cotizacion.DetalleCotizacionDAO;
import com.okiimport.app.dao.transaccion.impl.detalle.cotizacion.DetalleCotizacionInternacionalDAO;
import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Venta;
import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.DetalleCotizacionInternacional;
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.enumerados.EEstatusVenta;
import com.okiimport.app.model.enumerados.EEstatusCotizacion;
import com.okiimport.app.model.enumerados.EEstatusDetalleRequerimiento;
import com.okiimport.app.model.enumerados.EEstatusOferta;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.configuracion.SControlUsuario;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;

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
	private VentaRepository ventaRepository;

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
		ESTATUS_OFERTADOS.add("CC");
		ESTATUS_OFERTADOS.add("CP");
		ESTATUS_OFERTADOS.add("C");
	}

	public Requerimiento registrarRequerimiento(Requerimiento requerimiento, boolean asignarAnalista, SMaestros sMaestros) {
		Date fechaCreacion = calendar.getTime();
		Date fechaVencimiento = sumarORestarFDia(fechaCreacion, 15);
		if(asignarAnalista)
			asignarRequerimiento(requerimiento, sMaestros);
		requerimiento.setFechaCreacion(new Timestamp(fechaCreacion.getTime()));
		requerimiento.setFechaVencimiento(fechaVencimiento);
		requerimiento.setEstatus(EEstatusRequerimiento.EMITIDO);
		for(DetalleRequerimiento detalle:requerimiento.getDetalleRequerimientos())
			detalle.setEstatus(EEstatusDetalleRequerimiento.SOLICITADO);
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
			//detalleOferta.setEstatus(EEstatusDetalleOferta.SELECCION);
			this.detalleOfertaRepository.save(detalleOferta);
		}
	}
	
	public void asignarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros) {
		List<EEstatusRequerimiento> estatus = EEstatusRequerimiento.getEstatusGeneral();
		List<Analista> analistas = sMaestros.consultarCantRequerimientos(estatus, 0, 1);
		if(analistas.size()>0)
			requerimiento.setAnalista(analistas.get(0));
	}
	
	public Map<String, Object> consultarRequerimientosGeneral(Requerimiento regFiltro, String fieldSort, Boolean sortDirection,
			int page, int limit){
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Requerimiento> requerimientos = null;
		Sort sortRequerimiento 
			= new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idRequerimiento"));
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO()).consultarRequerimientosGeneral(regFiltro);
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository
					.findAll(specfRequerimiento, new PageRequest(page, limit, sortRequerimiento));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento, sortRequerimiento);
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
		Sort sortRequerimiento 
			= new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idRequerimiento"));
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientoUsuario(regFiltro, idusuario, EEstatusRequerimiento.getEstatusEmitidos());
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository
					.findAll(specfRequerimiento, new PageRequest(page, limit, sortRequerimiento));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento, sortRequerimiento);
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
		Sort sortRequerimiento 
			= new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idRequerimiento"));
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientoUsuario(regFiltro, idusuario, EEstatusRequerimiento.getEstatusProcesados());
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository
					.findAll(specfRequerimiento, new PageRequest(page, limit, sortRequerimiento));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento, sortRequerimiento);
			total = requerimientos.size();
		}
		
		if(!requerimientos.isEmpty())
			llenarNroOfertas(requerimientos);
		
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;
	}
	
	public Map <String, Object> consultarMisRequerimientosOfertados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, 
			Integer idusuario, int page, int limit){
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Requerimiento> requerimientos = null;
		Sort sortRequerimiento 
			= new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idRequerimiento"));
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientoUsuario(regFiltro, idusuario, EEstatusRequerimiento.getEstatusOfertados());
		
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository
					.findAll(specfRequerimiento, new PageRequest(page, limit, sortRequerimiento));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento, sortRequerimiento);
			total = requerimientos.size();
		}
		
		if(!requerimientos.isEmpty())
			llenarNroOfertas(requerimientos);
		
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;
	}
	
	public Map<String, Object> consultarRequerimientosCliente (Requerimiento regFiltro, String fieldSort, 
			Boolean sortDirection, String cedula, int page, int limit) {
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Requerimiento> requerimientos = null;
		Sort sortRequerimiento = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idRequerimiento"))
			.and(new Sort(Sort.Direction.DESC, "fechaCreacion"))
			.and(new Sort(Sort.Direction.ASC, "idRequerimiento"));
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientosCliente(regFiltro, cedula);
		
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository
					.findAll(specfRequerimiento, new PageRequest(page, limit, sortRequerimiento));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento, sortRequerimiento);
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
		Sort sortRequerimiento 
			= new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idRequerimiento"));
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientosCotizados(regFiltro, idusuario);
		
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository
					.findAll(specfRequerimiento, new PageRequest(page, limit, sortRequerimiento));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento, sortRequerimiento);
			total = requerimientos.size();
		}
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;
	}
	
	public Map<String, Object> ConsultarCotizacionesRequerimiento(Cotizacion cotFiltro, String fieldSort, Boolean sortDirection, 
			Integer idrequerimiento, int page, int limit) {
		List<EEstatusCotizacion> estatus=new ArrayList<EEstatusCotizacion>();
		estatus.add(EEstatusCotizacion.CONCRETADA);
		
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Cotizacion> cotizaciones = null;
		Sort sortCotizacion = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idCotizacion"));
		Specification<Cotizacion> specfCotizacion = (new CotizacionDAO())
				.consultarCotizacionesAsignadas(cotFiltro, idrequerimiento,estatus);
		
		if(limit>0){
			Page<Cotizacion> pageCotizacion = this.cotizacionRepository
					.findAll(specfCotizacion, new PageRequest(page, limit, sortCotizacion));
			total = Long.valueOf(pageCotizacion.getTotalElements()).intValue();
			cotizaciones = pageCotizacion.getContent();
		}
		else {
			cotizaciones = this.cotizacionRepository.findAll(specfCotizacion, sortCotizacion);
			total = cotizaciones.size();
		}
		parametros.put("total", total);
		parametros.put("cotizaciones", cotizaciones);
		return parametros;
	}
	
	public Map<String, Object> consultarRequerimientosConSolicitudesCotizacion(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, 
			Integer idProveedor, int page, int limit) {
		List<EEstatusRequerimiento> estatus=new ArrayList<EEstatusRequerimiento>();
		estatus.add(EEstatusRequerimiento.CONCRETADO);
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Requerimiento> requerimientos = null;
		Sort sortRequerimiento 
			= new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idRequerimiento"));
		Specification<Requerimiento> specfRequerimiento = (new RequerimientoDAO())
				.consultarRequerimientosConSolicitudesCotizacion(regFiltro, idProveedor, estatus);
		
		if(limit>0){
			Page<Requerimiento> pageRequerimiento = this.requerimientoRepository
					.findAll(specfRequerimiento, new PageRequest(page, limit, sortRequerimiento));
			total = Long.valueOf(pageRequerimiento.getTotalElements()).intValue();
			requerimientos = pageRequerimiento.getContent();
		}
		else {
			requerimientos = this.requerimientoRepository.findAll(specfRequerimiento, sortRequerimiento);
			total = requerimientos.size();
		}
		parametros.put("total", total);
		parametros.put("requerimientos", requerimientos);
		return parametros;

	}
	
	public Requerimiento reactivarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros){
		boolean encontrado = false;
		Analista analistaAnterior = requerimiento.getAnalista();
		
		do {
			this.asignarRequerimiento(requerimiento, sMaestros);
			if(requerimiento.getAnalista().getId() != analistaAnterior.getId())
				encontrado = true;
		} while(!encontrado);
		
		if(encontrado) {
			requerimiento.setEstatus(EEstatusRequerimiento.EMITIDO);
			this.registrarRequerimiento(requerimiento, false, sMaestros);
		}
		return requerimiento;
	}
	
	public Requerimiento cerrarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros,  SControlUsuario sControlUsuario, boolean aprobarProveedores){
		requerimiento.cerrarSolicitud();
		actualizarRequerimiento(requerimiento);
		if(aprobarProveedores){
			List<Proveedor> proveedores = sMaestros.consultarProveedoresHaAprobar(requerimiento);
			if(!proveedores.isEmpty())
				for(Proveedor proveedor : proveedores){
					sControlUsuario.crearUsuario(proveedor, sMaestros);
					//Faltaria enviar el correo al proveedor
				}
		}
		return requerimiento;
	}
	
	public DetalleRequerimiento registrarDetalleRequerimiento(int idRequerimiento, DetalleRequerimiento detalleRequerimiento){
		Requerimiento requerimiento = this.requerimientoRepository.findOne(idRequerimiento);
		if(requerimiento!=null){
			detalleRequerimiento.setRequerimiento(requerimiento);
			detalleRequerimiento=this.detalleRequerimientoRepository.save(detalleRequerimiento);
		}
		return detalleRequerimiento;
	}
	
	public Map<String, Object> consultarDetallesRequerimiento(int idRequerimiento, int page, int limit){
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleRequerimiento> detallesRequerimiento = null;
		if(limit>0){
			Page<DetalleRequerimiento> pageDetallesRequerimiento 
				= this.detalleRequerimientoRepository.findByRequerimiento_IdRequerimiento(idRequerimiento, new PageRequest(page, limit));
			total = Long.valueOf(pageDetallesRequerimiento.getTotalElements()).intValue();
			detallesRequerimiento = pageDetallesRequerimiento.getContent();
		}
		else {
			detallesRequerimiento = this.detalleRequerimientoRepository.findByRequerimiento_IdRequerimiento(idRequerimiento);
			total = detallesRequerimiento.size();
		}
		parametros.put("total", total);
		parametros.put("detallesRequerimiento", detallesRequerimiento);
		return parametros;
	}
	
	public Map<String, Object> ConsultarDetalleCotizacion(Integer idCotizacion, int page, int limit) {
		Map<String, Object> parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleCotizacion> detallesCotizacion = null;
				
		if(limit>0){
			Page<DetalleCotizacion> pageDetalleCotizacion 
				= this.detalleCotizacionRepository.findByCotizacion_IdCotizacion(idCotizacion, new PageRequest(page, limit));
			total = Long.valueOf(pageDetalleCotizacion.getTotalElements()).intValue();
			detallesCotizacion = pageDetalleCotizacion.getContent();
		}
		else {
			detallesCotizacion = this.detalleCotizacionRepository.findByCotizacion_IdCotizacion(idCotizacion);
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
		List<EEstatusCotizacion> estatus=new ArrayList<EEstatusCotizacion>();
		estatus.add(EEstatusCotizacion.SOLICITADA);
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Cotizacion> cotizaciones = null;
		Sort sortCotizacion = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idCotizacion"));
		Specification<Cotizacion> specfCotizacion = (new CotizacionDAO())
				.consultarSolicitudCotizaciones(cotizacionF, idRequerimiento, idProveedor, estatus);
		
		if(limit>0){
			Page<Cotizacion> pageCotizacion = this.cotizacionRepository
					.findAll(specfCotizacion, new PageRequest(page, limit, sortCotizacion));
			total = Long.valueOf(pageCotizacion.getTotalElements()).intValue();
			cotizaciones = pageCotizacion.getContent();
		}
		else {
			cotizaciones = this.cotizacionRepository.findAll(specfCotizacion, sortCotizacion);
			total = cotizaciones.size();
		}
		parametros.put("total", total);
		parametros.put("cotizaciones", cotizaciones);
		return parametros;
	}
	
	public Cotizacion registrarSolicitudCotizacion(Cotizacion cotizacion, List<DetalleCotizacion> detalleCotizacions) {
		cotizacion.setEstatus(EEstatusCotizacion.SOLICITADA);
		cotizacion.setFechaCreacion(calendar.getTime());
		cotizacion = cotizacionRepository.save(cotizacion);
		for(DetalleCotizacion detalleCotizacion : detalleCotizacions){
			
			detalleCotizacion.setCotizacion(cotizacion); 
			this.detalleCotizacionRepository.save(detalleCotizacion);
			
			DetalleRequerimiento detalleRequerimiento = detalleCotizacion.getDetalleRequerimiento();
			detalleRequerimiento.setEstatus(EEstatusDetalleRequerimiento.ENVIADO_PROVEEDOR);
			this.detalleRequerimientoRepository.save(detalleRequerimiento);
			
			Requerimiento requerimiento = detalleRequerimiento.getRequerimiento();
			if(requerimiento.getFechaSolicitud()==null){
				requerimiento.setEstatus(EEstatusRequerimiento.ENVIADO_PROVEEDOR);
				requerimiento.setFechaSolicitud(new Timestamp(Calendar.getInstance().getTime().getTime()));
				this.requerimientoRepository.save(requerimiento);
			}
			
		}
		cotizacion.setDetalleCotizacions(detalleCotizacions);
		return cotizacion;
	}
	
	@SuppressWarnings("unchecked")
	public Cotizacion registrarCotizacion(Cotizacion cotizacion, Requerimiento requerimiento) {
		EEstatusRequerimiento estatusRequerimiento = EEstatusRequerimiento.CON_COTIZACIONES_A;
		List<Cotizacion> cotizaciones = (List<Cotizacion>) consultarCotizacionesParaEditar(null, null, null, requerimiento.getIdRequerimiento(), 0, 1).get("cotizaciones");
		if(cotizacion.getEstatus()==null)
			cotizacion.setEstatus(EEstatusCotizacion.CONCRETADA);
		
		if(cotizacion.getEstatus().equals(EEstatusCotizacion.INCOMPLETA) /*Incompleto*/ || !cotizaciones.isEmpty() /*Completo*/) 
			estatusRequerimiento = EEstatusRequerimiento.CON_COTIZACIONES_I;
		
		requerimiento.setEstatus(estatusRequerimiento);
		this.requerimientoRepository.save(requerimiento);
		
		List<DetalleCotizacion> detalles = cotizacion.getDetalleCotizacions();
		cotizacion = cotizacionRepository.save(cotizacion);
		for(DetalleCotizacion detalle : detalles){
			this.detalleCotizacionRepository.save(detalle);
			DetalleRequerimiento detalleRequerimiento = detalle.getDetalleRequerimiento();
			
			detalleRequerimiento.setEstatus(EEstatusDetalleRequerimiento.CON_COTIZACIONES_A);
			this.detalleRequerimientoRepository.save(detalleRequerimiento);
		}
		return cotizacion;
	}
	
	public Map<String, Object> consultarCotizacionesParaEditar(Cotizacion cotizacionF, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, int page, int limit){
		List<EEstatusCotizacion> estatus = new ArrayList<EEstatusCotizacion>();
		estatus.add(EEstatusCotizacion.INCOMPLETA);
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Cotizacion> cotizaciones = null;
		Sort sortCotizacion = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idCotizacion"));
		Specification<Cotizacion> specfCotizacion = (new CotizacionDAO())
				.consultarCotizacionesParaEditar(cotizacionF, idRequerimiento, estatus);
		
		if(limit>0){
			Page<Cotizacion> pageCotizacion = this.cotizacionRepository
					.findAll(specfCotizacion, new PageRequest(page, limit, sortCotizacion));
			total = Long.valueOf(pageCotizacion.getTotalElements()).intValue();
			cotizaciones = pageCotizacion.getContent();
		}
		else {
			cotizaciones = this.cotizacionRepository.findAll(specfCotizacion, sortCotizacion);
			total = cotizaciones.size();
		}
		parametros.put("total", total);
		parametros.put("cotizaciones", cotizaciones);
		return parametros;
	}
	
	@Override
	public Boolean validarProveedorEnCotizaciones(Proveedor proveedor) {
		List<EEstatusCotizacion> estatus =  new ArrayList<EEstatusCotizacion>();
		estatus.add(EEstatusCotizacion.EMITIDA);
		estatus.add(EEstatusCotizacion.INCOMPLETA);
		estatus.add(EEstatusCotizacion.CONCRETADA);
		return this.cotizacionRepository.findByProveedorAndEstatusIn(proveedor, estatus).size()==0;
	}
	
	//Detalles Cotizacion
	public Map<String, Object> consultarDetallesCotizacion(DetalleCotizacion detalleF, int idCotizacion, 
			String fieldSort, Boolean sortDirection, int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleCotizacion> detallesCotizacion = null;
		Sort sortDetalleCotizacion 
			= new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idDetalleCotizacion"));
		Specification<DetalleCotizacion> specfDetalleCotizacion = (new DetalleCotizacionDAO())
				.consultarDetallesCotizacion(detalleF, idCotizacion, null, false, true);
		
		if(limit>0){
			Page<DetalleCotizacion> pageDetalleCotizacion = this.detalleCotizacionRepository
					.findAll(specfDetalleCotizacion, new PageRequest(page, limit, sortDetalleCotizacion));
			total = Long.valueOf(pageDetalleCotizacion.getTotalElements()).intValue();
			detallesCotizacion = pageDetalleCotizacion.getContent();
		}
		else {
			detallesCotizacion = this.detalleCotizacionRepository.findAll(specfDetalleCotizacion, sortDetalleCotizacion);
			total = detallesCotizacion.size();
		}
		parametros.put("total", total);
		parametros.put("detallesCotizacion", detallesCotizacion);
		return parametros;
	}
	
	public Map<String, Object> consultarDetallesCotizacion(final DetalleCotizacion detalleF, Integer idRequerimiento,
			String fieldSort, final Boolean sortDirection, int page, int limit){
		boolean nuloCantidad = false, nuloTotal = false;
		if(detalleF!=null){
			if(detalleF.getCantidad()==null){
				nuloCantidad = true;
				detalleF.setCantidad(new Long(0));
			}
			
			if(detalleF.getTotal() == 0)
				nuloTotal = true;
			else
				fieldSort = "total";
		}
		
		Integer total = 0;
		List<DetalleCotizacion> detallesCotizacion = null;
		Sort sortDetalleCotizacion = (fieldSort!=null && fieldSort.equalsIgnoreCase("total"))
				? new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idDetalleCotizacion"))
					: new Sort(getDirection(sortDirection, Sort.Direction.ASC), "idDetalleCotizacion");
	
		Specification<DetalleCotizacion> specfDetalleCotizacion = (new DetalleCotizacionDAO())
			.consultarDetallesCotizacion(detalleF, null, idRequerimiento, true, false, EEstatusCotizacion.EMITIDA);
		
		if(limit>0 && nuloTotal){
			Page<DetalleCotizacion> pageDetalleCotizacion = this.detalleCotizacionRepository
					.findAll(specfDetalleCotizacion, new PageRequest(page, limit, sortDetalleCotizacion));
			total = Long.valueOf(pageDetalleCotizacion.getTotalElements()).intValue();
			detallesCotizacion = pageDetalleCotizacion.getContent();
		}
		else {
			detallesCotizacion = this.detalleCotizacionRepository.findAll(specfDetalleCotizacion, sortDetalleCotizacion);
			total = detallesCotizacion.size();
		}
		
		if(fieldSort!=null && fieldSort.equalsIgnoreCase("total")){
			Collections.sort(detallesCotizacion, new Comparator<DetalleCotizacion>(){

				@Override
				public int compare(DetalleCotizacion o1, DetalleCotizacion o2) {
					if(sortDirection)
						return o1.calcularTotal().compareTo(o2.calcularTotal());
					else
						return o2.calcularTotal().compareTo(o2.calcularCosto());
				}
				
			});
		}
		
		List<DetalleCotizacion> detallesEliminar = new ArrayList<DetalleCotizacion>();
		
		if(!nuloTotal){
			for(DetalleCotizacion detalle : detallesCotizacion){
				if(!detalle.compararTotal(detalleF.totalParaLike()))
					detallesEliminar.add(detalle);
			}
			
			detallesCotizacion.removeAll(detallesEliminar);		
			total = detallesCotizacion.size();
		}
		
		detallesEliminar.clear();
		Map<Integer, DetalleCotizacion> detallesIncluir = new HashMap<Integer, DetalleCotizacion>();
		for(int i=0; i<detallesCotizacion.size(); i++){
			DetalleCotizacion detalle = detallesCotizacion.get(i);
			DetalleCotizacionInternacional detalleInter = this.detalleCotizacionInternacionalRepository.findOne(detalle.getIdDetalleCotizacion());
			if(detalleInter != null){
				detallesEliminar.add(detalle);
				detallesIncluir.put(i, detalleInter);
			}
		}
		
		if(!detallesEliminar.isEmpty()){
			detallesCotizacion = new ArrayList<DetalleCotizacion>(detallesCotizacion);
			detallesCotizacion.removeAll(detallesEliminar);
		}
		
		if(!detallesIncluir.isEmpty())
			for(Integer index : detallesIncluir.keySet())
				detallesCotizacion.add(index, detallesIncluir.get(index));
		
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
		Sort sortDetalleCotizacion 
			= new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idDetalleCotizacion"));
		Specification<DetalleCotizacionInternacional> specfDetalleCotizacion = (new DetalleCotizacionInternacionalDAO())
				.consultarDetallesCotizacion(detalleF, idCotizacion, null, false, true);
		
		if(limit>0){
			Page<DetalleCotizacionInternacional> pageDetalleCotizacion = 
					this.detalleCotizacionInternacionalRepository
						.findAll(specfDetalleCotizacion, new PageRequest(page, limit, sortDetalleCotizacion));
			total = Long.valueOf(pageDetalleCotizacion.getTotalElements()).intValue();
			detallesCotizacion = pageDetalleCotizacion.getContent();
		}
		else {
			detallesCotizacion = this.detalleCotizacionInternacionalRepository
					.findAll(specfDetalleCotizacion, sortDetalleCotizacion);
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
		Sort sortOferta = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idOferta"));
		Specification<Oferta> specfOferta = (new OfertaDAO()).consultarOfertasPorRequerimiento(idRequerimiento, null);
		
		if(limit>0){
			Page<Oferta> pageOferta = this.ofertaRepository.findAll(specfOferta, new PageRequest(page, limit, sortOferta));
			total = Long.valueOf(pageOferta.getTotalElements()).intValue();
			ofertas = pageOferta.getContent();
		}
		else {
			ofertas = this.ofertaRepository.findAll(specfOferta, sortOferta);
			total = ofertas.size();
		}
		parametros.put("total", total);
		parametros.put("ofertas", ofertas);
		return parametros;
	}
	
	public Map<String, Object> consultarOfertasRecibidasPorRequerimiento(int idRequerimiento, int page, int limit){
		List<EEstatusOferta> estatus = new ArrayList<EEstatusOferta>();
		estatus.add(EEstatusOferta.RECIBIDA);
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Oferta> ofertas = null;
		Sort sortOferta = new Sort(Sort.Direction.ASC, "fechaCreacion");
		Specification<Oferta> specfOferta = (new OfertaDAO()).consultarOfertasPorRequerimiento(idRequerimiento, estatus);
		
		if(limit>0){
			Page<Oferta> pageOferta = this.ofertaRepository.findAll(specfOferta, new PageRequest(page, limit, sortOferta));
			total = Long.valueOf(pageOferta.getTotalElements()).intValue();
			ofertas = pageOferta.getContent();
		}
		else {
			ofertas = this.ofertaRepository.findAll(specfOferta, sortOferta);
			total = ofertas.size();
		}
		parametros.put("total", total);
		parametros.put("ofertas", ofertas);
		return parametros;
	}
	
	public List<Oferta> consultarOfertasEnviadaPorRequerimiento(int idRequerimiento) {
		List<EEstatusOferta> estatus = new ArrayList<EEstatusOferta>();
		estatus.add(EEstatusOferta.ENVIADA);
		Sort sortOferta = new Sort(Sort.Direction.ASC, "fechaCreacion");
		Specification<Oferta> specfOferta = (new OfertaDAO()).consultarOfertasPorRequerimiento(idRequerimiento, estatus);
		List<Oferta> ofertas = ofertaRepository.findAll(specfOferta, sortOferta);
		if(ofertas!=null && !ofertas.isEmpty()){
			for(Oferta oferta : ofertas){
				oferta.setDetalleOfertas(consultarDetallesOferta(oferta));
			}
		}
		return ofertas;
	}
	
	public Integer consultarCantOfertasCreadasPorRequermiento(int idRequerimiento){
		List<EEstatusOferta> estatus = new ArrayList<EEstatusOferta>();
		estatus.add(EEstatusOferta.SELECCION);
		Specification<Oferta> specfOferta = (new OfertaDAO()).consultarOfertasPorRequerimiento(idRequerimiento, estatus);
		return Long.valueOf(this.ofertaRepository.count(specfOferta)).intValue();		
	}
	
	public Oferta actualizarOferta(Oferta oferta) {
		if(oferta.getIdOferta()==null)
	    	oferta.setEstatus(EEstatusOferta.SELECCION);
		return oferta = ofertaRepository.save(oferta);
	}
	
	//Detalles Oferta
	public List<DetalleOferta> consultarDetallesOferta(Oferta oferta){
		return detalleOfertaRepository.findByOferta(oferta);
	}
	
	public Map<String, Object> consultarSolicitudesVentaProveedor(Requerimiento requerimiento, Proveedor proveedor, int page, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleOferta> detallesOferta = null;
		
		if(limit>0){
			Page<DetalleOferta> pageDetalle = detalleOfertaRepository.findByVentaRequerimientoAndDetalleCotizacion_Cotizacion_Proveedor(requerimiento, proveedor, new PageRequest(0, 1));
			total = Long.valueOf(pageDetalle.getTotalElements()).intValue();
			detallesOferta = pageDetalle.getContent();
		}
		else {
			detallesOferta = detalleOfertaRepository.findByVentaRequerimientoAndDetalleCotizacion_Cotizacion_Proveedor(requerimiento, proveedor);
			total = detallesOferta.size();
		}
		
		parametros.put("total", total);
		parametros.put("detallesOferta", detallesOferta);
		return parametros;
	}

	//Ventas
	public Map<String, Object> consultarVentaPorRequerimiento(Venta ventaF, int idRequerimiento, String fieldSort, Boolean sortDirection,
			int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Venta> ventas = null;
		Sort sortVenta = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idVenta"));
		Specification<Venta> specfVenta = (new VentaDAO()).consultarVentaPorRequerimiento(ventaF, idRequerimiento);
		
		if(limit>0){
			Page<Venta> pageVenta = this.ventaRepository.findAll(specfVenta, new PageRequest(page, limit, sortVenta));
			total = Long.valueOf(pageVenta.getTotalElements()).intValue();
			ventas = pageVenta.getContent();
		}
		else {
			ventas = this.ventaRepository.findAll(specfVenta, sortVenta);
			total = ventas.size();
		}
		
		parametros.put("total", total);
		parametros.put("ventas", ventas);
		return parametros;
	}
	
	public Venta registrarOActualizarVenta(Venta venta){
		if(venta.getIdVenta()==null)
			venta.setFechaCreacion(new Timestamp(this.calendar.getTime().getTime()));
			
		return venta=this.ventaRepository.save(venta);
	}
	
	public Venta registrarSolicitudVenta(Venta venta) {
		venta.setEstatus(EEstatusVenta.SOLICITADA);
		return registrarOActualizarVenta(venta);
	}
	
	public Venta registrarVenta(Venta venta, Requerimiento requerimiento,  boolean cambiarEstatus) {
		if(cambiarEstatus)
			requerimiento.setEstatus((venta.getTipoFlete()) 
					? EEstatusRequerimiento.CONCRETADO : EEstatusRequerimiento.COMPRADO); 
		actualizarRequerimiento(requerimiento);
		List<DetalleOferta> detalleVenta = venta.getDetalleOfertas();
		venta.setDetalleOfertas(null);
		venta.setEstatus(EEstatusVenta.ENVIADA);
		venta = registrarOActualizarVenta(venta);
		if(detalleVenta!=null && !detalleVenta.isEmpty()){
			for(DetalleOferta detalle : detalleVenta){
				detalle = detalleOfertaRepository.findOne(detalle.getIdDetalleOferta());
				detalle.setVenta(venta);
				detalleOfertaRepository.save(detalle);
			}
		}
		venta.setDetalleOfertas(detalleVenta);
		return venta;
	}

	//DetalleVenta
	public Map<String, Object> consultarDetallesVenta(int idVenta, String fieldSort, Boolean sortDirection, 
			int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleOferta> detallesVenta = null;
		Sort sortVenta = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idDetalleOferta"));	
		if(limit>0){
			Page<DetalleOferta> pageDetallesVenta = this.detalleOfertaRepository
					.findByVenta_IdVenta(idVenta, new PageRequest(page, limit, sortVenta));
			total = Long.valueOf(pageDetallesVenta.getTotalElements()).intValue();
			detallesVenta = pageDetallesVenta.getContent();
		}
		else {
			detallesVenta = this.detalleOfertaRepository.findByVenta_IdVenta(idVenta, sortVenta);
			total = detallesVenta.size();
		}
		parametros.put("total", total);
		parametros.put("detallesVenta", detallesVenta);
		return parametros;
	}

	/**METODOS PROPIOS DE LA CLASE*/
	private void llenarNroOfertas(List<Requerimiento> requerimientos){
		Integer nroOfertas = 0;
		for(Requerimiento requerimiento : requerimientos){
			nroOfertas = (Integer) consultarOfertasPorRequerimiento(requerimiento.getIdRequerimiento(), null, null, 0, 1).get("total");
			requerimiento.setNroOfertas(nroOfertas);
		}
	}
}
