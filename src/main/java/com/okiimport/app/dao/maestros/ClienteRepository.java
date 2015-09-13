package com.okiimport.app.dao.maestros;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Cliente;

@Repository
public interface ClienteRepository extends PersonaRepository<Cliente> {

}
