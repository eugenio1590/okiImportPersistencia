package com.okiimport.app.dao.maestros;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.FormaPago;
import com.okiimport.app.model.enumerados.EEstatusFormaPago;
import com.okiimport.app.resource.dao.IGenericJPARepository;


@Repository
public interface FormaPagoRepository extends IGenericJPARepository<FormaPago, Integer> {
	
	List<FormaPago> findByEstatus(EEstatusFormaPago estatus);
	Page<FormaPago> findByEstatus(EEstatusFormaPago estatus, Pageable pageable);
	
}
