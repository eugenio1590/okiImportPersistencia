package com.okiimport.app.dao.transaccion;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface RequerimientoRepository extends IGenericJPARepository<Requerimiento, Integer> {

}
