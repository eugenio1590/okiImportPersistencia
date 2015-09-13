package com.okiimport.app.dao.configuracion;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Menu;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface MenuRepository extends IGenericJPARepository<Menu, Integer> {
	List<Menu> findByPadreIsNullAndTipoOrderByIdMenu(Integer tipo);
}
