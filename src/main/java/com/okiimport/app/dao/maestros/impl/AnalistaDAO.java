package com.okiimport.app.dao.maestros.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Persona;

public class AnalistaDAO extends PersonaDAO<Analista> {
	
	public Specification<Analista> consultarAnalistasSinUsuarios(Persona persona){
		Analista analista = (persona == null) ? new Analista() : new Analista(persona);
		analista.setAdministrador(false);
		return consultarPersonaSinUsuarios(analista);
	}
	
	public Specification<Analista> consultarAdministradoresSinUsuarios(Persona persona){
		Analista analista = (persona == null) ? new Analista() : new Analista(persona);
		analista.setAdministrador(true);
		return consultarPersonaSinUsuarios(analista);
	}

	public Specification<Analista> consultarCantRequerimientos(final List<String> estatus) {
		return new Specification<Analista>(){
			public Predicate toPredicate(Root<Analista> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades=new HashMap<String, JoinType>();
				entidades.put("requerimientos", JoinType.LEFT);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos los campos a seleccionar
				Expression<Long> cantRequerimientos = criteriaBuilder.countDistinct(joins.get("requerimientos"));
				
				criteriaQuery.multiselect(new Selection[]{
						cantRequerimientos.alias("cantRequerimientos"),
						entity.get("id")
				});
				
				// 4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				restricciones.add(
						criteriaBuilder.notEqual(entity.get("administrador"), true)
				);
				if(estatus!=null && !estatus.isEmpty())
					restricciones.add(
							criteriaBuilder.or(
									joins.get("requerimientos").get("estatus").in(estatus),
									criteriaBuilder.isEmpty(entity.<List<?>>get("requerimientos"))
									)
							);
				
				// 5. Creamos los campos de ordenamiento y ejecutamos
				List<Order> orders = new ArrayList<Order>();
				orders.add(criteriaBuilder.asc(cantRequerimientos));
				criteriaQuery.orderBy(orders);

				 List<Expression<?>> groupBy = new ArrayList<Expression<?>>();
				 groupBy.add(entity.get("id"));
				 
				 return crearPredicate(restricciones);
			}
			
		};
	}
	
	/**METODOS OVERRIDE*/
	@Override
	protected void agregarRestriccionesPersona(Analista persona, List<Predicate> restricciones) {
		if(persona.getAdministrador()!=null)
			restricciones.add(this.criteriaBuilder.equal(this.entity.get("administrador"), persona.getAdministrador()));
	}

	/**METODOS PROPIOS DE LA CLASE*/
}
