package com.okiimport.app.dao.transaccion;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.enumerados.EEstatusCotizacion;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface CotizacionRepository extends IGenericJPARepository<Cotizacion, Integer> {
	List<Cotizacion> findByProveedorAndEstatusIn(Proveedor proveedor,List<EEstatusCotizacion> estatus);
	List<Cotizacion> findTop1ByProveedorAndEstatusIn(Proveedor proveedor,List<EEstatusCotizacion> estatus);
}
