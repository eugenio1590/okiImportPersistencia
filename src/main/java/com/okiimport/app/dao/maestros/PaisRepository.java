package com.okiimport.app.dao.maestros;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Pais;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface PaisRepository extends IGenericJPARepository<Pais, Integer> {

}
