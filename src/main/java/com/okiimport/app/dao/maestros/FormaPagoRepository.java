package com.okiimport.app.dao.maestros;


import com.okiimport.app.model.FormaPago;

import com.okiimport.app.resource.dao.IGenericJPARepository;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface FormaPagoRepository extends IGenericJPARepository<FormaPago, Integer> {
	
	
}
