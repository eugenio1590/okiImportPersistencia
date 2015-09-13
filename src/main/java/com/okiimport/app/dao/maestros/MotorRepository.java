package com.okiimport.app.dao.maestros;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Motor;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface MotorRepository extends IGenericJPARepository<Motor, Integer> {

}
