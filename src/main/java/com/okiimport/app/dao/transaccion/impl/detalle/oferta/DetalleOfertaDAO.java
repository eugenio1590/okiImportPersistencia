package com.okiimport.app.dao.transaccion.impl.detalle.oferta;

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

import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class DetalleOfertaDAO extends AbstractJpaDao<DetalleOferta>{

	public Specification<DetalleOferta> consultarDetallesOferta(final Integer idOferta, final List<DetalleRequerimiento> detallesRequerimiento){
		return new Specification<DetalleOferta>(){

			@Override
			public Predicate toPredicate(Root<DetalleOferta> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("oferta", JoinType.INNER);
				entidades.put("detalleCotizacion", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				criteriaQuery.distinct(true);
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				if(idOferta!=null)
					restricciones.add(criteriaBuilder.equal(
							joins.get("oferta").get("idOferta"), 
							idOferta));
				
				if(detallesRequerimiento!=null && !detallesRequerimiento.isEmpty())
					restricciones.add(criteriaBuilder.not(
							joins.get("detalleCotizacion").join("detalleRequerimiento").in(detallesRequerimiento) 
					));
				
				// 3. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	}

}
