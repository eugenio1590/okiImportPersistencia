package com.okiimport.app.service.configuracion;

import java.util.List;
import java.util.Map;

//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.User;




import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.model.Menu;
import com.okiimport.app.model.Usuario;

@Service
@Transactional
public interface SControlUsuario {
	//Usuarios
	@Transactional(readOnly=true)
	Usuario consultarUsuario(Integer id);
	
	@Transactional(readOnly=true)
	Usuario consultarUsuario(String usuario, String clave);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Usuario grabarUsuario(Usuario usuario, SMaestros smaestros);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Usuario actualizarUsuario(Usuario usuario, boolean encriptar);
	
	@Transactional(readOnly=true)
	Boolean cambiarEstadoUsuario(Usuario usuario, boolean estado);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarUsuarios(Usuario usuarioF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	boolean verificarUsername(String username);
	
	@Transactional(readOnly=true)
	Usuario consultarUsuario(int idPersona);

	//Menu
	@Transactional(readOnly=true)
	List<Menu> consultarPadresMenuUsuario(Integer tipo);
	@Transactional(readOnly=true)
	List<Menu> consultarHijosMenuUsuario(Integer tipo);
}
