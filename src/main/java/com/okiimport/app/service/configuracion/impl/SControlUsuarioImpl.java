package com.okiimport.app.service.configuracion.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.GrantedAuthorityImpl;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.okiimport.app.dao.configuracion.MenuRepository;
import com.okiimport.app.dao.configuracion.UsuarioRepository;
import com.okiimport.app.dao.configuracion.impl.MenuDAO;
import com.okiimport.app.dao.configuracion.impl.UsuarioDAO;
import com.okiimport.app.model.Menu;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.configuracion.SControlUsuario;
import com.okiimport.app.service.maestros.SMaestros;

@Service
public class SControlUsuarioImpl extends AbstractServiceImpl implements SControlUsuario {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private MenuRepository menuRepository;

	public SControlUsuarioImpl() {
	}
	
	//1.Usuario
	public Usuario consultarUsuario(Integer id){
		return this.usuarioRepository.findOne(id);
	}
	
	public Usuario consultarUsuario(String usuario, String clave) {
		return this.usuarioRepository.findByUsernameIgnoreCaseAndPaswordIgnoreCase(usuario, clave);
	}
	
	public Usuario grabarUsuario(Usuario usuario, SMaestros sMaestros) {
		//usuario.setPasword(this.bcryptEncoder.encode(usuario.getPasword()));
		Persona persona = usuario.getPersona();
		persona.getTipoMenu();
		usuario = actualizarUsuario(usuario, true);
		persona = sMaestros.acutalizarPersona(persona);
		usuario.setPersona(persona);
		return usuario;
	}
	
	public Usuario actualizarUsuario(Usuario usuario, boolean encriptar){
		/*if(encriptar)
			usuario.setPasword(this.bcryptEncoder.encode(usuario.getPasword()));*/
		return this.usuarioRepository.save(usuario);
	}
	
	public Boolean cambiarEstadoUsuario(Usuario usuario, boolean estado){
		if((usuario=consultarUsuario(usuario.getId()))!=null) {
			usuario.setActivo(estado);
			actualizarUsuario(usuario, false);
			return true;
		}
		
		return false;
	}
	
	public Map<String, Object> consultarUsuarios(Usuario usuarioF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Usuario> usuarios = null;
		if(limit != -1){
			Specification<Usuario> specfUsuario = (new UsuarioDAO()).consultarUsuarios(usuarioF, fieldSort, sortDirection);
			Page<Usuario> pageUsuario = this.usuarioRepository.findAll(specfUsuario, new PageRequest(pagina, limit));
			total = Long.valueOf(pageUsuario.getTotalElements()).intValue();
			usuarios = pageUsuario.getContent();
		}
		else {
			total = Long.valueOf(this.usuarioRepository.count()).intValue();
			usuarios = this.usuarioRepository.findAll();
		}
		parametros.put("total", total);
		parametros.put("usuarios", usuarios);
		return parametros;
	}
	
	public boolean verificarUsername(String username){
		Usuario usuario = consultarUsuario(username, null);
		return (usuario!=null);
	}
	
	public Usuario consultarUsuario(int idPersona) {
		return this.usuarioRepository.findByPersonaIdOrderById(idPersona);
	}
	
	//2. Menus
	public List<Menu> consultarPadresMenuUsuario(Integer tipo) {
		Specification<Menu> specfMenu = (new MenuDAO()).consultarPadresMenuUsuario(tipo);
		return this.menuRepository.findAll(specfMenu);
	}
}
