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

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.Venta;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class CompraDAO extends AbstractJpaDao<Venta> {

	public Specification<Venta> consultarComprasPorRequerimiento(final Venta compraF, final int idRequerimiento){
		return new Specification<Venta>(){
			public Predicate toPredicate(Root<Venta> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("requerimiento", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				restricciones.add(
						criteriaBuilder.equal(
								joins.get("requerimiento").get("idRequerimiento"), 
								idRequerimiento)
				);
				
				agregarFiltros(compraF, restricciones);
				
				// 4. Ejecutamos				
				return crearPredicate(restricciones);
			}
		};
	}
	
	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarFiltros(Venta compraF, List<Predicate> restricciones) {
		// TODO Auto-generated method stub
		
	}
}
