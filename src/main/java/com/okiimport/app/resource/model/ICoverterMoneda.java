package com.okiimport.app.resource.model;

public interface ICoverterMoneda {
	Number getMontoPorUnidadBase();
	String getSimboloMoneda();
	String withSimbolo(Object val);
	Number convert(Number val);
	Number deconvert(Number val);
}
