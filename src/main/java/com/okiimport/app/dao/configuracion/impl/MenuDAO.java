package com.okiimport.app.dao.configuracion.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.Menu;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class MenuDAO extends AbstractJpaDao<Menu> {
	
	public Specification<Menu> consultarHijosTipoMenu(final int tipo){
		return new Specification<Menu>() {
			public Predicate toPredicate(Root<Menu> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				restricciones.add(
						criteriaBuilder.isEmpty(entity.get("hijos").as(List.class))
				);
				
				restricciones.add(
						criteriaBuilder.equal(entity.get("tipo"), tipo)
				);
				
				// 4. Ejecutamos				
				return crearPredicate(restricciones);
			}
		};
	}
}
