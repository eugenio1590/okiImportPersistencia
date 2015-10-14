package com.okiimport.app.dao.transaccion.impl.detalle.cotizacion;

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
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public abstract class AbstractDetalleCotizacionDAO<T extends DetalleCotizacion> extends AbstractJpaDao<T> {

	public Specification<T> consultarDetallesCotizacion(final DetalleCotizacion detalleF, 
			final Integer idCotizacion, final Integer idRequerimiento, final boolean distinct,  final boolean cantExacta){
		return new Specification<T>(){
			public Predicate toPredicate(Root<T> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("cotizacion", JoinType.INNER);
				entidades.put("detalleRequerimiento", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos los campos a seleccionar
				if(distinct){
					criteriaQuery.multiselect(new Selection[]{
							entity.get("idDetalleCotizacion"),
							entity.get("marcaRepuesto"),
							entity.get("precioVenta"),
							entity.get("precioFlete"),
							entity.get("cantidad"),
							joins.get("cotizacion"),
							joins.get("detalleRequerimiento"),
					});
					criteriaQuery.distinct(distinct);
				}
				
				// 4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				
				agregarRestricciones(detalleF, restricciones, joins, cantExacta);

				if(idCotizacion!=null)
					restricciones.add(criteriaBuilder.equal(
							joins.get("cotizacion").get("idCotizacion"), 
							idCotizacion));
				
				if(idRequerimiento!=null)
					restricciones.add(criteriaBuilder.equal(
							joins.get("detalleRequerimiento").join("requerimiento").get("idRequerimiento"), 
							idRequerimiento));
				
				// 5. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	}
	
	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarRestricciones(DetalleCotizacion detalleF, List<Predicate> restricciones, Map<String, Join<?,?>> joins, boolean cantExacta){
		if(detalleF!=null){
			if(detalleF.getMarcaRepuesto()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("marcaRepuesto").as(String.class)),
						"%"+String.valueOf(detalleF.getMarcaRepuesto()).toLowerCase()+"%"));
			
			if(detalleF.getCantidad()!=null){
				if(cantExacta)
					restricciones.add(criteriaBuilder.like(
							criteriaBuilder.lower(this.entity.get("cantidad").as(String.class)),
							"%"+String.valueOf(detalleF.getCantidad()).toLowerCase()+"%"));
				else
					restricciones.add(criteriaBuilder.greaterThanOrEqualTo(this.entity.get("cantidad").as(Long.class), detalleF.getCantidad()));
			}
			
			if(detalleF.getPrecioVenta()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("precioVenta").as(String.class)),
						"%"+String.valueOf(detalleF.getPrecioVenta()).replaceAll(".?0*$", "").toLowerCase()+"%"));
			
			if(detalleF.getPrecioFlete()!=null)
				restricciones.add(criteriaBuilder.like(
						criteriaBuilder.lower(this.entity.get("precioFlete").as(String.class)),
						"%"+String.valueOf(detalleF.getPrecioFlete()).replaceAll(".?0*$", "").toLowerCase()+"%"));
			
			//Cotizacion
			Cotizacion cotizacion = detalleF.getCotizacion();
			if(cotizacion!=null){
				if(cotizacion.getIdCotizacion()!=null)
					restricciones.add(criteriaBuilder.like(
							criteriaBuilder.lower(joins.get("cotizacion").get("idCotizacion").as(String.class)),
							"%"+String.valueOf(cotizacion.getIdCotizacion()).toLowerCase()+"%"));
				
				//Proveedor
				Proveedor proveedor = cotizacion.getProveedor();
				if(proveedor!=null){
					Join<?,?> joinProveedor = joins.get("cotizacion").join("proveedor");
					
					if(proveedor.getNombre()!=null)
						restricciones.add(criteriaBuilder.like(
								criteriaBuilder.lower(joinProveedor.get("nombre").as(String.class)),
								"%"+String.valueOf(proveedor.getNombre()).toLowerCase()+"%"));
					
					String ubicacion = proveedor.getUbicacion();
					if(ubicacion!=null){
						Join<?,?> joinCiudad = joinProveedor.join("ciudad", JoinType.LEFT);
						restricciones.add(
								criteriaBuilder.or(
										criteriaBuilder.like(
												criteriaBuilder.lower(joinProveedor.join("pais").get("nombre").as(String.class)),
												"%"+String.valueOf(ubicacion).toLowerCase()+"%"),
										criteriaBuilder.like(
												criteriaBuilder.lower(joinCiudad.get("nombre").as(String.class)),
												"%"+String.valueOf(ubicacion).toLowerCase()+"%"),
										criteriaBuilder.like(
												criteriaBuilder.lower(joinCiudad.join("estado", JoinType.LEFT).get("nombre").as(String.class)),
												"%"+String.valueOf(ubicacion).toLowerCase()+"%")
								)
						);
					}
					else {
						//Pais, Ciudad, Estado Unitarios
					}
						
				}
			}
			
			//Detalle Requerimiento
			DetalleRequerimiento detalleRequerimiento = detalleF.getDetalleRequerimiento();
			if(detalleRequerimiento!=null){
				Join<?,?> joinDetalleRequerimiento = joins.get("detalleRequerimiento");
				if(detalleRequerimiento.getDescripcion()!=null)
					restricciones.add(criteriaBuilder.like(
							criteriaBuilder.lower(joinDetalleRequerimiento.get("descripcion").as(String.class)),
							"%"+String.valueOf(detalleRequerimiento.getDescripcion()).toLowerCase()+"%"));
				System.out.println("Cantidad Exacta: "+cantExacta);
				if(detalleRequerimiento.getCantidad()!=null)
					if(cantExacta)
						restricciones.add(criteriaBuilder.like(
								criteriaBuilder.lower(joinDetalleRequerimiento.get("cantidad").as(String.class)),
								"%"+String.valueOf(detalleRequerimiento.getCantidad()).toLowerCase()+"%"));
					else
						restricciones.add(criteriaBuilder.greaterThanOrEqualTo(
								joinDetalleRequerimiento.get("cantidad").as(Long.class), detalleRequerimiento.getCantidad()));
			}
		}
	}
}
