package com.okiimport.app.service.mail.impl;

import java.util.HashMap;
import java.util.Map;

import com.okiimport.app.model.Usuario;
import com.okiimport.app.service.mail.MailUsuario;
import com.okiimport.app.service.mail.MailService;

public class MailUsuarioImpl extends AbstractMailImpl implements MailUsuario {

	public void enviarUsuarioyPassword(final Usuario usuario, final MailService mailService) {
		super.sendMail(new Runnable(){
			@Override
			public void run() {
				try {
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("usuario", usuario);

					mailService.send(usuario.getPersona().getCorreo(), "Bienvenido a Ubicapartes. ",
							"enviarUsuarioYClave.html", model);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}