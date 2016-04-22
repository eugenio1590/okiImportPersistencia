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

import com.okiimport.app.model.OrdenCompra;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class OrdenCompraDAO extends AbstractJpaDao<OrdenCompra> {

	public OrdenCompraDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public Specification<OrdenCompra> consultarOrdenesCompra(final OrdenCompra ordenCompra, final Requerimiento requerimiento){
		return new Specification<OrdenCompra>(){

			@Override
			public Predicate toPredicate(Root<OrdenCompra> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("detalleOfertas", JoinType.LEFT);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos los campos a seleccionar
				criteriaQuery.multiselect(new Selection[]{
						entity.get("idOrdenCompra"),
						entity.get("fechaCreacion"),
						entity.get("estatus")
				});
				criteriaQuery.distinct(true);
				
				// 4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				
				restricciones.add(criteriaBuilder.equal(
						joins.get("detalleOfertas").join("detalleCotizacion").join("cotizacion").join("proveedor").get("id"),
						ordenCompra.getProveedor().getId()
				));
				
				if(requerimiento!=null)
					restricciones.add(criteriaBuilder.equal(
							joins.get("detalleOfertas").join("compra").join("requerimiento").get("idRequerimiento"), 
							requerimiento.getIdRequerimiento()
					));
				
				//Falta las restricciones de las entidades
				
				// 5. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	}

}
