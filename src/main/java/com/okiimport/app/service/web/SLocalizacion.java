package com.okiimport.app.service.web;

import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.Pais;

public interface SLocalizacion {
	long calcularDistancia(Ciudad ciudadOrigen, Ciudad ciudadDestino);
	long calcularDistancia(Pais paisOrigen, Pais paisDestino);
}
