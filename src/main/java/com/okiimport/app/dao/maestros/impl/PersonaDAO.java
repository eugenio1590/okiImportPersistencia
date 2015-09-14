package com.okiimport.app.dao.maestros.impl;

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

import com.okiimport.app.model.Persona;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public abstract class PersonaDAO<T extends Persona> extends AbstractJpaDao<T> {
	
	public Specification<T> consultarPersona(final T personaF){
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				
				agregarFiltros(personaF, restricciones);
				
				// 4. Ejecutamos
				return crearPredicate(restricciones);
			}
		};
	}
	
	public Specification<T> consultarPersonaSinUsuarios(final T persona) {
		return new Specification<T>() {

			public Predicate toPredicate(Root<T> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades=new HashMap<String, JoinType>();
				entidades.put("usuario", JoinType.LEFT);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				restricciones.add(criteriaBuilder.isNull(joins.get("usuario")));
				agregarFiltros(persona, restricciones);

				// 4. Ejecutamos				
				return crearPredicate(restricciones);
			}
		};
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	protected void agregarFiltros(T personaF, List<Predicate> restricciones){
		if(personaF!=null){
			if(personaF.getCedula()!=null){
				System.out.println("Restriccion Cedula: "+personaF.getCedula());
				restricciones.add(this.criteriaBuilder.like(
						this.criteriaBuilder.lower(this.entity.get("cedula").as(String.class)), 
						"%"+personaF.getCedula().toLowerCase()+"%"
						));
			}

			if(personaF.getNombre()!=null){
				System.out.println("Restriccion Nombre: "+personaF.getNombre());
				restricciones.add(this.criteriaBuilder.like(
						this.criteriaBuilder.lower(this.entity.get("nombre").as(String.class)), 
						"%"+personaF.getNombre().toLowerCase()+"%"
						));
			}

			if(personaF.getApellido()!=null){
				System.out.println("Restriccion Apellido: "+personaF.getApellido());
				restricciones.add(this.criteriaBuilder.like(
						this.criteriaBuilder.lower(this.entity.get("apellido").as(String.class)), 
						"%"+personaF.getApellido().toLowerCase()+"%"
						));
			}
			
			if(personaF.getEstatus()!=null){
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("estatus").as(String.class)), 
						"%"+String.valueOf(personaF.getEstatus()).toLowerCase()+"%"));
			}
			agregarRestriccionesPersona(personaF, restricciones);
		}
	}
	
	protected abstract void agregarRestriccionesPersona(T persona, List<Predicate> restricciones);
}
