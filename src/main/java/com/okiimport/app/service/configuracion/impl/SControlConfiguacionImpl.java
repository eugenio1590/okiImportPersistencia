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
import com.okiimport.app.dao.configuracion.impl.MonedaDAO;
import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Configuracion;
import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.Moneda;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.configuracion.SControlConfiguracion;

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
		System.out.println("sc"+configuraciones.getContent().get(0));
		System.out.println("sc iva "+configuraciones.getContent().get(0).getPorctIva());
		return configuraciones.getContent().get(0);
	}
	
	public void guardarConfiguracion(Configuracion configuracion){
		configuracionRepository.save(configuracion);
		
		
	}

	//Moneda
	public Map<String, Object> consultarMonedasConHistorico(int page, int limite) {
		System.out.println("*********");
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Moneda> monedas = null;
		Sort sortMoneda = new Sort(getDirection(true, null), "idMoneda");
		Specification<Moneda> specfMoneda = (new MonedaDAO()).consultarMonedasConHistorico(EEstatusGeneral.ACTIVO);
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
	
	public Moneda consultarMonedaBase(){
		return monedaRepository.findByPaisTrue();
	}
	
	//Historico de Moneda
	public HistoricoMoneda consultarActualConversion(Moneda moneda) {
		Sort sortHistoricoMoneda = new Sort(getDirection(false, null), "fechaCreacion")
											.and(new Sort(getDirection(true, null), "idHistoria"));
		return this.historicoMonedaRepository.findByMonedaAndEstatus(moneda, EEstatusGeneral.ACTIVO, sortHistoricoMoneda);
	}
	
	public HistoricoMoneda consultarActualConversion(Persona persona) {
		HistoricoMoneda historico = null;
		
		if(persona instanceof Proveedor)
			historico = this.consultarActualConversion(((Proveedor) persona).getPais().getMoneda());
		else if(persona instanceof Analista || persona instanceof Cliente)
			historico = consultarActualConversionMonedaBase();
		
		persona.setHistoricoMoneda(historico);
		return historico;
	}

	public HistoricoMoneda consultarActualConversionMonedaBase() {
		return historicoMonedaRepository.findByMonedaPaisTrueAndEstatus(EEstatusGeneral.ACTIVO);
	}
}
