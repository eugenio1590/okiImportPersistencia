package com.okiimport.app.dao.transaccion.detalle.requerimiento;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface DetalleRequerimientoRepository extends IGenericJPARepository<DetalleRequerimiento, Integer> {

}
