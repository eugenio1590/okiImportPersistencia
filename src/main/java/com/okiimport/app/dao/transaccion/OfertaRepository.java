package com.okiimport.app.dao.transaccion;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.enumerados.EEstatusOferta;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface OfertaRepository extends IGenericJPARepository<Oferta, Integer> {
	List<Oferta> findDistinctIdOfertaByDetalleOfertas_DetalleCotizacion_DetalleRequerimiento_RequerimientoAndEstatusIn(Requerimiento requerimiento, EEstatusOferta... estatus);
}
