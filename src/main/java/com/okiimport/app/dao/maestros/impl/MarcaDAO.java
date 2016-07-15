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

import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class MarcaDAO extends AbstractJpaDao<MarcaVehiculo>{
	public Specification<MarcaVehiculo> consultarMarcaVehiculos(final MarcaVehiculo MarcaVehiculoF){
		return new Specification<MarcaVehiculo>(){
			public Predicate toPredicate(Root<MarcaVehiculo> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				agregarRestricciones(MarcaVehiculoF, restricciones, null);
				
				// 4. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	}

	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarRestricciones(final MarcaVehiculo MarcaVehiculoF, final List<Predicate> restricciones, final Map<String, Join<?,?>> joins){
		if(MarcaVehiculoF!=null){
			if(MarcaVehiculoF.getIdMarcaVehiculo()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("idMarcaVehiculo").as(String.class)),
						"%"+String.valueOf(MarcaVehiculoF.getIdMarcaVehiculo()).toLowerCase()+"%"));
			
			if(MarcaVehiculoF.getNombre()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("nombre").as(String.class)),
						"%"+String.valueOf(MarcaVehiculoF.getNombre()).toLowerCase()+"%"));
		}
	}

}
