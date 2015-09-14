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
	Usuario consultarUsuario(Integer id);
	
	Usuario consultarUsuario(String usuario, String clave);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Usuario grabarUsuario(Usuario usuario, SMaestros smaestros);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Usuario actualizarUsuario(Usuario usuario, boolean encriptar);
	
	Boolean cambiarEstadoUsuario(Usuario usuario, boolean estado);
	
	Map<String, Object> consultarUsuarios(Usuario usuarioF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit);
	
	boolean verificarUsername(String username);
	
	Usuario consultarUsuario(int idPersona);

	//Menu
	List<Menu> consultarPadresMenuUsuario(Integer tipo);
}
