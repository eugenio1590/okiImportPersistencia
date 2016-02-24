package com.okiimport.app.dao.transaccion;

import com.okiimport.app.model.OrdenCompra;

import com.okiimport.app.resource.dao.IGenericJPARepository;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface OrdenCompraRepository extends IGenericJPARepository<OrdenCompra, Integer> {
	
	
}

