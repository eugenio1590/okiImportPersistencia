package com.okiimport.app.dao.maestros.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Cotizacion;

public class ProveedorDAO extends PersonaDAO<Proveedor> {
	
	public Specification<Proveedor> consultarProveedoresSinUsuarios(Persona persona,  String fieldSort, Boolean sortDirection){
		Proveedor proveedor = (persona==null) ? new Proveedor() : new Proveedor(persona);
		return consultarPersonaSinUsuarios(proveedor, fieldSort, sortDirection);
	}

	public Specification<Proveedor> consultarProveedoresListaClasificacionRepuesto(final Persona persona, final String fieldSort, final Boolean sortDirection,
			final Integer idRequerimiento, final List<Integer> idsClasificacionRepuesto){
		return new Specification<Proveedor>(){
			@SuppressWarnings("unchecked")
			public Predicate toPredicate(Root<Proveedor> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("clasificacionRepuestos", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				//3. Creamos los campos a seleccionar
				criteriaQuery = criteriaBuilder.createTupleQuery();
				criteriaQuery.multiselect(new Selection[]{
						entity.get("id"),
						entity.get("cedula"),
						entity.get("correo"),
						entity.get("direccion"),
						entity.get("nombre"),
						entity.get("telefono"),
						entity.get("estatus"),
						entity.get("tipoProveedor")
				}).distinct(true);
				
				//4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				restricciones.add(joins.get("clasificacionRepuestos").get("idClasificacionRepuesto").in(idsClasificacionRepuesto));
				
				entidades = new HashMap<String, JoinType>();
				entidades.put("proveedor", JoinType.INNER);
				entidades.put("detalleCotizacions", JoinType.INNER);
				Map<String, Object> paramSubquery = createSubquery(Cotizacion.class, "idCotizacion", entidades);
				Subquery<Cotizacion> subQuCotizacion = (Subquery<Cotizacion>) paramSubquery.get("subquery");
				Map<String, Join<?,?>> joinsSubquery = (Map<String, Join<?,?>>) paramSubquery.get("joins");
				
				Join<?,?> joinDetalleRequerimiento = joinsSubquery.get("detalleCotizacions").join("detalleRequerimiento");
				Join<?,?> joinProveedor = joinsSubquery.get("proveedor");
				
				List<Predicate> restriccionesSubquery = new ArrayList<Predicate>();
				
				restriccionesSubquery.add(criteriaBuilder.equal(
						joinDetalleRequerimiento.join("requerimiento").get("idRequerimiento"),
						idRequerimiento
				));
				
				restriccionesSubquery.add(criteriaBuilder.equal(joinProveedor.get("id"), entity.get("id")));
				restriccionesSubquery.add(joinDetalleRequerimiento.join("clasificacionRepuesto", JoinType.LEFT)
					.in(idsClasificacionRepuesto));
				
				subQuCotizacion=addRestriccionesSubquery(subQuCotizacion, restriccionesSubquery);
				
				restricciones.add(criteriaBuilder.not(criteriaBuilder.exists(subQuCotizacion)));
				
				Proveedor proveedor = (persona==null) ? new Proveedor() : new Proveedor(persona);
				agregarFiltros(proveedor, restricciones);


				//4. Creamos los campos de ordenamiento y ejecutamos
				List<Order> orders = new ArrayList<Order>();
				orders.add(criteriaBuilder.asc(entity.get("id")));
				criteriaQuery.orderBy(orders);
				
				return crearPredicate(restricciones);
			}
			
		};
	}
	
	public Specification<Proveedor> consultarProveedoresConSolicitudCotizaciones(final Proveedor proveedor, final Integer idRequerimiento, 
			final String fieldSort, final Boolean sortDirection){
		return new Specification<Proveedor>(){
			public Predicate toPredicate(Root<Proveedor> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("cotizacions", JoinType.INNER);
				entidades.put("usuario", JoinType.LEFT);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				//3. Creamos los campos a seleccionar
				criteriaQuery = criteriaBuilder.createTupleQuery();
				criteriaQuery.multiselect(new Selection[]{
						entity.get("id"),
						entity.get("cedula"),
						entity.get("correo"),
						entity.get("direccion"),
						entity.get("nombre"),
						entity.get("telefono"),
						entity.get("estatus"),
						entity.get("tipoProveedor")
				}).distinct(true);
				
				//4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				restricciones.add(criteriaBuilder.isNotEmpty(entity.<List<?>>get("cotizacions")));
				restricciones.add(criteriaBuilder.equal(joins.get("cotizacions").get("estatus"), "SC"));
				restricciones.add(criteriaBuilder.equal(
						joins.get("cotizacions").join("detalleCotizacions").join("detalleRequerimiento")
						.join("requerimiento").get("idRequerimiento"),
						idRequerimiento));
				
				
				Proveedor proveedorF = (proveedor==null) ? new Proveedor() : proveedor;
				agregarFiltros(proveedorF, restricciones);
				
				//4. Creamos los campos de ordenamiento y ejecutamos
				Map<String, Boolean> orders = new HashMap<String, Boolean>();
				
				if(fieldSort!=null && sortDirection!=null)
					orders.put(fieldSort, sortDirection);
				else
					orders.put("id", true);
				return crearPredicate(restricciones, orders);
			}
			
		};
	}
	
	/**METODOS OVERRIDE*/
	@Override
	protected void agregarRestriccionesPersona(Proveedor persona, List<Predicate> restricciones) {
		// TODO Auto-generated method stub
		
	}

}
