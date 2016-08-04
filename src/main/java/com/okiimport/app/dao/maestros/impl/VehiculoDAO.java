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

import com.okiimport.app.model.Vehiculo;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class VehiculoDAO extends AbstractJpaDao<Vehiculo>{
	
	public Specification<Vehiculo> consultarVehiculos(final Vehiculo vehiculoF){
		return new Specification<Vehiculo>(){
			public Predicate toPredicate(Root<Vehiculo> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				agregarRestricciones(vehiculoF, restricciones, null);
				
				// 4. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	}

	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarRestricciones(final Vehiculo vehiculoF, final List<Predicate> restricciones, final Map<String, Join<?,?>> joins){
		if(vehiculoF!=null){
			if(vehiculoF.getId()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("id").as(String.class)),
						"%"+String.valueOf(vehiculoF.getId()).toLowerCase()+"%"));
			
			if(vehiculoF.getMarcaVehiculo()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("marcaVehiculo").as(String.class)),
						"%"+String.valueOf(vehiculoF.getMarcaVehiculo().getNombre()).toLowerCase()+"%"));
			
			if(vehiculoF.getModelo()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("modelo").as(String.class)),
						"%"+String.valueOf(vehiculoF.getModelo()).toLowerCase()+"%"));
			
			if(vehiculoF.getAnno()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("anno").as(String.class)),
						"%"+String.valueOf(vehiculoF.getAnno()).toLowerCase()+"%"));
			if(vehiculoF.getMotor()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("motor").as(String.class)),
						"%"+String.valueOf(vehiculoF.getMotor()).toLowerCase()+"%"));
		}
	}
}
