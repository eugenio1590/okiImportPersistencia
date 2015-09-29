package com.okiimport.app.dao.maestros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.ClasificacionRepuesto;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface ClasificacionRepuestoRepository extends IGenericJPARepository<ClasificacionRepuesto, Integer> {
	List<ClasificacionRepuesto> findByProveedoresId(Integer id);
	Page<ClasificacionRepuesto> findByProveedoresId(Integer id, Pageable pageable);
}
