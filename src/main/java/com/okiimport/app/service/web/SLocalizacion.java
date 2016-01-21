package com.okiimport.app.service.web;

import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.Pais;

public interface SLocalizacion {
	double calcularDistancia(Ciudad ciudadOrigen, Ciudad ciudadDestino);
	double calcularDistancia(Pais paisOrigen, Pais paisDestino);
}
