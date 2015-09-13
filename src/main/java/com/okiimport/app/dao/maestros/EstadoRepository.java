package com.okiimport.app.dao.maestros;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Estado;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface EstadoRepository extends IGenericJPARepository<Estado, Integer> {

}
