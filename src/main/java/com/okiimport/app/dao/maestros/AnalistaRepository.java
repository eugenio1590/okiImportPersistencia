package com.okiimport.app.dao.maestros;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Analista;

@Repository
public interface AnalistaRepository extends PersonaRepository<Analista> {
	Analista findByUsuarioUsernameContainingIgnoreCase(String username);
}
