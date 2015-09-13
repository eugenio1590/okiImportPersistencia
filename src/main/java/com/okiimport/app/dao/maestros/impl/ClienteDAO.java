package com.okiimport.app.dao.maestros.impl;

import java.util.List;

import javax.persistence.criteria.Predicate;

import com.okiimport.app.model.Cliente;

public class ClienteDAO extends PersonaDAO<Cliente> {

	@Override
	protected void agregarRestriccionesPersona(Cliente persona, List<Predicate> restricciones) {
		// TODO Auto-generated method stub
		
	}

}
