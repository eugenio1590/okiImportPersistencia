package com.okiimport.app.service.web.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.okiimport.app.dao.pago.PagoClienteRepository;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.service.web.SPago;

public class SPagoImpl implements SPago{
	
	@Autowired
	private PagoClienteRepository pagoRepository;

	
	public Boolean guardarPago(BraintreeGateway gateway, float amount, String nonceFromTheClient, Compra compra) {
		Boolean valor=false;
		try{
				TransactionRequest request = new TransactionRequest()
			    .amount(new BigDecimal(amount))
			    .paymentMethodNonce(nonceFromTheClient)
			    .options()
			        .submitForSettlement(true)
			        .done();
		
				Result<Transaction> result = gateway.transaction().sale(request);
				
				if(result.isSuccess()){
					
					String estatus=result.getTarget().getStatus().toString();
					// donde deberia crearse el atributo idTransaccion?? si en modelo Pago o PagoCliente
					String idTransaccion=result.getTarget().getId();
					
					Date fechaTrans=Calendar.getInstance().getTime();
					PagoCliente pago= new PagoCliente();
					pago.setCompra(compra);
					pago.setFechaCreacion(fechaTrans);
					pago.setFechaPago(fechaTrans);
					pago.setEstatus(estatus);
					//falta otros atributos para setear en el objeto pagoCliente
					//como banco, forma de pago
					
					pagoRepository.save(pago);
					
					valor= true;
				}else{
					valor= false;
				}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	 return valor;
}
	
}
