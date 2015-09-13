package com.okiimport.app.service.maestros.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.dao.maestros.AnalistaRepository;
import com.okiimport.app.dao.maestros.BancoRepository;
import com.okiimport.app.dao.maestros.CiudadRepository;
import com.okiimport.app.dao.maestros.ClasificacionRepuestoRepository;
import com.okiimport.app.dao.maestros.ClienteRepository;
import com.okiimport.app.dao.maestros.EstadoRepository;
import com.okiimport.app.dao.maestros.MarcaVehiculoRepository;
import com.okiimport.app.dao.maestros.MotorRepository;
import com.okiimport.app.dao.maestros.PersonaRepository;
import com.okiimport.app.dao.maestros.ProveedorRepository;
import com.okiimport.app.dao.maestros.impl.AnalistaDAO;
import com.okiimport.app.dao.maestros.impl.ClienteDAO;
import com.okiimport.app.dao.maestros.impl.ProveedorDAO;
import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Banco;
import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.ClasificacionRepuesto;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Estado;
import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.Motor;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.maestros.SMaestros;

public class SMaestrosImpl extends AbstractServiceImpl implements SMaestros {

	@Autowired
	private MarcaVehiculoRepository marcaVehiculoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CiudadRepository ciudadRepository;
	
	@Autowired
    private ClienteRepository clienteRepository;
	
	@Autowired
	private AnalistaRepository analistaRepository;
	
	@Autowired
	private ProveedorRepository proveedorRepository;
	
	@Autowired
	private ClasificacionRepuestoRepository clasificacionRepuestoRepository;
	
	@Autowired
	private MotorRepository motorRepository;
	
	@Autowired
	private BancoRepository bancoRepository;
		
	//Marcas
	public Map<String, Object> consultarMarcas(int page, int limit) {
		Map<String, Object> Parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<MarcaVehiculo> marcasVehiculo = null;
		if(limit>0){
			Page<MarcaVehiculo> pageMarcaVehiculo = this.marcaVehiculoRepository.findByEstatusIgnoreCase("activo", new PageRequest(page, limit));
			total = Long.valueOf(pageMarcaVehiculo.getTotalElements()).intValue();
			marcasVehiculo = pageMarcaVehiculo.getContent();
		}
		else {
			marcasVehiculo = this.marcaVehiculoRepository.findByEstatusIgnoreCase("activo");
			total = marcasVehiculo.size();
		}
		Parametros.put("total", total);
		Parametros.put("marcas", marcasVehiculo);
		return Parametros;
	}
	
	public MarcaVehiculo registrarMarca(MarcaVehiculo marca) {
	   return marcaVehiculoRepository.save(marca);
	}
	
	//Estados
	public Map<String, Object> ConsultarEstado(int page, int limit) {
		Map<String, Object> Parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Estado> estados = null;
		if(limit>0){
			Page<Estado> pageEstado = this.estadoRepository.findAll(new PageRequest(page, limit));
			total = Long.valueOf(pageEstado.getTotalElements()).intValue();
			estados = pageEstado.getContent();
		}
		else {
			total = Long.valueOf(this.estadoRepository.count()).intValue();
			estados = this.estadoRepository.findAll();
		}
		Parametros.put("total", total);
		Parametros.put("estados", estados);
		return Parametros;
	}
		
	//Ciudades
	public Map<String, Object> ConsultarCiudad(Integer idEstado, int page, int limit) {
		Map<String, Object> Parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Ciudad> ciudades = null;
		if(limit>0){
			Page<Ciudad> pageCiudad = this.ciudadRepository.findByEstado_IdEstado(idEstado, new PageRequest(page, limit));
			total = Long.valueOf(pageCiudad.getTotalElements()).intValue();
			ciudades = pageCiudad.getContent();
		}
		else {
			ciudades = this.ciudadRepository.findByEstado_IdEstado(idEstado);
			total = ciudades.size();
		}
		Parametros.put("total", total);
		Parametros.put("ciudades", ciudades);
		return Parametros;
	}
	
	//Persona
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Persona> T acutalizarPersona(T persona){
		PersonaRepository dao = determinarPersonaDAO(persona.getClass());
		if(dao!=null)
			persona = (T) dao.save(persona);
		return persona;
	}
	
	//Cliente
	public Cliente registrarOActualizarCliente(Cliente cliente) {
		Cliente temp = clienteRepository.findByCedula(cliente.getCedula());
		if (temp==null)
			cliente = clienteRepository.save(cliente);
		else{
			cliente.setId(temp.getId());
			cliente = clienteRepository.save(cliente);
		}
		return cliente;
	}
	
