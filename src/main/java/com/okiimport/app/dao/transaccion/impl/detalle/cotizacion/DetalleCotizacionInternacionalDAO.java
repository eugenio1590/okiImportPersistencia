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

import com.okiimport.app.model.DetalleCotizacionInternacional;
import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Estado;
import com.okiimport.app.model.Proveedor;

public class DetalleCotizacionInternacionalDAO extends AbstractDetalleCotizacionDAO<DetalleCotizacionInternacional> {

	public Specification<DetalleCotizacionInternacional> consultarDetallesCotizacion(
			final DetalleCotizacionInternacional detalleF, final Integer idCotizacion, final Integer idRequerimiento,
			final boolean distinct, final boolean cantExacta){
		return new Specification<DetalleCotizacionInternacional>(){
			public Predicate toPredicate(Root<DetalleCotizacionInternacional> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("cotizacion", JoinType.INNER);
				entidades.put("detalleRequerimiento", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				//3. Creamos los campos a seleccionar
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
				
				//4. Creamos las Restricciones de la busqueda
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
				
				// 4. Ejecutamos				
				return crearPredicate(restricciones);
			}
			
		};
	}
	
	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarRestricciones(DetalleCotizacionInternacional detalleF, List<Predicate> restricciones, 
			Map<String, Join<?,?>> joins, boolean cantExacta){
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
					Join<?,?> joinP = joins.get("cotizacion").join("proveedor");
					if(proveedor.getNombre()!=null)
						restricciones.add(criteriaBuilder.like(
								criteriaBuilder.lower(joinP.get("nombre").as(String.class)),
								"%"+String.valueOf(proveedor.getNombre()).toLowerCase()+"%"));
					
					/**Ubicacion*/
					//Ciudad
					Ciudad ciudadP = proveedor.getCiudad();
					if(ciudadP!=null){
						Join<?,?> joinC = joinP.join("ciudad");
						
						//Estado
						Estado estadoP = ciudadP.getEstado();
						if(estadoP!=null){
							Join<?,?> joinE = joinC.join("estado");
							if(estadoP.getNombre()!=null && ciudadP.getNombre()!=null)
								restricciones.add(criteriaBuilder.or(
										criteriaBuilder.like(
												criteriaBuilder.lower(joinC.get("nombre").as(String.class)),
													"%"+String.valueOf(ciudadP.getNombre()).toLowerCase()+"%"),
												criteriaBuilder.like(
														criteriaBuilder.lower(joinE.get("nombre").as(String.class)),
														"%"+String.valueOf(estadoP.getNombre()).toLowerCase()+"%")
										));
							else if(estadoP.getNombre()!=null)
								restricciones.add(criteriaBuilder.like(
										criteriaBuilder.lower(joinE.get("nombre").as(String.class)),
										"%"+String.valueOf(estadoP.getNombre()).toLowerCase()+"%"));
						}
						else {
							if(ciudadP.getNombre()!=null)
								restricciones.add(criteriaBuilder.like(
										criteriaBuilder.lower(joinC.get("nombre").as(String.class)),
										"%"+String.valueOf(ciudadP.getNombre()).toLowerCase()+"%"));
						}
					}
				}
			}
			
			//Detalle Requerimiento
			DetalleRequerimiento detalleR = detalleF.getDetalleRequerimiento();
			if(detalleR!=null){
				if(detalleR.getDescripcion()!=null)
					restricciones.add(criteriaBuilder.like(
							criteriaBuilder.lower(joins.get("detalleRequerimiento").get("descripcion").as(String.class)),
							"%"+String.valueOf(detalleR.getDescripcion()).toLowerCase()+"%"));
			}
		}
	}
}
