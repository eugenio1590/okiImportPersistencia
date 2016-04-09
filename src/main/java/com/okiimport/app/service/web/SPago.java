package com.okiimport.app.service.web;

import com.braintreegateway.BraintreeGateway;
import com.okiimport.app.model.Compra;

public interface SPago {
	
	Boolean guardarPago(BraintreeGateway gateway, float amount, String nonceFromTheClient, Compra compra);

}
