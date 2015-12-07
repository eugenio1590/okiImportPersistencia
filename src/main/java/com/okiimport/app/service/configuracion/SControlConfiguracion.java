package com.okiimport.app.service.configuracion;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.okiimport.app.model.Configuracion;
import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.Moneda;
import com.okiimport.app.model.Persona;

@Service
@Transactional
public interface SControlConfiguracion {
	//Configuracion
	@Transactional(readOnly=true)
	Configuracion consultarConfiguracionActual();
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	void guardarConfiguracion(Configuracion configuracion, Moneda monedaBase);
	
	//Moneda
	@Transactional(readOnly=true)
	Map<String, Object> consultarMonedasConHistorico(int page, int limite);
	
	@Transactional(readOnly=true)
	Moneda consultarMonedaBase();
	
	//Historico de Moneda
	@Transactional(readOnly=true)
	HistoricoMoneda consultarActualConversion(Moneda moneda);
	
	@Transactional(readOnly=true)
	HistoricoMoneda consultarActualConversion(final Persona persona);
	
	@Transactional(readOnly=true)
	HistoricoMoneda consultarActualConversionMonedaBase();
}
