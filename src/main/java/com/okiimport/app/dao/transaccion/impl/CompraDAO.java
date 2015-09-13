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

import com.okiimport.app.model.Compra;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class CompraDAO extends AbstractJpaDao<Compra> {

	public Specification<Compra> consultarComprasPorRequerimiento(final Compra compraF, final int idRequerimiento, 
			final String fieldSort, final Boolean sortDirection){
		return new Specification<Compra>(){
			public Predicate toPredicate(Root<Compra> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
				
				// 4. Creamos los campos de ordenamiento y ejecutamos
				Map<String, Boolean> orders = new HashMap<String, Boolean>();
				if(fieldSort!=null && sortDirection!=null)
					orders.put(fieldSort, sortDirection);
				else
					orders.put("idCompra", true);
				
				return crearPredicate(restricciones, orders);
			}
		};
	}
	
	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarFiltros(Compra compraF, List<Predicate> restricciones) {
		// TODO Auto-generated method stub
		
	}
}
