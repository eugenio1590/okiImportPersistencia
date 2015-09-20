package com.okiimport.app.dao.configuracion;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Menu;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface MenuRepository extends IGenericJPARepository<Menu, Integer> {
	List<Menu> findByPadreNullAndTipo(Integer tipo, Sort sort);
	//List<Menu> findByHijosNotExistsAndTipo(Integer tipo, Sort sort);
	List<Menu> findByHijosNullAndTipo(Integer tipo, Sort sort);
}
