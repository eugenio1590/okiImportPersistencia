package com.okiimport.app.dao.maestros;

import org.springframework.data.repository.NoRepositoryBean;

import com.okiimport.app.model.Persona;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@NoRepositoryBean
public interface PersonaRepository<T extends Persona> extends IGenericJPARepository<T, Integer> {
	T findByUsuarioUsernameIgnoreCase(String username);
	T findByCedula(String cedula);
	T findByCorreo(String correo);
}
