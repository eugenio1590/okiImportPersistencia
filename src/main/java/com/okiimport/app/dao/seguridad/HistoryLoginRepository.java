package com.okiimport.app.dao.seguridad;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.HistoryLogin;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface HistoryLoginRepository extends IGenericJPARepository<HistoryLogin, Integer> {
	HistoryLogin findByDateLogoutIsNullAndUsuarioUsernameContainingIgnoreCase(String username);
}
