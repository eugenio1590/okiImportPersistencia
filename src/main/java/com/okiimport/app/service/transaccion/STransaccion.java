package com.okiimport.app.service.transaccion;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.DetalleCotizacionInternacional;
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.Requerimiento;

@Service
@Transactional
public interface STransaccion {
	
	//Requerimiento
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Requerimiento registrarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Requerimiento actualizarRequerimiento(Requerimiento requerimiento);
	
	@Transactional(readOnly=true)
	void asignarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarRequerimientosGeneral(Requerimiento regFiltro, String fieldSort, Boolean sortDirection,
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarMisRequerimientosEmitidos(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarMisRequerimientosProcesados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarMisRequerimientosOfertados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);

	@Transactional(readOnly=true)
	Map<String, Object> ConsultarRequerimientosCliente(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, String cedula,
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> RequerimientosCotizados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> ConsultarCotizacionesRequerimiento(Cotizacion cotFiltro, String fieldSort, Boolean sortDirection, Integer idrequerimiento,
			int pagina, int limit);

	@Transactional(readOnly=true)
	Map <String, Object> ConsultarRequerimientosConSolicitudesCotizacion(Requerimiento regFiltro, String fieldSort, 
			Boolean sortDirection, Integer idProveedor, int pagina, int limit);
	
	//DetalleRequerimiento
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	DetalleRequerimiento registrarDetalleRequerimiento(int idRequerimiento, DetalleRequerimiento detalleRequerimiento);
	
	Map<String, Object> consultarDetallesRequerimiento(int idRequerimiento, int pagina, int limit);
	
	//Cotizaciones
	@Transactional(readOnly=true)
	Map<String, Object> consultarSolicitudCotizaciones(Cotizacion cotizacionF, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, int idProveedor, int pagina, int limit);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Cotizacion ActualizarCotizacion(Cotizacion cotizacion);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Cotizacion registrarSolicitudCotizacion(Cotizacion cotizacion, List<DetalleCotizacion> detalleCotizacions);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Cotizacion registrarCotizacion(Cotizacion cotizacion, Requerimiento requerimiento);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarCotizacionesParaEditar(Cotizacion cotizacionF, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, int pagina, int limit);
	
	//Detalle de Cotizaciones
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	void guardarSeleccionRequerimiento(List<DetalleCotizacion> detalleCotizaciones);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarDetallesCotizacion(DetalleCotizacion detalleF, int idCotizacion,
			String fieldSort, Boolean sortDirection, int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarDetallesCotizacion(DetalleCotizacion detalleF, Integer idRequerimiento,
			String fieldSort, Boolean sortDirection, int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map <String, Object> ConsultarDetalleCotizacion(Integer idcotizacion,int pagina, int limit);
	
		//Internacional
	@Transactional(readOnly=true)
	Map<String, Object> consultarDetallesCotizacion(DetalleCotizacionInternacional detalleF, int idCotizacion,
			String fieldSort, Boolean sortDirection, int pagina, int limit);
	
	//Ofertas
	@Transactional(readOnly=true)
	Map<String, Object> consultarOfertasPorRequerimiento(int idRequerimiento, String fieldSort, Boolean sortDirection, 
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarOfertasRecibidasPorRequerimiento(int idRequerimiento, int pagina, int limit);
	
	@Transactional(readOnly=true)
	Oferta consultarOfertaEnviadaPorRequerimiento(int idRequerimiento);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Oferta actualizarOferta(Oferta oferta);
	
	@Transactional(readOnly=true)
	List<DetalleOferta> consultarDetallesOferta(Integer idOferta);
	
	//Compras
	@Transactional(readOnly=true)
	Map<String, Object> consultarComprasPorRequerimiento(Compra compraF, int idRequerimiento, String fieldSort, Boolean sortDirection,
			int pagina, int limite);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Compra registrarOActualizarCompra(Compra compra);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Compra registrarSolicitudCompra(Compra compra);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Compra registrarCompra(Compra compra);
	
	//DetalleCompra
	@Transactional(readOnly=true)
	Map<String, Object> consultarDetallesCompra(int idCompra, String fieldSort, Boolean sortDirection, 
			int pagina, int limite);
}
