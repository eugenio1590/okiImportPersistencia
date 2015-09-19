package com.okiimport.app.dao.transaccion;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Oferta;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface OfertaRepository extends IGenericJPARepository<Oferta, Integer> {

}
