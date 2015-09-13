package com.okiimport.app.service.seguridad;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface SAcceso extends UserDetailsService {
	Boolean validarAutenticacion(User user);
	UsernamePasswordAuthenticationToken consultarAutenticacion(User user);
}
