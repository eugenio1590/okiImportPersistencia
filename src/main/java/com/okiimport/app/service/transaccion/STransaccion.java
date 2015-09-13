package com.okiimport.app.service.transaccion;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
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
	Requerimiento registrarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros);
	Requerimiento actualizarRequerimiento(Requerimiento requerimiento);
	void asignarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros);
	Map<String, Object> consultarRequerimientosGeneral(Requerimiento regFiltro, String fieldSort, Boolean sortDirection,
			int pagina, int limit);
	
	Map<String, Object> consultarMisRequerimientosEmitidos(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);
	
	Map<String, Object> consultarMisRequerimientosProcesados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);
	
	Map<String, Object> consultarMisRequerimientosOfertados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);

	Map<String, Object> ConsultarRequerimientosCliente(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, String cedula,
			int pagina, int limit);
	
	Map<String, Object> RequerimientosCotizados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);
	

	Map<String, Object> ConsultarCotizacionesRequerimiento(Cotizacion cotFiltro, String fieldSort, Boolean sortDirection, Integer idrequerimiento,
			int pagina, int limit);

	Map <String, Object> ConsultarRequerimientosConSolicitudesCotizacion(Requerimiento regFiltro, String fieldSort, 
			Boolean sortDirection, Integer idProveedor, int pagina, int limit);
	
	//DetalleRequerimiento
	DetalleRequerimiento registrarDetalleRequerimiento(int idRequerimiento, DetalleRequerimiento detalleRequerimiento);
	
	//Cotizaciones
	Map<String, Object> consultarSolicitudCotizaciones(Cotizacion cotizacionF, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, int idProveedor, int pagina, int limit);
	Cotizacion ActualizarCotizacion(Cotizacion cotizacion);
	Cotizacion registrarSolicitudCotizacion(Cotizacion cotizacion, List<DetalleCotizacion> detalleCotizacions);
	Cotizacion registrarCotizacion(Cotizacion cotizacion, Requerimiento requerimiento);
	Map<String, Object> consultarCotizacionesParaEditar(Cotizacion cotizacionF, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, int pagina, int limit);
	
	//Detalle de Cotizaciones
	void guardarSeleccionRequerimiento(List<DetalleCotizacion> detalleCotizaciones);
	Map<String, Object> consultarDetallesCotizacion(DetalleCotizacion detalleF, int idCotizacion,
			String fieldSort, Boolean sortDirection, int pagina, int limit);
	Map<String, Object> consultarDetallesCotizacion(DetalleCotizacion detalleF, Integer idRequerimiento,
			String fieldSort, Boolean sortDirection, int pagina, int limit);
	Map <String, Object> ConsultarDetalleCotizacion(Integer idcotizacion,int pagina, int limit);
	
		//Internacional
	Map<String, Object> consultarDetallesCotizacion(DetalleCotizacionInternacional detalleF, int idCotizacion,
			String fieldSort, Boolean sortDirection, int pagina, int limit);
	
	//Ofertas
	Map<String, Object> consultarOfertasPorRequerimiento(int idRequerimiento, String fieldSort, Boolean sortDirection, 
			int pagina, int limit);
	Map<String, Object> consultarOfertasRecibidasPorRequerimiento(int idRequerimiento, int pagina, int limit);
	Oferta consultarOfertaEnviadaPorRequerimiento(int idRequerimiento);
	Oferta actualizarOferta(Oferta oferta);
	List<DetalleOferta> consultarDetallesOferta(Integer idOferta);
	
	//Compras
	Map<String, Object> consultarComprasPorRequerimiento(Compra compraF, int idRequerimiento, String fieldSort, Boolean sortDirection,
			int pagina, int limite);
	Compra registrarOActualizarCompra(Compra compra);
	Compra registrarSolicitudCompra(Compra compra);
	Compra registrarCompra(Compra compra);
	
	//DetalleCompra
	Map<String, Object> consultarDetallesCompra(int idCompra, String fieldSort, Boolean sortDirection, 
			int pagina, int limite);
}
