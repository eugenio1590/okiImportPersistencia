package com.okiimport.app.service.seguridad;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.okiimport.app.model.HistoryLogin;
import com.okiimport.app.model.Usuario;

@Service
@Transactional
public interface SHistorial {
	//Historial de Session
	HistoryLogin historicoSessionNoTerminada(String username);
	void registrarHistorialSession(Usuario usuario);
	void cerarHistorialSession(Usuario usuario);
}
