package com.okiimport.app.dao.transaccion.impl;

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
import javax.persistence.criteria.Selection;

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.Oferta;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class OfertaDAO extends AbstractJpaDao<Oferta> {
	
	public Specification<Oferta> consultarOfertasPorRequerimiento(final Integer idRequerimiento, final List<String> estatus, 
			final String fieldSort, final Boolean sortDirection){
		return new Specification<Oferta>(){
			public Predicate toPredicate(Root<Oferta> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("detalleOfertas", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				//3. Creamos los campos a seleccionar
				criteriaQuery = criteriaBuilder.createTupleQuery();
				criteriaQuery.multiselect(new Selection[]{
						entity.get("idOferta"),
						entity.get("fechaCreacion"),
						entity.get("estatus")
				}).distinct(true);
				
				//4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				restricciones.add(criteriaBuilder.equal(
						joins.get("detalleOfertas").join("detalleCotizacion")
							.join("detalleRequerimiento").join("requerimiento").get("idRequerimiento"), 
						idRequerimiento));
				
				if(estatus!=null && !estatus.isEmpty())
					restricciones.add(entity.get("estatus").in(estatus));
				
				//5. Creamos los campos de ordenamiento y ejecutamos
				Map<String, Boolean> orders = new HashMap<String, Boolean>();
				if(fieldSort!=null && sortDirection!=null)
					orders.put(fieldSort, sortDirection);
				else
					orders.put("idOferta", true);
				
				return crearPredicate(restricciones, orders);
			}
			
		};
	}
}
