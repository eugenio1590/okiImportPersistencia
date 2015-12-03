package com.okiimport.app.dao.configuracion;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Moneda;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface MonedaRepository extends IGenericJPARepository<Moneda, Integer> {
	Moneda findByPaisTrue();
}
