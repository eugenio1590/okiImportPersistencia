package com.okiimport.app.dao.transaccion;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Compra;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface CompraRepository extends IGenericJPARepository<Compra, Integer> {

}
