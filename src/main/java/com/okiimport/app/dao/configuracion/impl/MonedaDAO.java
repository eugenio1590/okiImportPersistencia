package com.okiimport.app.dao.configuracion.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.Moneda;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class MonedaDAO extends AbstractJpaDao<Moneda> {
	
	public Specification<Moneda> consultarMonedasConHistorico(final EEstatusGeneral estatus){
		return new Specification<Moneda>(){

			public Predicate toPredicate(Root<Moneda> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				restricciones.add(criteriaBuilder.isNotEmpty(entity.get("historicoMonedas").as(List.class)));
				
				restricciones.add(criteriaBuilder.equal(entity.get("estatus"), estatus));
				
				// 4. Ejecutamos				
				return crearPredicate(restricciones);
			}
			
		};
	}
	
	
	
}
