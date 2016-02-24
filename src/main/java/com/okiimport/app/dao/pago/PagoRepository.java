package com.okiimport.app.dao.pago;


import com.okiimport.app.model.Pago;

import com.okiimport.app.resource.dao.IGenericJPARepository;

import org.springframework.data.repository.NoRepositoryBean;



@NoRepositoryBean
public interface PagoRepository<T extends Pago> extends IGenericJPARepository<T, Integer> {

	
	
}


