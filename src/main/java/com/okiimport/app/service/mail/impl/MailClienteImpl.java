package com.okiimport.app.service.mail.impl;

import java.util.HashMap;
import java.util.Map;

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.service.mail.MailCliente;
import com.okiimport.app.service.mail.MailService;

public class MailClienteImpl extends AbstractMailImpl implements MailCliente {

	public void registrarRequerimiento(final Requerimiento requerimiento, final MailService mailService) {
		super.sendMail(new Runnable(){
			@Override
			public void run() {
				try {
					final Cliente cliente = requerimiento.getCliente();
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("fechaEnvio", dateFormat.format(calendar.getTime()));
					model.put("cliente", cliente);
					model.put("requerimiento", requerimiento);

					mailService.send(cliente.getCorreo(), "Registro de Requerimiento Nro. "+requerimiento.getIdRequerimiento(),
							"registrarRequerimiento.html", model);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	public void enviarOfertas(final Requerimiento requerimiento, final MailService mailService) {
		super.sendMail(new Runnable(){
			@Override
			public void run() {
				try {
					final Cliente cliente = requerimiento.getCliente();
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("fechaEnvio", dateFormat.format(calendar.getTime()));
					model.put("cliente", cliente);
					model.put("requerimiento", requerimiento);

					mailService.send(cliente.getCorreo(), "Tiene una Oferta en su Requerimiento Nro. "+requerimiento.getIdRequerimiento(),
							"enviarOfertas.html", model);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	public void enviarCotizacionCliente(final Requerimiento requerimiento, final MailService mailService) {
		super.sendMail(new Runnable(){
			@Override
			public void run() {
				try {
					final Cliente cliente = requerimiento.getCliente();
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("cliente", cliente);
					model.put("requerimiento", requerimiento);

					mailService.send(cliente.getCorreo(), "Tiene una Cotizacion en su Requerimiento Nro. "+requerimiento.getIdRequerimiento(),
							"enviarCotizacionCliente.html", model);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}
