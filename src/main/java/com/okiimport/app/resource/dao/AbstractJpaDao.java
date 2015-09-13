package com.okiimport.app.resource.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.Attribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

public abstract class AbstractJpaDao<T> {
	
	//Finales
	protected static final Logger logger =  LoggerFactory.getLogger(AbstractJpaDao.class);
	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	//Atributos
	protected CriteriaBuilder criteriaBuilder;
	protected CriteriaQuery<?> criteriaQuery;
	protected Root<T> entity;
	
	protected void inicializar(Root<T> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
		this.criteriaBuilder = criteriaBuilder;
		this.criteriaQuery = criteriaQuery;
		this.entity = entity;
	}
	
	
	public Specification<T> findByExample(final T exampleInstance){
		return new Specification<T>() {
			
			public Predicate toPredicate(Root<T> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub
				return constructExample(exampleInstance, entity, criteriaQuery, criteriaBuilder);
			}
			
		};
	}
	
	protected Predicate constructExample(T exampleInstance, Root<T> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
		Predicate p = criteriaBuilder.conjunction();
		try {
			for (Attribute<? super T, ?> a: entity.getModel().getAttributes()) {
				if(!a.getClass().getCanonicalName().equalsIgnoreCase("org.hibernate.ejb.metamodel.PluralAttributeImpl.ListAttributeImpl")){
					
				String name = a.getName();
				String javaName = a.getJavaMember().getName();
				String getter = "get" + javaName.substring(0,1).toUpperCase() + javaName.substring(1);
				Method method = exampleInstance.getClass().getDeclaredMethod(getter, (Class<?>[]) null);
				Object result = method.invoke(exampleInstance, (Object[]) null);
				if (result !=  null)
					p = criteriaBuilder.and(p, criteriaBuilder.like(
							criteriaBuilder.lower(entity.get(name).as(String.class)), 
							"%"+String.valueOf(result).toLowerCase()+"%"
							));
				}
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}

	protected void agregarOrder(String fieldSort, Boolean sortDirection, List<Order> orders){
		Order order = (sortDirection) 
				? criteriaBuilder.asc(entity.get(fieldSort)) 
					: criteriaBuilder.desc(entity.get(fieldSort));
		if(orders!=null)
			orders.add(order);
	}
	
	protected Map<String, Join<?,?>> crearJoins(Map<String, JoinType> entidades){
		Map<String, Join<?,?>> joins = new HashMap<String, Join<?,?>>();
		if(entidades!=null)
			for(String entidad : entidades.keySet())
				joins.put(entidad, entity.join(entidad, entidades.get(entidad)));
		return joins;
	}
	
	protected Predicate crearPredicate(List<Predicate> restricciones){
		return criteriaBuilder.and(restricciones.toArray(new Predicate[]{}));
	}
	
	protected Predicate crearPredicate(List<Predicate> restricciones, Map<String, Boolean> orders){
		if(orders!=null){
			for(String field : orders.keySet())
				agregarOrder(field, orders.get(field), null);
		}
		return crearPredicate(restricciones);
	}
	
	protected Predicate crearPredicate(List<Predicate> restricciones, List<Expression<?>> grouping){
		criteriaQuery.groupBy(grouping);
		return criteriaBuilder.and(restricciones.toArray(new Predicate[]{}));
	}
	
	/**SUBQUERY*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <Y> Map<String, Object> createSubquery(Class<Y> clazz, String field, Map<String, JoinType> entidades){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Map<String, Join> joins = new HashMap();
		Subquery<Y> subquery = criteriaBuilder.createQuery().subquery(clazz);
		Root fromY = subquery.from(clazz);
		subquery.select(fromY.get(field));
		if(entidades != null){
			for (String entidad : entidades.keySet()){
				Join<Object, Object> join = fromY.join(entidad, entidades.get(entidad));
				joins.put(entidad, join);
			}
		}
		parametros.put("subquery", subquery);
		parametros.put("joins", joins);
		return parametros;
	}
	
	protected <Y> Subquery<Y> addRestriccionesSubquery(Subquery<Y> subquery, List<Predicate> conditions){
		subquery=(conditions!=null) ? subquery.where(conditions.toArray(new Predicate[]{})) : subquery;
		return subquery;
	}
	
}
