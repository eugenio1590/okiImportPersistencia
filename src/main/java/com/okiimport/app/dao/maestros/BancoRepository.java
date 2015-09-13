package com.okiimport.app.dao.maestros;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Banco;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface BancoRepository extends IGenericJPARepository<Banco, Integer> {

}
