package com.okiimport.app.service.transaccion.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.dao.maestros.DepositoRepository;
import com.okiimport.app.dao.pago.PagoClienteRepository;
import com.okiimport.app.dao.pago.impl.PagoClienteDAO;
import com.okiimport.app.dao.transaccion.CompraRepository;
import com.okiimport.app.dao.transaccion.CotizacionRepository;
import com.okiimport.app.dao.transaccion.OfertaRepository;
import com.okiimport.app.dao.transaccion.OrdenCompraRepository;
import com.okiimport.app.dao.transaccion.RequerimientoRepository;
import com.okiimport.app.dao.transaccion.detalle.cotizacion.DetalleCotizacionInternacionalRepository;
import com.okiimport.app.dao.transaccion.detalle.cotizacion.DetalleCotizacionRepository;
import com.okiimport.app.dao.transaccion.detalle.oferta.DetalleOfertaRepository;
import com.okiimport.app.dao.transaccion.detalle.requerimiento.DetalleRequerimientoRepository;
import com.okiimport.app.dao.transaccion.impl.CompraDAO;
import com.okiimport.app.dao.transaccion.impl.CotizacionDAO;
import com.okiimport.app.dao.transaccion.impl.OfertaDAO;
import com.okiimport.app.dao.transaccion.impl.OrdenCompraDAO;
import com.okiimport.app.dao.transaccion.impl.RequerimientoDAO;
import com.okiimport.app.dao.transaccion.impl.detalle.cotizacion.DetalleCotizacionDAO;
import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.Configuracion;
import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.DetalleCotizacionInternacional;
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.OrdenCompra;
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.enumerados.EEstatusCompra;
import com.okiimport.app.model.enumerados.EEstatusCotizacion;
import com.okiimport.app.model.enumerados.EEstatusDetalleRequerimiento;
import com.okiimport.app.model.enumerados.EEstatusOferta;
import com.okiimport.app.model.enumerados.EEstatusOrdenCompra;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.configuracion.SControlConfiguracion;
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
	private CompraRepository compraRepository;
	
	@Autowired
	private OrdenCompraRepository ordenCompraRepository;
	
	@Autowired
	private PagoClienteRepository pagoRepository;
	
	@Autowired
	private DepositoRepository depositoRepository;
	
	@Autowired
	private PagoClienteRepository pagoClienteRepository;
	
	private SControlConfiguracion sControlConfiguracion;

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
	
	@Override
	public Cotizacion registrarRecotizacion(Requerimiento requerimiento, Proveedor proveedor, List<DetalleCotizacion> detalles){
		requerimiento.setEstatus(EEstatusRequerimiento.CON_RECOTIZACIONES);
		this.requerimientoRepository.save(requerimiento);
		Cotizacion cotizacion = new Cotizacion(proveedor);
		cotizacion.setDetalleCotizacions(detalles);
		cotizacion.setEstatus(EEstatusCotizacion.SOLICITADA);

		cotizacion = this.cotizacionRepository.save(cotizacion);
		for(DetalleCotizacion detalle : detalles)
			this.detalleCotizacionRepository.saveAndFlush(detalle);

		return cotizacion;
	}
	
	//Detalles Cotizacion
	public List<DetalleCotizacion> consultarDetallesCotizacion(int idCotizacion) {
		return detalleCotizacionRepository.findByCotizacion_IdCotizacion(idCotizacion);
	}
	
	public Map<DetalleRequerimiento, List<DetalleCotizacion>> consultarDetallesCotizacionEmitidos(Integer idRequerimiento){
		Specification<DetalleCotizacion> specfDetalleCotizacion = (new DetalleCotizacionDAO())
				.consultarDetallesCotizacionEmitidos(idRequerimiento);
		
		List<DetalleCotizacion> detallesCotizacion = this.detalleCotizacionRepository.findAll(specfDetalleCotizacion);
				
		List<DetalleCotizacion> detallesEliminar = new ArrayList<DetalleCotizacion>();
		
		Map<Integer, DetalleCotizacion> detallesIncluir = new HashMap<Integer, DetalleCotizacion>();
		for(int i=0; i<detallesCotizacion.size(); i++){
			DetalleCotizacion detalle = detallesCotizacion.get(i);
			DetalleCotizacionInternacional detalleInter = this.detalleCotizacionInternacionalRepository.findOne(detalle.getIdDetalleCotizacion());
			if(detalleInter != null){
				detallesEliminar.add(detalle);
				detallesIncluir.put(i, detalleInter);
			}
		}
		
		if(!detallesIncluir.isEmpty())
			for(Integer index : detallesIncluir.keySet())
				detallesCotizacion.add(index, detallesIncluir.get(index));
		
		if(!detallesEliminar.isEmpty()){
			detallesCotizacion = new ArrayList<DetalleCotizacion>(detallesCotizacion);
			detallesCotizacion.removeAll(detallesEliminar);
		}
				
		Map<DetalleRequerimiento, List<DetalleCotizacion>> parametros = new LinkedHashMap<DetalleRequerimiento, List<DetalleCotizacion>>();
		for(int i=0; i<detallesCotizacion.size(); i++){
			DetalleCotizacion detalle = detallesCotizacion.get(i);
			if (parametros.get(detalle.getDetalleRequerimiento()) == null)
					parametros.put(detalle.getDetalleRequerimiento(), new ArrayList<DetalleCotizacion>());
			parametros.get(detalle.getDetalleRequerimiento()).add(detalle);
		}
		return parametros;
	}
	
		//Internacional
	public List<DetalleCotizacionInternacional> consultarDetallesCotizacion(Integer idCotizacion) {
		return this.detalleCotizacionInternacionalRepository.findByCotizacion_IdCotizacion(idCotizacion);
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
	
	public Oferta actualizarOferta(Oferta oferta) {
		return oferta = ofertaRepository.save(oferta);
	}
	
	public List<Oferta> consultarOfertasNoEnviada(Requerimiento requerimiento){
		EEstatusOferta[] estatus = new EEstatusOferta[]{EEstatusOferta.SELECCIONADA, EEstatusOferta.INVALIDA};
		List<Oferta> ofertas = this.ofertaRepository
				.findDistinctIdOfertaByDetalleOfertas_DetalleCotizacion_DetalleRequerimiento_RequerimientoAndEstatusIn(requerimiento, estatus);
		if(!ofertas.isEmpty())
			for(int i=0; i<ofertas.size(); i++){
				Oferta oferta = ofertas.get(i);
				oferta.setNroOferta(i+1);
				oferta.setDetalleOfertas(detalleOfertaRepository.findByOferta(oferta));
			}
		return ofertas;
	}
	
	//Detalles Oferta
	public List<DetalleOferta> consultarDetallesOferta(Oferta oferta){
		return detalleOfertaRepository.findByOferta(oferta);
	}
	
	public Map<String, Object> consultarSolicitudesCompraProveedor(Requerimiento requerimiento, Proveedor proveedor, int page, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleOferta> detallesOferta = null;
		
		if(limit>0){
			Page<DetalleOferta> pageDetalle = detalleOfertaRepository.findByCompraRequerimientoAndDetalleCotizacion_Cotizacion_Proveedor(requerimiento, proveedor, new PageRequest(0, 1));
			total = Long.valueOf(pageDetalle.getTotalElements()).intValue();
			detallesOferta = pageDetalle.getContent();
		}
		else {
			detallesOferta = detalleOfertaRepository.findByCompraRequerimientoAndDetalleCotizacion_Cotizacion_Proveedor(requerimiento, proveedor);
			total = detallesOferta.size();
		}
		
		parametros.put("total", total);
		parametros.put("detallesOferta", detallesOferta);
		return parametros;
	}

	//Compras
	public Map<String, Object> consultarComprasPorRequerimiento(Compra compraF, int idRequerimiento, String fieldSort, Boolean sortDirection,
			int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Compra> compras = null;
		Sort sortCompra = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idCompra"));
		Specification<Compra> specfCompra = (new CompraDAO()).consultarComprasPorRequerimiento(compraF, idRequerimiento);
		
		if(limit>0){
			Page<Compra> pageCompra = this.compraRepository.findAll(specfCompra, new PageRequest(page, limit, sortCompra));
			total = Long.valueOf(pageCompra.getTotalElements()).intValue();
			compras = pageCompra.getContent();
		}
		else {
			compras = this.compraRepository.findAll(specfCompra, sortCompra);
			total = compras.size();
		}
		
		parametros.put("total", total);
		parametros.put("compras", compras);
		return parametros;
	}
	
	public Compra registrarOActualizarCompra(Compra compra){			
		return compra=this.compraRepository.save(compra);
	}
	
	public Compra registrarSolicitudCompra(Compra compra) {
		compra.setEstatus(EEstatusCompra.SOLICITADA);
		return registrarOActualizarCompra(compra);
	}
	
	public Compra registrarCompra(Compra compra, Requerimiento requerimiento,  boolean cambiarEstatus) {
		if(cambiarEstatus)
			requerimiento.setEstatus((compra.getTipoFlete()) 
					? EEstatusRequerimiento.CONCRETADO : EEstatusRequerimiento.COMPRADO); 
		actualizarRequerimiento(requerimiento);
		List<DetalleOferta> detalleCompra = compra.getDetalleOfertas();
		compra.setDetalleOfertas(null);
		compra.setEstatus(EEstatusCompra.ENVIADA);
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
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<DetalleOferta> detallesCompra = null;
		Sort sortCompra = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idDetalleOferta"));	
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
	
	//OrdenCompra
	public Map<String, Object> consultarOrdenesCompraProveedor(OrdenCompra ordenCompra, Requerimiento requerimiento, 
			String fieldSort, Boolean sortDirection, int page, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<OrdenCompra> ordenesCompra = null;
		Sort sortOrdenCompra = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "idOrdenCompra"));
		Specification<OrdenCompra> specfOrdenCompra = new OrdenCompraDAO().consultarOrdenesCompra(ordenCompra, requerimiento);
		if(limit>0){
			Page<OrdenCompra> pageOrdenCompra 
			= this.ordenCompraRepository.findAll(specfOrdenCompra, new PageRequest(page, limit, sortOrdenCompra));
			total = Long.valueOf(pageOrdenCompra.getTotalElements()).intValue();
			ordenesCompra = pageOrdenCompra.getContent();
		}
		else {
			ordenesCompra = this.ordenCompraRepository.findAll(specfOrdenCompra);
			total = ordenesCompra.size();
		}
		if(ordenesCompra!=null && !ordenesCompra.isEmpty()){
			Requerimiento requerimientoF;
			for(OrdenCompra ordenCompraF : ordenesCompra){
				requerimientoF = this.requerimientoRepository.findAll(new RequerimientoDAO().consultarPorOrdenCompra(ordenCompraF)).get(0);
				ordenCompraF.setRequerimiento(requerimientoF);
				ordenCompraF.setDetalleOfertas(this.detalleOfertaRepository.findByOrdenCompra(ordenCompraF));
			}
		}
		parametros.put("total", total);
		parametros.put("ordenesCompra", ordenesCompra);
		return parametros;
		
	}
	
	public void guardarOrdenCompra(Compra compra, SControlConfiguracion sControlConfiguracion){
        Map<Proveedor, List<DetalleOferta>> map = compra.getMap();
        System.out.println("Proveedores: "+map.keySet().size());
        //Generamos la orden de compra
        Configuracion configuracion = sControlConfiguracion.consultarConfiguracionActual();
        OrdenCompra ordenCompra = new OrdenCompra();
        for(Proveedor proveedor : map.keySet()){
        	ordenCompra = new OrdenCompra(EEstatusOrdenCompra.CREADA);
        	ordenCompra.setIva(configuracion.getPorctIva());
            List<DetalleOferta> values = map.get(proveedor);
            this.ordenCompraRepository.save(ordenCompra);
            ordenCompra.setDetalleOfertas(values);
            for(DetalleOferta detalle : ordenCompra.getDetalleOfertas())
            	this.detalleOfertaRepository.save(detalle);
        }
    }
	
	// PAGOS
		@Override
		public Map<String, Object> consultarPagosClientes(PagoCliente pagoFiltro,  String fieldSort, Boolean sortDirection, 
				 int page, int limit) {
			Map<String, Object> parametros = new HashMap<String, Object>();
			Integer total = 0;
			List<PagoCliente> pagoClientes = null;
			Sort sortPagosCliente = new Sort(getDirection(sortDirection,
					Sort.Direction.ASC), getFieldSort(fieldSort, "id"));
			Specification<PagoCliente> specfPagoCliente = (new PagoClienteDAO())
					.consultarPagoCliente(pagoFiltro);
			if (limit > 0) {
				Page<PagoCliente> pagePagoCliente = this.pagoClienteRepository
						.findAll(specfPagoCliente, new PageRequest(page, limit,sortPagosCliente));
				total = Long.valueOf(pagePagoCliente.getTotalElements()).intValue();
				pagoClientes = pagePagoCliente.getContent();
			} else {
				pagoClientes = this.pagoClienteRepository.findAll(specfPagoCliente,sortPagosCliente);
				total = pagoClientes.size();
			}
			parametros.put("total", total);
			parametros.put("pagoClientes", pagoClientes);
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
