package com.okiimport.app.service.mail.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.service.mail.MailProveedor;
import com.okiimport.app.service.mail.MailService;

public class MailProveedorImpl extends AbstractMailImpl implements MailProveedor {

	@Override
	public void registrarSolicitudProveedor(final Proveedor proveedor, final MailService mailService) {
		super.sendMail(new Runnable(){
			@Override
			public void run() {
				try {
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("fechaEnvio", dateFormat.format(calendar.getTime()));
					model.put("proveedor", proveedor);

					mailService.send(proveedor.getCorreo(), "Registro de Solicitud de Proveedor",
							"registrarProveedor.html", model);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void enviarRequerimientoProveedor(final Proveedor proveedor, final Requerimiento requerimiento,
			final List<DetalleCotizacion> detallesCotizacion, final MailService mailService) {
		super.sendMail(new Runnable(){
			@Override
			public void run() {
				try {
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("fechaCreacion", dateFormat.format(requerimiento.getFechaCreacion()));
					model.put("fechaVencimiento", dateFormat.format(requerimiento.getFechaVencimiento()));
					model.put("fechaEnvio", dateFormat.format(calendar.getTime()));
					model.put("proveedor", proveedor);
					model.put("requerimiento", requerimiento);
					model.put("detallesCotizacion", detallesCotizacion);

					mailService.send(proveedor.getCorreo(), "Solicitud de Cotizacion",
							"enviarRequisitoProveedor.html", model);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void enviarRecotizacionProveedor(final Proveedor proveedor, final Requerimiento requerimiento, 
			final Cotizacion cotizacion, final MailService mailService) {
		super.sendMail(new Runnable(){
			@Override
			public void run() {
				try {
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("fechaCreacion", dateFormat.format(requerimiento.getFechaCreacion()));
					model.put("fechaVencimiento", dateFormat.format(requerimiento.getFechaVencimiento()));
					model.put("requerimiento", requerimiento);

					mailService.send(proveedor.getCorreo(), "Solicitud de Recotizacion",
							"enviarRecotizacionProveedor.html", model);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void enviarCotAprobadaProveedor(final Proveedor proveedor, final Requerimiento requerimiento,
			final Cotizacion cotizacion, final MailService mailService) {
		super.sendMail(new Runnable(){
			@Override
			public void run() {
				try {
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("proveedor", proveedor);
					model.put("requerimiento", requerimiento);
					model.put("cotizacion", cotizacion);

					mailService.send(proveedor.getCorreo(), "Aprobacion de Cotizacion",
							"enviarCotAprobadaProveedor.html", model);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}
