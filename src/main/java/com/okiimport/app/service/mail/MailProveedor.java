package com.okiimport.app.service.mail;

import com.okiimport.app.model.Proveedor;

public interface MailProveedor {
	void registrarSolicitudProveedor(final Proveedor proveedor, MailService mailService);
}
