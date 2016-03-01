package com.okiimport.app.dao.maestros;

import com.okiimport.app.model.Deposito;

import com.okiimport.app.resource.dao.IGenericJPARepository;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface DepositoRepository extends IGenericJPARepository<Deposito, Integer> {
	
	
}
