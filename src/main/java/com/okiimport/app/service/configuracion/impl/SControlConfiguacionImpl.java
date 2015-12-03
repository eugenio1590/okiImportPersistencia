package com.okiimport.app.service.configuracion.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.okiimport.app.dao.configuracion.ConfiguracionRepository;
import com.okiimport.app.dao.configuracion.HistoricoMonedaRepository;
import com.okiimport.app.dao.configuracion.MonedaRepository;
import com.okiimport.app.dao.configuracion.impl.HistoricoMonedaDAO;
import com.okiimport.app.dao.configuracion.impl.MonedaDAO;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.configuracion.SControlConfiguracion;
import com.okiimport.app.model.Configuracion;
import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.Moneda;

@Service
public class SControlConfiguacionImpl extends AbstractServiceImpl implements SControlConfiguracion {

	@Autowired
	private MonedaRepository monedaRepository;
	
	@Autowired
	private HistoricoMonedaRepository historicoMonedaRepository;
	
	@Autowired
	private ConfiguracionRepository configuracionRepository;
	
	public SControlConfiguacionImpl() {
	}
	
	//Configuracion
	public Configuracion consultarConfiguracionActual() {
		Page<Configuracion> configuraciones = configuracionRepository.findAll(new PageRequest(0, 1));
		return configuraciones.getContent().get(0);
	}
	
	public void guardarConfiguracion(Configuracion configuracion, Moneda monedaBase){
		configuracionRepository.save(configuracion);
		
		Moneda monedaBaseAnt = monedaRepository.findByPaisTrue();
		if(monedaBaseAnt!=null){
			monedaBaseAnt.setPais(false);
			monedaRepository.save(monedaBaseAnt);
		}
		
		monedaBase.setPais(true);
		monedaRepository.save(monedaBase);
	}

	//Historico de Moneda
	public Map<String, Object> consultarMonedasConHistorico(int page, int limite) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Moneda> monedas = null;
		Sort sortMoneda = new Sort(getDirection(true, null), "idMoneda");
		Specification<Moneda> specfMoneda = (new MonedaDAO()).consultarMonedasConHistorico("activo");
		if(limite > 0){
			Page<Moneda> pageMoneda = this.monedaRepository.findAll(specfMoneda, new PageRequest(page, limite, sortMoneda));
			total = Long.valueOf(pageMoneda.getTotalElements()).intValue();
			monedas = pageMoneda.getContent();
		}
		else {
			monedas = monedaRepository.findAll(specfMoneda, sortMoneda);
			total = monedas.size();
		}
		parametros.put("total", total);
		parametros.put("monedas", monedas);
		return parametros;
	}
	
	public HistoricoMoneda consultarActualConversion(Moneda moneda) {
		Integer idMoneda = moneda.getIdMoneda();
		Sort sortHistoricoMoneda = new Sort(getDirection(false, null), "fechaCreacion")
											.and(new Sort(getDirection(true, null), "idHistoria"));
		Specification<HistoricoMoneda> specfHistoricoMoneda = (new HistoricoMonedaDAO()).consultarActualConversion(idMoneda);
		List<HistoricoMoneda> historicoMonedas = this.historicoMonedaRepository.findAll(specfHistoricoMoneda, sortHistoricoMoneda);
		if(historicoMonedas!=null && !historicoMonedas.isEmpty())
			return historicoMonedas.get(0);
		return null;
	}
}