	public Cliente consultarCliente(Cliente cliente) {
		Specification<Cliente> specfCliente = (new ClienteDAO()).consultarPersona(cliente);
		List<Cliente> clientes = this.clienteRepository.findAll(specfCliente);
		if(clientes!=null && clientes.size()>0)
			return clientes.get(0);
		return null;
	}
	
	//Analista
	public Map<String, Object> consultarAnalistasSinUsuarios(Persona personaF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Analista> analistas = null;
		Specification<Analista> specfAnalista = (new AnalistaDAO()).consultarAnalistasSinUsuarios(personaF, fieldSort, sortDirection);
		if(limit>0){
			Page<Analista> pageAnalista = this.analistaRepository.findAll(specfAnalista, new PageRequest(pagina, limit));
			total = Long.valueOf(pageAnalista.getTotalElements()).intValue();
			analistas = pageAnalista.getContent();
		}
		else {
			analistas = this.analistaRepository.findAll(specfAnalista);
			total = analistas.size();
		}
		parametros.put("total", total);
		parametros.put("analistas", analistas);
		return parametros;
	}
	
	public Map<String, Object> consultarAdministradoresSinUsuarios(Persona personaF,  String fieldSort, Boolean sortDirection, 
			int page, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Analista> analistas = null;
		Specification<Analista> specfAnalista = (new AnalistaDAO()).consultarAdministradoresSinUsuarios(personaF, fieldSort, sortDirection);
		if(limit>0){
			Page<Analista> pageAnalista = this.analistaRepository.findAll(specfAnalista, new PageRequest(page, limit));
			total = Long.valueOf(pageAnalista.getTotalElements()).intValue();
			analistas = pageAnalista.getContent();
		}
		else {
			analistas = this.analistaRepository.findAll(specfAnalista);
			total = analistas.size();
		}
		parametros.put("total", total);
		parametros.put("administradores", analistas);
		return parametros;
	}
	
	public List<Analista> consultarCantRequerimientos(List<String> estatus, int page, int limit){
		Specification<Analista> specfAnalista = (new AnalistaDAO()).consultarCantRequerimientos(estatus);
		List<Analista> analistas = null;
		if(limit>0)
			analistas = this.analistaRepository.findAll(specfAnalista, new PageRequest(page, limit)).getContent();
		else
			analistas = this.analistaRepository.findAll(specfAnalista);
		return analistas;
	}
	
	public Map<String, Object> consultarAnalistas(Analista analista, int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Analista> analistas = null;
		Specification<Analista> specfAnalista = (new AnalistaDAO()).consultarPersona(analista);
		
		if(limit>0){
			Page<Analista> pageAnalista = this.analistaRepository.findAll(specfAnalista, new PageRequest(page, limit));
			total = Long.valueOf(pageAnalista.getTotalElements()).intValue();
			analistas = pageAnalista.getContent();
		}
		else {
			analistas = this.analistaRepository.findAll(specfAnalista);
			total = analistas.size();
		}
		parametros.put("total", total);
		parametros.put("analistas", analistas);
		return parametros;
	}
	
	public Analista registrarAnalista(Analista analista) {
	   return this.analistaRepository.save(analista);
	}
	
	//Proveedores
	public Map<String, Object> consultarProveedoresSinUsuarios(Persona personaF, String fieldSort, Boolean sortDirection,
			int page, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Proveedor> proveedores = null;
		Specification<Proveedor> specfProveedro = 
				(new ProveedorDAO()).consultarProveedoresSinUsuarios(personaF, fieldSort, sortDirection);
		if(limit>0){
			Page<Proveedor> pageProveedor = this.proveedorRepository.findAll(specfProveedro, new PageRequest(page, limit));
			total = Long.valueOf(pageProveedor.getTotalElements()).intValue();
			proveedores = pageProveedor.getContent();
		}
		else {
			proveedores = this.proveedorRepository.findAll(specfProveedro);
			total = proveedores.size();
		}
		parametros.put("total", total);
		parametros.put("proveedores", proveedores);
		return parametros;
	}
	
	public Proveedor registrarProveedor(Proveedor proveedor) {
		for(ClasificacionRepuesto clasificacion : proveedor.getClasificacionRepuestos())
			clasificacion.getProveedores().add(proveedor);
		for(MarcaVehiculo marca : proveedor.getMarcaVehiculos())
			marca.getProveedores().add(proveedor);

		return proveedor = this.proveedorRepository.save(proveedor);
	}
	
