package com.okiimport.app.dao.maestros.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.Motor;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class MotorDAO extends AbstractJpaDao<Motor> {
	
	public Specification<Motor> consultarMotores(final Motor motorF){
		return new Specification<Motor>(){
			public Predicate toPredicate(Root<Motor> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				agregarRestricciones(motorF, restricciones, null);
				
				// 4. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	}

	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarRestricciones(final Motor motorF, final List<Predicate> restricciones, final Map<String, Join<?,?>> joins){
		if(motorF!=null){
			if(motorF.getIdMotor()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("idMotor").as(String.class)),
						"%"+String.valueOf(motorF.getIdMotor()).toLowerCase()+"%"));
			
			if(motorF.getNombre()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("nombre").as(String.class)),
						"%"+String.valueOf(motorF.getNombre()).toLowerCase()+"%"));
		}
	}
}
