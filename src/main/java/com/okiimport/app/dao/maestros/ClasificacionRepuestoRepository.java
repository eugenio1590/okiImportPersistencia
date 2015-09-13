package com.okiimport.app.dao.maestros;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.ClasificacionRepuesto;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface ClasificacionRepuestoRepository extends IGenericJPARepository<ClasificacionRepuesto, Integer> {

}
