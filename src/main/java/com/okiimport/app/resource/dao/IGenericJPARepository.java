package com.okiimport.app.resource.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IGenericJPARepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
	long count(Specification<T> specification);
	T findOne(Specification<T> specification);
	List<T> findAll(Specification<T> specification);
	List<T> findAll(Specification<T> specification, Sort sort);
	Page<T> findAll(Specification<T> specification, Pageable pageable);
}
