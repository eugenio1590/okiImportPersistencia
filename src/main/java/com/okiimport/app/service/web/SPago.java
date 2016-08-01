package com.okiimport.app.service.web;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.braintreegateway.BraintreeGateway;
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.service.configuracion.SControlConfiguracion;

public interface SPago {
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Boolean guardarPagoCliente(SControlConfiguracion sControlConfiguracion, BraintreeGateway gateway, PagoCliente pago);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Boolean registrarPagoEfectivo(PagoCliente pago);
}
