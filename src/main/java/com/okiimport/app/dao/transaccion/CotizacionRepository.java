package com.okiimport.app.dao.transaccion;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface CotizacionRepository extends IGenericJPARepository<Cotizacion, Integer> {

}
