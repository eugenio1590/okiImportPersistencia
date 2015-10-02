package com.okiimport.app.service.mail.impl;

import java.util.HashMap;
import java.util.Map;

import com.okiimport.app.model.Proveedor;
import com.okiimport.app.service.mail.MailProveedor;
import com.okiimport.app.service.mail.MailService;

public class MailProveedorImpl extends AbstractMailImpl implements MailProveedor {

	public void registrarSolicitudProveedor(final Proveedor proveedor, final MailService mailService) {
		super.sendMail(new Runnable(){
			@Override
			public void run() {
				try {
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("fechaEnvio", dateFormat.format(calendar.getTime()));
					model.put("proveedor", proveedor);

					mailService.send(proveedor.getCorreo(), "Registro de Solicitud de Proveedor",
							"registrarRequerimiento.html", model);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}
