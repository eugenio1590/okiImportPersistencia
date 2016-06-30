package com.okiimport.app.dao.maestros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Vehiculo;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface VehiculoRepository extends IGenericJPARepository<Vehiculo, Integer> {
	List<Vehiculo> findByCliente(Cliente cliente);
	Page<Vehiculo> findByCliente(Cliente cliente, Pageable pageable);
}
