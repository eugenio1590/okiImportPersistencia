package com.okiimport.app.dao.configuracion;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Configuracion;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface ConfiguracionRepository extends IGenericJPARepository<Configuracion, Integer> {

}
