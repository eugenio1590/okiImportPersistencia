package com.okiimport.app.service.maestros;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Proveedor;

@Service
@Transactional
public interface SMaestros {
	//Marcas
	Map<String,Object> consultarMarcas(int page, int limit);
	MarcaVehiculo registrarMarca(MarcaVehiculo marca);
	
	//Estados
	Map<String,Object> ConsultarEstado(int page, int limit);
			
	//Ciudades
	Map<String,Object> ConsultarCiudad(Integer idEstado, int page, int limit);
	
	//Personas
	<T extends Persona> T acutalizarPersona(T persona);
	
	//Cliente
	Cliente registrarOActualizarCliente(Cliente cliente);
	Cliente consultarCliente(Cliente cliente);

	//Analistas
	Map<String, Object> consultarAnalistasSinUsuarios(Persona personaF,  String fieldSort, Boolean sortDirection, 
			int page, int limit);
	Map<String, Object> consultarAdministradoresSinUsuarios(Persona personaF,  String fieldSort, Boolean sortDirection, 
			int page, int limit);
	List<Analista> consultarCantRequerimientos(List<String> estatus, int page, int limit);
	Map<String, Object> consultarAnalistas(Analista analista, int page, int limit);
	/*public Map<String, Object> consultarAnalistas(Analista analistaF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit);*/
	Analista registrarAnalista(Analista analista);
	
	
	//Proveedores
	Map<String, Object> consultarProveedoresSinUsuarios(Persona personaF, String fieldSort, Boolean sortDirection,
			int page, int limit);
	
	Map<String,Object> ConsultarClasificacionRepuesto(int page, int limit);
	
	Proveedor registrarProveedor(Proveedor proveedor);
	
	Map<String,Object> ConsultarMotor(int page, int limit);
	
	Map<String, Object> ConsultarProveedoresListaClasificacionRepuesto(Persona persona, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, List<Integer> idsClasificacionRepuesto, int page, int limit);
	
	Map<String, Object> consultarProveedores(Proveedor proveedor, int page, int limit);
	
	Map<String, Object> consultarProveedoresConSolicitudCotizaciones(Proveedor proveedor, Integer idRequerimiento, 
			String fieldSort, Boolean sortDirection, int page, int limit);
	
	
	  
	//Banco
	Map<String, Object> consultarBancos(int page, int limit);

}
