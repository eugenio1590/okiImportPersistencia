package com.okiimport.app.dao.transaccion;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface RequerimientoRepository extends IGenericJPARepository<Requerimiento, Integer> {
	List<Requerimiento> findByAnalistaAndEstatusIn(Analista Analista,List<EEstatusRequerimiento> estatus);

}
