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

import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class CotizacionDAO extends AbstractJpaDao<Cotizacion> {

	public Specification<Cotizacion> consultarCotizacionesAsignadas(final Cotizacion cotizacion, final int idRequerimiento, 
			final List<String> estatus){
		return new Specification<Cotizacion>(){
			public Predicate toPredicate(Root<Cotizacion> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("detalleCotizacions", JoinType.INNER);
				entidades.put("proveedor", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos los campos a seleccionar
				criteriaQuery.multiselect(new Selection[]{
						entity.get("idCotizacion"),
						entity.get("fechaCreacion"),
						entity.get("fechaVencimiento"),
						entity.get("estatus"),
						entity.get("mensaje"),
						entity.get("proveedor"),
				});
				criteriaQuery.distinct(true);
				
				// 4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				
				agregarFiltro(cotizacion,restricciones,joins);
				
				restricciones.add(entity.get("estatus").in(estatus));
				restricciones.add(criteriaBuilder.equal(
							joins.get("detalleCotizacions").join("detalleRequerimiento")
								.join("requerimiento").get("idRequerimiento"), 
							idRequerimiento));
				
				// 5. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	}
	
	public Specification<Cotizacion> consultarSolicitudCotizaciones(final Cotizacion cotizacionF, final Integer idRequerimiento, 
			final int idProveedor, final List<String> estatus){
		return new Specification<Cotizacion>(){
			public Predicate toPredicate(Root<Cotizacion> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("detalleCotizacions", JoinType.INNER);
				entidades.put("proveedor", JoinType.INNER);
				entidades.put("historicoMoneda", JoinType.LEFT);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos los campos a seleccionar
				criteriaQuery.multiselect(new Selection[]{
						entity.get("idCotizacion"),
						entity.get("fechaCreacion"),
						entity.get("fechaVencimiento"),
						entity.get("estatus"),
						entity.get("mensaje"),
						entity.get("proveedor"),
						joins.get("historicoMoneda")
				});
				criteriaQuery.distinct(true);
				
				// 4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				agregarFiltro(cotizacionF, restricciones, joins);
				
				restricciones.add(criteriaBuilder.equal(joins.get("proveedor").get("id"), idProveedor));
				restricciones.add(entity.get("estatus").in(estatus));
				restricciones.add(criteriaBuilder.equal(
						joins.get("detalleCotizacions").join("detalleRequerimiento").join("requerimiento").get("idRequerimiento"), 
						idRequerimiento));
				
				// 5. Ejecutamos				
				return crearPredicate(restricciones);
			}
		};
	}
	
	public Specification<Cotizacion> consultarCotizacionesParaEditar(final Cotizacion cotizacionF, final Integer idRequerimiento, 
			final List<String> estatus){
		return new Specification<Cotizacion>(){
			public Predicate toPredicate(Root<Cotizacion> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("detalleCotizacions", JoinType.INNER);
				entidades.put("proveedor", JoinType.INNER);
				entidades.put("historicoMoneda", JoinType.LEFT);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos los campos a seleccionar
				criteriaQuery.multiselect(new Selection[]{
						entity.get("idCotizacion"),
						entity.get("fechaCreacion"),
						entity.get("fechaVencimiento"),
						entity.get("estatus"),
						entity.get("mensaje"),
						entity.get("proveedor"),
						joins.get("historicoMoneda")
				});
				criteriaQuery.distinct(true);
				
				// 4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				agregarFiltro(cotizacionF, restricciones, joins);
				
				restricciones.add(entity.get("estatus").in(estatus));
				restricciones.add(criteriaBuilder.equal(
						joins.get("detalleCotizacions").join("detalleRequerimiento")
							.join("requerimiento").get("idRequerimiento"), 
						idRequerimiento));
				
				// 5. Ejecutamos				
				return crearPredicate(restricciones);
			}
			
		};
	}
	
	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarFiltro(Cotizacion cotizacionF,List<Predicate> restricciones,Map<String, Join<?,?>> joins){
		if (cotizacionF != null){
			if (cotizacionF.getIdCotizacion() != null){
				restricciones.add(criteriaBuilder.like(criteriaBuilder.lower(this.entity.get("idCotizacion").as(String.class)), "%"+String.valueOf(cotizacionF.getIdCotizacion()).toLowerCase()+"%"));
			}
			if (cotizacionF.getFechaCreacion() != null){
				restricciones.add(criteriaBuilder.like(criteriaBuilder.lower(this.entity.get("fechaCreacion").as(String.class)), "%"+dateFormat.format(cotizacionF.getFechaCreacion()).toLowerCase()+"%"));
			}
			if (cotizacionF.getFechaVencimiento() != null){
				restricciones.add(criteriaBuilder.like(criteriaBuilder.lower(this.entity.get("fechaVencimiento").as(String.class)), "%"+dateFormat.format(cotizacionF.getFechaVencimiento()).toLowerCase()+"%"));
			}
			if(cotizacionF.getTipo() != null){
				restricciones.add(criteriaBuilder.equal(this.entity.get("tipo"), cotizacionF.getTipo()));
			}
			if(joins.get("proveedor") != null && cotizacionF.getProveedor() != null){
				if(cotizacionF.getProveedor().getNombre() != null){
					restricciones.add(criteriaBuilder.like(criteriaBuilder.lower(joins.get("proveedor").get("nombre").as(String.class)), "%"+String.valueOf(cotizacionF.getProveedor().getNombre()).toLowerCase()+"%"));
				}
			}
		}
	}
	
}
