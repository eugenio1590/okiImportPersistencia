package com.okiimport.app.dao.configuracion;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface HistoricoMonedaRepository extends IGenericJPARepository<HistoricoMoneda, Integer> {

	
}
