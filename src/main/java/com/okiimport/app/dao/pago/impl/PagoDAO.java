package com.okiimport.app.dao.pago.impl;

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

import com.okiimport.app.model.Pago;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public abstract class PagoDAO<T extends Pago> extends AbstractJpaDao<T> {

	public Specification<T> consultarPago(final T pagoF){
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				
				//agregarFiltros(pagoF, restricciones);
				
				// 4. Ejecutamos
				return crearPredicate(restricciones);
			}

		};
	}
	
	
	
}
