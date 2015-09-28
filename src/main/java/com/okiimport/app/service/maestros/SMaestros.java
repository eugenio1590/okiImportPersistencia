package com.okiimport.app.service.maestros;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.Motor;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Proveedor;

@Service
@Transactional
public interface SMaestros {
	//Marcas
	@Transactional(readOnly=true)
	Map<String,Object> consultarMarcas(int page, int limit);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	MarcaVehiculo registrarMarca(MarcaVehiculo marca);
	
	//Estados
	@Transactional(readOnly=true)
	Map<String,Object> ConsultarEstado(int page, int limit);
			
	//Ciudades
	@Transactional(readOnly=true)
	Map<String,Object> ConsultarCiudad(Integer idEstado, int page, int limit);
	
	//Personas
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	<T extends Persona> T acutalizarPersona(T persona);
	
	//Cliente
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Cliente registrarOActualizarCliente(Cliente cliente);
	
	@Transactional(readOnly=true)
	Cliente consultarCliente(Cliente cliente);

	//Analistas
	@Transactional(readOnly=true)
	Map<String, Object> consultarAnalistasSinUsuarios(Persona personaF,  String fieldSort, Boolean sortDirection, 
			int page, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarAdministradoresSinUsuarios(Persona personaF,  String fieldSort, Boolean sortDirection, 
			int page, int limit);
	
	@Transactional(readOnly=true)
	List<Analista> consultarCantRequerimientos(List<String> estatus, int page, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarAnalistas(Analista analista, int page, int limit);
	/*public Map<String, Object> consultarAnalistas(Analista analistaF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit);*/
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Analista registrarAnalista(Analista analista);
	
	//Proveedores
	@Transactional(readOnly=true)
	Map<String, Object> consultarProveedoresSinUsuarios(Persona personaF, String fieldSort, Boolean sortDirection,
			int page, int limit);
	
	@Transactional(readOnly=true)
	Map<String,Object> ConsultarClasificacionRepuesto(int page, int limit);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Proveedor registrarProveedor(Proveedor proveedor);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarProveedores(Proveedor proveedor, int page, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarProveedoresConSolicitudCotizaciones(Proveedor proveedor, Integer idRequerimiento, 
			String fieldSort, Boolean sortDirection, int page, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> ConsultarProveedoresListaClasificacionRepuesto(Persona persona, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, List<Integer> idsClasificacionRepuesto, int page, int limit);
	
	//Motor
	@Transactional(readOnly=true)
	Map<String,Object> consultarMotores(Motor motor, String fieldSort, Boolean sortDirection, int page, int limit);
	
	//Banco
	@Transactional(readOnly=true)
	Map<String, Object> consultarBancos(int page, int limit);
	
	//Pais
	@Transactional(readOnly=true)
	Map<String, Object> consultarPaises(int page, int limit);

}
