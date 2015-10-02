package com.okiimport.app.service.mail;

import java.util.List;

import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.Proveedor;

public interface MailProveedor {
	void registrarSolicitudProveedor(final Proveedor proveedor, final MailService mailService);
	void enviarRequerimientoProveedor(final Proveedor proveedor, final List<DetalleCotizacion> detallesCotizacion, 
			final MailService mailService);
}
