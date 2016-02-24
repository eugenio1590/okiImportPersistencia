package com.okiimport.app.dao.pago;

import com.okiimport.app.model.PagoProveedor;

import com.okiimport.app.resource.dao.IGenericJPARepository;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface PagoProveedorRepository extends IGenericJPARepository<PagoProveedor, Integer> {
	
	
}


