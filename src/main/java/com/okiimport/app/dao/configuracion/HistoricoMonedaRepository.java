package com.okiimport.app.dao.configuracion;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.Moneda;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface HistoricoMonedaRepository extends IGenericJPARepository<HistoricoMoneda, Integer> {
	HistoricoMoneda findByMonedaAndEstatus(Moneda moneda, EEstatusGeneral estatus, Sort sort);
	HistoricoMoneda findByMonedaPaisTrueAndEstatus(EEstatusGeneral estatus);
}
