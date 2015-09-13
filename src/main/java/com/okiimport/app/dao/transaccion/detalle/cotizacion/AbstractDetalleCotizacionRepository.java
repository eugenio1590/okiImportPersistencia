package com.okiimport.app.dao.transaccion.detalle.cotizacion;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@NoRepositoryBean
public interface AbstractDetalleCotizacionRepository<T extends DetalleCotizacion> extends IGenericJPARepository<T, Integer> {
	List<T> findByCotizacion_IdCotizacion(Integer idCotizacion);
	Page<T> findByCotizacion_IdCotizacion(Integer idCotizacion, Pageable pageable);
}
