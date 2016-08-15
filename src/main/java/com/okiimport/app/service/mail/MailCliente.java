package com.okiimport.app.service.mail;

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.Requerimiento;

public interface MailCliente {
	void registrarRequerimiento(Requerimiento requerimiento, MailService mailService);
	void enviarOfertas(Requerimiento requerimiento,MailService mailService);
	void enviarCotizacionCliente(Requerimiento requerimiento,MailService mailService);
	void enviarInformacionCompra(Compra compra, MailService mailService);
}
