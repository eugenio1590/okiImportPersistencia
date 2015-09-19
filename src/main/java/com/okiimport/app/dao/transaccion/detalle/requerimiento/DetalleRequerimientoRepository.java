package com.okiimport.app.dao.transaccion.detalle.requerimiento;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface DetalleRequerimientoRepository extends IGenericJPARepository<DetalleRequerimiento, Integer> {
	List<DetalleRequerimiento> findByRequerimiento_IdRequerimiento(Integer idRequerimiento);
	Page<DetalleRequerimiento> findByRequerimiento_IdRequerimiento(Integer idRequerimiento, Pageable pageable);
}
