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

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Motor;
import com.okiimport.app.model.Vehiculo;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.MarcaVehiculo;
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
	
	
	public Specification<Vehiculo> consultarVehiculosPorCliente(final Vehiculo vehiculoF){
		return new Specification<Vehiculo>(){
			public Predicate toPredicate(Root<Vehiculo> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("cliente", JoinType.INNER);
				entidades.put("marcaVehiculo", JoinType.LEFT);
				entidades.put("motor", JoinType.LEFT);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos las Restricciones de la busqueda
				Integer idCliente = vehiculoF.getCliente().getId();
				List<Predicate> restricciones = new ArrayList<Predicate>();
				restricciones.add(criteriaBuilder.equal(entity.get("cliente"),idCliente));
				agregarRestricciones(vehiculoF, restricciones, joins);
				
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
			
			//Motor
			Motor motor = vehiculoF.getMotor();
			if(motor!=null && joins.get("motor") != null){
				if(motor.getNombre() != null)
					restricciones.add(this.criteriaBuilder.like(
							this.criteriaBuilder.lower(joins.get("motor").get("nombre").as(String.class)),
							"%"+ String.valueOf(motor.getNombre()).toLowerCase() + "%"));
			}
			
			//Marca Vehiculo
			MarcaVehiculo marcaVehiculo = vehiculoF.getMarcaVehiculo();
			if(marcaVehiculo!=null && joins.get("marcaVehiculo") != null){
				if(marcaVehiculo.getNombre()!=null)
					restricciones.add(this.criteriaBuilder.like(
							this.criteriaBuilder.lower(joins.get("marcaVehiculo").get("nombre").as(String.class)), 
							"%"+String.valueOf(marcaVehiculo.getNombre()).toLowerCase()+"%"));
			}
			
			if(vehiculoF.getModelo()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("modelo").as(String.class)),
						"%"+String.valueOf(vehiculoF.getModelo()).toLowerCase()+"%"));
			

			
			if(vehiculoF.getAnno()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("anno").as(String.class)),
						"%"+String.valueOf(vehiculoF.getAnno()).toLowerCase()+"%"));
		}
	}
}
