package com.okiimport.app.service.mail;

import com.okiimport.app.model.Usuario;

public interface MailUsuario {
	void enviarUsuarioyPassword(Usuario usuario, MailService mailService);
	
}
