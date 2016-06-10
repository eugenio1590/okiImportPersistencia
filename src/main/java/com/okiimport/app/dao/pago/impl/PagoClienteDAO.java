package com.okiimport.app.dao.pago.impl;

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
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class PagoClienteDAO extends AbstractJpaDao<PagoCliente> {
	
	public Specification<PagoCliente> consultarPagoCliente(final PagoCliente pagoFiltro) 
			 {
		return new Specification<PagoCliente>(){
			public Predicate toPredicate(Root<PagoCliente> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("compra", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				agregarRestriccionesFiltros(restricciones, pagoFiltro, joins);
				
				// 4. Ejecutamos				
				return crearPredicate(restricciones);
			}
		};
	}
	
	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarRestriccionesFiltros(List<Predicate> restricciones,
			PagoCliente pagoFiltro, Map<String, Join<?,?>> joins) {

		if (pagoFiltro != null) {
			if (pagoFiltro.getId() != null) {
				restricciones.add(this.criteriaBuilder.like(
						this.entity.get("id").as(String.class),
						"%" + String.valueOf(pagoFiltro.getId()) + "%"));
			}

			
			
			if(pagoFiltro.getFormaPago() != null){
				restricciones.add(this.criteriaBuilder.like(
						this.entity.get("formaPago").as(String.class), 
						"%"));
			}
			
			if(pagoFiltro.getFechaCreacion() != null){
				restricciones.add(this.criteriaBuilder.like(
						this.entity.get("fechaCreacion").as(String.class),
						"%" + dateFormat.format(pagoFiltro.getFechaCreacion()) + "%"));
			}
			
			if (pagoFiltro.getMonto() != null)
			{
				restricciones.add(this.criteriaBuilder.like(
						this.entity.get("montoPago").as(String.class), 
						"%" ));
			}
			
			if(joins!=null){
				//Compra
				Compra compra = pagoFiltro.getCompra();
				if (compra != null && joins.get("compra") != null) {
					if (pagoFiltro.getCompra().getIdCompra() != null) {
						restricciones.add(this.criteriaBuilder.like(
								joins.get("compra").get("idCompra").as(String.class),
								"%"+String.valueOf(pagoFiltro.getCompra().getIdCompra()) +"%" ));
					}

					if (pagoFiltro.getCompra().getRequerimiento().getCliente().getNombre() != null) {
						restricciones.add(this.criteriaBuilder.like(
						joins.get("compra").join("cliente").get("nombre").as(String.class),
						"%"+pagoFiltro.getCompra().getRequerimiento().getCliente().getNombre() + "%" ));

				}
					}
				
			}
		}
	}

}
