package com.okiimport.app.dao.maestros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Ciudad;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface CiudadRepository extends IGenericJPARepository<Ciudad, Integer> {
	List<Ciudad> findByEstado_IdEstado(Integer idEstado);
	Page<Ciudad> findByEstado_IdEstado(Integer idEstado, Pageable pageable);
}
