package com.okiimport.app.service.configuracion;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.okiimport.app.model.Configuracion;
import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.Moneda;

@Service
@Transactional
public interface SControlConfiguracion {
	//Configuracion
	Configuracion consultarConfiguracionActual();
	
	//Historico de Moneda
	Map<String, Object> consultarMonedasConHistorico(int page, int limite);
	HistoricoMoneda consultarActualConversion(Moneda moneda);
}
