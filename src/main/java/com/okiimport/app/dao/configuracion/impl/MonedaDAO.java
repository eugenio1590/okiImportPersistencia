package com.okiimport.app.dao.configuracion.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.Moneda;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class MonedaDAO extends AbstractJpaDao<Moneda> {
	
	public Specification<Moneda> consultarMonedasConHistorico(final String estatus){
		return new Specification<Moneda>(){

			public Predicate toPredicate(Root<Moneda> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Generamos los Joins
				
				//3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				restricciones.add(criteriaBuilder.isNotEmpty(entity.get("historicoMonedas").as(List.class)));
				
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(entity.get("estatus").as(String.class)), 
						"%"+estatus+"%"));
				
				//4. Creamos los campos de ordenamiento y ejecutamos
				Map<String, Boolean> orders = new HashMap<String, Boolean>();
				orders.put("idMoneda", true);
				
				return crearPredicate(restricciones, orders);
			}
			
		};
	}
	
}
