package com.okiimport.app.service.mail;

import java.util.List;

import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Requerimiento;

public interface MailProveedor {
	void registrarSolicitudProveedor(final Proveedor proveedor, final MailService mailService);
	void enviarRequerimientoProveedor(final Proveedor proveedor, final Requerimiento requerimiento, 
			final List<DetalleCotizacion> detallesCotizacion, final MailService mailService);
	void enviarCotAprobadaProveedor(final Proveedor proveedor, final Requerimiento requerimiento, 
			final Cotizacion cotizacion, final MailService mailService);
}
