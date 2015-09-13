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

import com.okiimport.app.model.Menu;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class MenuDAO extends AbstractJpaDao<Menu> {

	public Specification<Menu> consultarPadresMenuUsuario(final Integer tipo) {
		return new Specification<Menu>() {
			public Predicate toPredicate(Root<Menu> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Generamos los Joins
				
				//3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				restricciones.add(
						criteriaBuilder.isNull(entity.get("padre"))
				);
				
				restricciones.add(
						criteriaBuilder.equal(entity.get("tipo"), tipo)
				);
				
				//4. Creamos los campos de ordenamiento y ejecutamos
				Map<String, Boolean> orders = new HashMap<String, Boolean>();
				orders.put("idMenu", true);
				
				return crearPredicate(restricciones, orders);
			}
		};
		
	}
}
