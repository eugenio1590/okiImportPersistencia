package com.okiimport.app.service.web.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.okiimport.app.dao.pago.PagoClienteRepository;
import com.okiimport.app.dao.transaccion.CompraRepository;
import com.okiimport.app.dao.transaccion.detalle.oferta.DetalleOfertaRepository;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.Deposito;
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.Pago;
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.model.enumerados.EEstatusCompra;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.configuracion.SControlConfiguracion;
import com.okiimport.app.service.web.SPago;

public class SPagoImpl extends AbstractServiceImpl implements SPago{
	
	private static DecimalFormat decimalFormat;

	@Autowired
	private PagoClienteRepository pagoRepository;
	
	@Autowired 
	private CompraRepository compraRepository;
	
	@Autowired
	private DetalleOfertaRepository detalleOfertaRepository;
	

	public Boolean guardarPagoCliente(SControlConfiguracion sControlConfiguracion, BraintreeGateway gateway, PagoCliente pagoCliente) {
		Boolean valor=false;
		Result<Transaction> result = crearTransaccion(sControlConfiguracion, gateway, pagoCliente);
		System.out.println("Result: "+result.isSuccess()+" "+result.getMessage());

		if(valor=result.isSuccess()){
			
			/*Compra compra = pagoCliente.getCompra();
			compra.setIdCompra(null);
			compra.setEstatus(EEstatusCompra.PAGADA);
			compraRepository.save(compra);
			for(DetalleOferta detalle : pagoCliente.getCompra().getDetalleOfertas()){
				detalle.setCompra(compra);
				detalleOfertaRepository.save(detalle);
			}*/
			//Por ahora para evitar errores
			PagoCliente p = new PagoCliente();
			this.compraRepository.save(pagoCliente.getCompra());
			p.setCompra(pagoCliente.getCompra());
			p.setFechaPago(pagoCliente.getFechaPago());
			p.setMonto(pagoCliente.getMonto());
			p.setEstatus(pagoCliente.getEstatus());
			p.setDescripcion(pagoCliente.getDescripcion());
			p.setFormaPago(pagoCliente.getFormaPago());
			this.pagoRepository.save(p);
			
			// donde deberia crearse el atributo idTransaccion?? si en modelo Pago o PagoCliente
			String idTransaccion=result.getTarget().getId();
			pagoCliente.setTransactionId(idTransaccion);
			
			String estatus=result.getTarget().getStatus().toString();
			pagoCliente.setEstatus(estatus);
			
			pagoCliente.setFechaPago(this.calendar.getTime());
			pagoCliente.setDescripcion("Pago de Compra Nro. "+pagoCliente.getCompra().getIdCompra());
			//falta otros atributos para setear en el objeto pagoCliente
			//como banco, forma de pago
			
			pagoRepository.save(pagoCliente);
		}
		else
		{
			for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
			    System.out.println(error.getCode());
			    System.out.println(error.getMessage());
			}
		}
		return valor;
	}
	
	/**METODOS ESTATICOS DE LA CLASE*/
	private static DecimalFormat getDecimalFormat(){
		if(decimalFormat==null){
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
	        symbols.setDecimalSeparator('.');
	        symbols.setGroupingSeparator(',');
	        decimalFormat = new DecimalFormat("#.##", symbols);
		}
		return decimalFormat;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	//Metodos Privados
	private Result<Transaction> crearTransaccion(SControlConfiguracion sControlConfiguracion, BraintreeGateway gateway, Pago pago){
        DecimalFormat decimalFormat = getDecimalFormat();
        
		HistoricoMoneda historico = sControlConfiguracion.consultarActualConversionMonedaBase();
		System.out.println(new BigDecimal((float) historico.convert(pago.getMonto())).toPlainString());
		System.out.println(pago.getPaymentMethodNonce());
		TransactionRequest request = new TransactionRequest()
			.amount(new BigDecimal(decimalFormat.format((float) historico.convert(pago.getMonto()))))
			.paymentMethodNonce(pago.getPaymentMethodNonce())
			.options()
				.submitForSettlement(true)
				.done();

		return gateway.transaction().sale(request);
	}

}
