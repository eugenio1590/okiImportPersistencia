package com.okiimport.app.dao.maestros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface MarcaVehiculoRepository extends IGenericJPARepository<MarcaVehiculo, Integer> {
	List<MarcaVehiculo> findByEstatus(EEstatusGeneral estatus);
	Page<MarcaVehiculo> findByEstatus(EEstatusGeneral estatus, Pageable pageable);
	//Page<MarcaVehiculo> findByEstatus(EEstatusGeneral estatus, Specification<MarcaVehiculo> spec, Pageable pageable);
	List<MarcaVehiculo> findByProveedoresId(Integer id);
	Page<MarcaVehiculo> findByProveedoresId(Integer id, Pageable pageable);
}
