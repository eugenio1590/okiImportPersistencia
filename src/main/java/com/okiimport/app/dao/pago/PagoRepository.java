package com.okiimport.app.dao.pago;


import com.okiimport.app.model.Pago;

import com.okiimport.app.resource.dao.IGenericJPARepository;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface PagoRepository extends IGenericJPARepository<Pago, Integer> {
	
	
}