	public Map<String, Object> ConsultarProveedoresListaClasificacionRepuesto(Persona persona, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, List<Integer> idsClasificacionRepuesto, int page, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Proveedor> proveedores = null;
		Specification<Proveedor> specfProveedor = (new ProveedorDAO()).consultarProveedoresListaClasificacionRepuesto(persona, fieldSort, sortDirection, idRequerimiento, idsClasificacionRepuesto);
		if(limit>0){
			Page<Proveedor> pageProveedor = this.proveedorRepository.findAll(specfProveedor, new PageRequest(page, limit));
			total = Long.valueOf(pageProveedor.getTotalElements()).intValue();
			proveedores = pageProveedor.getContent();
		}
		else {
			proveedores = this.proveedorRepository.findAll(specfProveedor);
			total = proveedores.size();
		}
		parametros.put("total", total);
		parametros.put("proveedores", proveedores);
		return parametros;
	}
	
	public Map<String, Object> consultarProveedores(Proveedor proveedor, int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Proveedor> proveedores = null;
		Specification<Proveedor> specfProveedor = (new ProveedorDAO()).consultarPersona(proveedor);
		if(limit>0){
			Page<Proveedor> pageProveedor = this.proveedorRepository.findAll(specfProveedor, new PageRequest(page, limit));
			total = Long.valueOf(pageProveedor.getTotalElements()).intValue();
			proveedores = pageProveedor.getContent();
		}
		else {
			proveedores = this.proveedorRepository.findAll(specfProveedor);
			total = proveedores.size();
		}
		parametros.put("total", total);
		parametros.put("proveedores", proveedores);
		return parametros;
	}
	
	public Map<String, Object> consultarProveedoresConSolicitudCotizaciones(Proveedor proveedor, Integer idRequerimiento, 
			String fieldSort, Boolean sortDirection, int page, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Proveedor> proveedores = null;
		Specification<Proveedor> specfProveedor = (new ProveedorDAO()).consultarProveedoresConSolicitudCotizaciones(proveedor, idRequerimiento, fieldSort, sortDirection);
		if(limit>0){
			Page<Proveedor> pageProveedor = this.proveedorRepository.findAll(specfProveedor, new PageRequest(page, limit));
			total = Long.valueOf(pageProveedor.getTotalElements()).intValue();
			proveedores = pageProveedor.getContent();
		}
		else {
			proveedores = this.proveedorRepository.findAll(specfProveedor);
			total = proveedores.size();
		}
		parametros.put("total", total);
		parametros.put("proveedores", proveedores);
		return parametros;
	}
	
	//Clasificacion Repuesto
	public Map<String, Object> ConsultarClasificacionRepuesto(int page, int limit) {
		Map<String, Object> Parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<ClasificacionRepuesto> clasfRepuestos = null;
		if(limit>0){
			Page<ClasificacionRepuesto> pageClasfRepuesto = this.clasificacionRepuestoRepository.findAll(new PageRequest(page, limit));
			total = Long.valueOf(pageClasfRepuesto.getTotalElements()).intValue();
			clasfRepuestos = pageClasfRepuesto.getContent();
		}
		else {
			clasfRepuestos = this.clasificacionRepuestoRepository.findAll();
			total = clasfRepuestos.size();
		}
		Parametros.put("total", total);
		Parametros.put("clasificacionRepuesto", clasfRepuestos);
		return Parametros;
	}
	
	//Motor
	public Map<String, Object> ConsultarMotor(int page, int limit) {
		Map<String, Object> Parametros= new HashMap<String, Object>();
		Integer total = 0;
		List<Motor> motores = null;
		if(limit>0){
			Page<Motor> pageMotor = this.motorRepository.findAll(new PageRequest(page, limit));
			total = Long.valueOf(pageMotor.getTotalElements()).intValue();
			motores = pageMotor.getContent();
		}
		else {
			motores = this.motorRepository.findAll();
			total = motores.size();
		}
		Parametros.put("total", total);
		Parametros.put("motor", motores);
		return Parametros;
	}
	
	//Banco
	public Map<String, Object> consultarBancos(int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Banco> bancos = null;
		if(limit>0){
			Page<Banco> pageBanco = this.bancoRepository.findAll(new PageRequest(page, limit));
			total = Long.valueOf(pageBanco.getTotalElements()).intValue();
			bancos = pageBanco.getContent();
		}
		else {
			bancos = this.bancoRepository.findAll();
			total = bancos.size();
		}
		parametros.put("total", total);
		parametros.put("bancos", bancos);
		return parametros;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T extends PersonaRepository, Y extends Persona> T determinarPersonaDAO(Class<Y> clase){
		T dao = null;
		if(clase != null){
			if(clase.equals(Analista.class))
				dao = (T) this.analistaRepository;
			else if(clase.equals(Proveedor.class))
				dao = (T) this.proveedorRepository;
			else if(clase.equals(Cliente.class))
				dao = (T) this.clienteRepository;
		}
		return dao;
	}
}
