package com.okiimport.app.dao.transaccion;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.OrdenCompra;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.enumerados.EEstatusOrdenCompra;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface OrdenCompraRepository extends IGenericJPARepository<OrdenCompra, Integer> {
	public List<OrdenCompra> findByDetalleOfertas_Compra_RequerimientoAndEstatusIn(Requerimiento requerimiento, EEstatusOrdenCompra[] estatus);
	public Page<OrdenCompra> findByDetalleOfertas_Compra_RequerimientoAndEstatusIn(Requerimiento requerimiento, EEstatusOrdenCompra[] estatus, Pageable pageable);
}
