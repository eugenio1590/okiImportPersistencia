package com.okiimport.app.dao.transaccion;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Venta;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface VentaRepository extends IGenericJPARepository<Venta, Integer> {

}
