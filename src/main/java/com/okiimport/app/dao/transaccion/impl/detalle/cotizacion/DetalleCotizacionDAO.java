package com.okiimport.app.dao.transaccion.impl.detalle.cotizacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.enumerados.EEstatusCotizacion;

public class DetalleCotizacionDAO extends AbstractDetalleCotizacionDAO<DetalleCotizacion> {
	
	public Specification<DetalleCotizacion> consultarDetallesCotizacionEmitidos(final Integer idRequerimiento){
		
		return new Specification<DetalleCotizacion>(){
			public Predicate toPredicate(Root<DetalleCotizacion> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("cotizacion", JoinType.INNER);
				entidades.put("detalleRequerimiento", JoinType.INNER);
				entidades.put("detalleOfertas", JoinType.LEFT);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos los campos a seleccionar
				addMultiselect(joins, criteriaQuery, true);
				
				// 4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				agregarRestricciones(restricciones, joins, null, idRequerimiento, EEstatusCotizacion.EMITIDA);
				
				restricciones.add(criteriaBuilder.isEmpty(entity.<List<?>>get("detalleOfertas")));
				
				// 5. Ejecutamos
				return crearPredicate(restricciones);
			};
		};
	}
	
}
