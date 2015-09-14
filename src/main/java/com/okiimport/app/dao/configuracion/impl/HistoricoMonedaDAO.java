package com.okiimport.app.dao.configuracion.impl;

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

import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class HistoricoMonedaDAO extends AbstractJpaDao<HistoricoMoneda> {
	
	public Specification<HistoricoMoneda> consultarActualConversion(final Integer idMoneda){
		return new Specification<HistoricoMoneda>() {
			public Predicate toPredicate(Root<HistoricoMoneda> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("moneda", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				
				restricciones.add(criteriaBuilder.equal(joins.get("moneda").get("idMoneda"), idMoneda));
				
				// 4. Ejecutamos				
				return crearPredicate(restricciones);
			}
		};
	}
	
}
