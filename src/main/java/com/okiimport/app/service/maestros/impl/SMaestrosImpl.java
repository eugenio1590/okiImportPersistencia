package com.okiimport.app.service.maestros.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.dao.configuracion.HistoricoMonedaRepository;
import com.okiimport.app.dao.configuracion.MonedaRepository;
import com.okiimport.app.dao.maestros.AnalistaRepository;
import com.okiimport.app.dao.maestros.BancoRepository;
import com.okiimport.app.dao.maestros.CiudadRepository;
import com.okiimport.app.dao.maestros.ClasificacionRepuestoRepository;
import com.okiimport.app.dao.maestros.ClienteRepository;
import com.okiimport.app.dao.maestros.EstadoRepository;
import com.okiimport.app.dao.maestros.FormaPagoRepository;
import com.okiimport.app.dao.maestros.MarcaVehiculoRepository;
import com.okiimport.app.dao.maestros.MotorRepository;
import com.okiimport.app.dao.maestros.PaisRepository;
import com.okiimport.app.dao.maestros.PersonaRepository;
import com.okiimport.app.dao.maestros.ProveedorRepository;
import com.okiimport.app.dao.maestros.VehiculoRepository;
import com.okiimport.app.dao.maestros.impl.AnalistaDAO;
import com.okiimport.app.dao.maestros.impl.ClienteDAO;
import com.okiimport.app.dao.maestros.impl.MarcaDAO;
import com.okiimport.app.dao.maestros.impl.MotorDAO;
import com.okiimport.app.dao.maestros.impl.ProveedorDAO;
import com.okiimport.app.dao.maestros.impl.VehiculoDAO;
import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Banco;
import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.ClasificacionRepuesto;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Estado;
import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.FormaPago;
import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.Moneda;
import com.okiimport.app.model.Motor;
import com.okiimport.app.model.Pais;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.Vehiculo;
import com.okiimport.app.model.enumerados.EEstatusFormaPago;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.maestros.SMaestros;

public class SMaestrosImpl extends AbstractServiceImpl implements SMaestros {

	@Autowired
	private MarcaVehiculoRepository marcaVehiculoRepository;
	
	@Autowired
	private FormaPagoRepository formaPagoRepository;

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

	@Autowired
	private PaisRepository paisRepository;
	
	@Autowired
	private MonedaRepository monedaRepository;

	@Autowired
	private HistoricoMonedaRepository historicoRepository;

	@Autowired
	private VehiculoRepository vehiculoRepository;

	// Marcas
	public Map<String, Object> consultarMarcas(int page, int limit) {
		Map<String, Object> Parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<MarcaVehiculo> marcasVehiculo = null;
		if (limit > 0) {
			Page<MarcaVehiculo> pageMarcaVehiculo = this.marcaVehiculoRepository
					.findByEstatus(EEstatusGeneral.ACTIVO, new PageRequest(
							page, limit));
			total = Long.valueOf(pageMarcaVehiculo.getTotalElements())
					.intValue();
			marcasVehiculo = pageMarcaVehiculo.getContent();
		} else {
			marcasVehiculo = this.marcaVehiculoRepository
					.findByEstatus(EEstatusGeneral.ACTIVO);
			total = marcasVehiculo.size();
		}
		Parametros.put("total", total);
		Parametros.put("marcas", marcasVehiculo);
		return Parametros;
	}
	
	public Map<String, Object> consultarMarcasF(MarcaVehiculo marca, String fieldSort,
			Boolean sortDirection, int page, int limit) {
		Map<String, Object> Parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<MarcaVehiculo> marcasVehiculo = null;
		Sort sortMarca = new Sort(getDirection(sortDirection,
				Sort.Direction.ASC), getFieldSort(fieldSort, "idMarcaVehiculo"));
		Specification<MarcaVehiculo> specfMarca = (new MarcaDAO()).consultarMarcaVehiculos(marca);
		
		if (limit > 0) {
			
			Page<MarcaVehiculo> pageMarcaVehiculo = this.marcaVehiculoRepository.findAll(specfMarca,
					new PageRequest(page, limit, sortMarca));
			total = Long.valueOf(pageMarcaVehiculo.getTotalElements())
					.intValue();
			marcasVehiculo = pageMarcaVehiculo.getContent();
		} else {
			marcasVehiculo = this.marcaVehiculoRepository
					.findByEstatus(EEstatusGeneral.ACTIVO);
			total = marcasVehiculo.size();
		}
		Parametros.put("total", total);
		Parametros.put("marcas", marcasVehiculo);
		return Parametros;
	}
	
	// Formas de pago
	public Map<String, Object> consultarFormasPago(int page, int limit) {
			Map<String, Object> Parametros = new HashMap<String, Object>();
			Integer total = 0;
			List<FormaPago> formas = null;
			if (limit > 0) {
				Page<FormaPago> pageformas = this.formaPagoRepository
						.findByEstatus(EEstatusFormaPago.ACTIVO, new PageRequest(
								page, limit));
				total = Long.valueOf(pageformas.getTotalElements())
						.intValue();
				formas = pageformas.getContent();
			} else {
				formas = this.formaPagoRepository
						.findByEstatus(EEstatusFormaPago.ACTIVO);
				total = formas.size();
			}
			Parametros.put("total", total);
			Parametros.put("formasPago", formas);
			return Parametros;
	}

	public MarcaVehiculo registrarMarca(MarcaVehiculo marca) {
		return marcaVehiculoRepository.save(marca);
	}

	public Map<String, Object> consultarMarcasVehiculoProveedor(
			Integer idProveedor, int page, int limit) {
		Map<String, Object> Parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<MarcaVehiculo> marcasVehiculo = null;
		if (limit > 0) {
			Page<MarcaVehiculo> pageMarcaVehiculo = this.marcaVehiculoRepository
					.findByProveedoresId(idProveedor, new PageRequest(page,
							limit));
			total = Long.valueOf(pageMarcaVehiculo.getTotalElements())
					.intValue();
			marcasVehiculo = pageMarcaVehiculo.getContent();
		} else {
			marcasVehiculo = this.marcaVehiculoRepository
					.findByProveedoresId(idProveedor);
			total = marcasVehiculo.size();
		}
		Parametros.put("total", total);
		Parametros.put("marcas", marcasVehiculo);
		return Parametros;
	}

	// Estados
	public Map<String, Object> consultarEstados(int page, int limit) {
		Map<String, Object> Parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Estado> estados = null;
		if (limit > 0) {
			Page<Estado> pageEstado = this.estadoRepository
					.findAll(new PageRequest(page, limit));
			total = Long.valueOf(pageEstado.getTotalElements()).intValue();
			estados = pageEstado.getContent();
		} else {
			total = Long.valueOf(this.estadoRepository.count()).intValue();
			estados = this.estadoRepository.findAll();
		}
		Parametros.put("total", total);
		Parametros.put("estados", estados);
		return Parametros;
	}

	// Ciudades
	public Map<String, Object> ConsultarCiudad(Integer idEstado, int page,
			int limit) {
		Map<String, Object> Parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Ciudad> ciudades = null;
		if (limit > 0) {
			Page<Ciudad> pageCiudad = this.ciudadRepository
					.findByEstado_IdEstado(idEstado, new PageRequest(page,
							limit));
			total = Long.valueOf(pageCiudad.getTotalElements()).intValue();
			ciudades = pageCiudad.getContent();
		} else {
			ciudades = this.ciudadRepository.findByEstado_IdEstado(idEstado);
			total = ciudades.size();
		}
		Parametros.put("total", total);
		Parametros.put("ciudades", ciudades);
		return Parametros;
	}

	// Persona
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Persona> T acutalizarPersona(T persona) {
		PersonaRepository dao = determinarPersonaDAO(persona.getClass());
		if (dao != null)
			persona = (T) dao.save(persona);
		return persona;
	}

	// Cliente
	public Cliente registrarOActualizarCliente(Cliente cliente) {
		Cliente temp = clienteRepository.findByCedula(cliente.getCedula());
		if(temp != null)
			cliente.setId(temp.getId());
		return clienteRepository.save(cliente);
	}

	public Cliente consultarCliente(Cliente cliente) {
		Sort sortCliente = new Sort(Sort.Direction.ASC, "id");
		Specification<Cliente> specfCliente = (new ClienteDAO())
				.consultarPersona(cliente);
		List<Cliente> clientes = this.clienteRepository.findAll(specfCliente,
				sortCliente);
		if (clientes != null && clientes.size() > 0)
			return clientes.get(0);
		return null;
	}
	
	public Boolean consultarCorreoCliente(String correo) {
		Cliente cliente=new Cliente();

		cliente=this.clienteRepository.findByCorreo(correo);

		if(cliente!=null){
			System.out.println("correo existe es ---- "+cliente.getCorreo());
			return true;
		}else{
			return false;
		}

	}
	
	// Analista
	public Map<String, Object> consultarAnalistasSinUsuarios(Persona personaF,
			String fieldSort, Boolean sortDirection, int pagina, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Analista> analistas = null;
		Sort sortAnalista = new Sort(getDirection(sortDirection,
				Sort.Direction.ASC), getFieldSort(fieldSort, "id"));
		Specification<Analista> specfAnalista = (new AnalistaDAO())
				.consultarAnalistasSinUsuarios(personaF);
		if (limit > 0) {
			Page<Analista> pageAnalista = this.analistaRepository
					.findAll(specfAnalista, new PageRequest(pagina, limit,
							sortAnalista));
			total = Long.valueOf(pageAnalista.getTotalElements()).intValue();
			analistas = pageAnalista.getContent();
		} else {
			analistas = this.analistaRepository.findAll(specfAnalista,
					sortAnalista);
			total = analistas.size();
		}
		parametros.put("total", total);
		parametros.put("analistas", analistas);
		return parametros;
	}

	public Map<String, Object> consultarAdministradoresSinUsuarios(
			Persona personaF, String fieldSort, Boolean sortDirection,
			int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Analista> analistas = null;
		Sort sortAnalista = new Sort(getDirection(sortDirection,
				Sort.Direction.ASC), getFieldSort(fieldSort, "id"));
		Specification<Analista> specfAnalista = (new AnalistaDAO())
				.consultarAdministradoresSinUsuarios(personaF);
		if (limit > 0) {
			Page<Analista> pageAnalista = this.analistaRepository.findAll(
					specfAnalista, new PageRequest(page, limit, sortAnalista));
			total = Long.valueOf(pageAnalista.getTotalElements()).intValue();
			analistas = pageAnalista.getContent();
		} else {
			analistas = this.analistaRepository.findAll(specfAnalista,
					sortAnalista);
			total = analistas.size();
		}
		parametros.put("total", total);
		parametros.put("administradores", analistas);
		return parametros;
	}

	public List<Analista> consultarCantRequerimientos(
			List<EEstatusRequerimiento> estatus, int page, int limit) {
		Specification<Analista> specfAnalista = (new AnalistaDAO())
				.consultarCantRequerimientos(estatus);
		List<Analista> analistas = null;
		if (limit > 0)
			analistas = this.analistaRepository.findAll(specfAnalista,
					new PageRequest(page, limit)).getContent();
		else
			analistas = this.analistaRepository.findAll(specfAnalista);
		return analistas;
	}

	public Map<String, Object> consultarAnalistas(Analista analista, int page,
			int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Analista> analistas = null;
		Sort sortAnalista = new Sort(Sort.Direction.ASC, "id");
		Specification<Analista> specfAnalista = (new AnalistaDAO())
				.consultarPersona(analista);

		if (limit > 0) {
			Page<Analista> pageAnalista = this.analistaRepository.findAll(
					specfAnalista, new PageRequest(page, limit, sortAnalista));
			total = Long.valueOf(pageAnalista.getTotalElements()).intValue();
			analistas = pageAnalista.getContent();
		} else {
			analistas = this.analistaRepository.findAll(specfAnalista,
					sortAnalista);
			total = analistas.size();
		}
		parametros.put("total", total);
		parametros.put("analistas", analistas);
		return parametros;
	}

	public Map<String, Object> consultarAnalistasF(Analista analista, String fieldSort,
			Boolean sortDirection, int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Analista> analistas = null;
		Sort sortAnalista = new Sort(getDirection(sortDirection,
				Sort.Direction.ASC), getFieldSort(fieldSort, "id"));
		Specification<Analista> specfAnalista = (new AnalistaDAO())
				.consultarPersona(analista);
		if (limit > 0) {
			Page<Analista> pageAnalista = this.analistaRepository.findAll(
					specfAnalista, new PageRequest(page, limit, sortAnalista));
			total = Long.valueOf(pageAnalista.getTotalElements()).intValue();
			analistas = pageAnalista.getContent();
		} else {
			analistas = this.analistaRepository.findAll(specfAnalista,
					sortAnalista);
			total = analistas.size();
		}
		parametros.put("total", total);
		parametros.put("analistas", analistas);
		return parametros;
	}
	
	public Analista registrarAnalista(Analista analista) {
		return this.analistaRepository.save(analista);
	}

	// Proveedores
	public Map<String, Object> consultarProveedoresSinUsuarios(
			Persona personaF, String fieldSort, Boolean sortDirection,
			int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Proveedor> proveedores = null;
		Sort sortProveedor = new Sort(getDirection(sortDirection,
				Sort.Direction.ASC), getFieldSort(fieldSort, "id"));
		Specification<Proveedor> specfProveedro = (new ProveedorDAO())
				.consultarProveedoresSinUsuarios(personaF);
		if (limit > 0) {
			Page<Proveedor> pageProveedor = this.proveedorRepository
					.findAll(specfProveedro, new PageRequest(page, limit,
							sortProveedor));
			total = Long.valueOf(pageProveedor.getTotalElements()).intValue();
			proveedores = pageProveedor.getContent();
		} else {
			proveedores = this.proveedorRepository.findAll(specfProveedro,
					sortProveedor);
			total = proveedores.size();
		}
		parametros.put("total", total);
		parametros.put("proveedores", proveedores);
		return parametros;
	}

	public Proveedor registrarProveedor(Proveedor proveedor) {
		return this.proveedorRepository.save(proveedor);
	}

	public Map<String, Object> ConsultarProveedoresListaClasificacionRepuesto(
			Persona persona, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, List<Integer> idsClasificacionRepuesto,
			int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Proveedor> proveedores = null;
		Sort sortProveedor = new Sort(getDirection(sortDirection,
				Sort.Direction.ASC), getFieldSort(fieldSort, "id"));
		Specification<Proveedor> specfProveedor = (new ProveedorDAO())
				.consultarProveedoresListaClasificacionRepuesto(persona,
						idRequerimiento, idsClasificacionRepuesto);
		if (limit > 0) {
			Page<Proveedor> pageProveedor = this.proveedorRepository
					.findAll(specfProveedor, new PageRequest(page, limit,
							sortProveedor));
			total = Long.valueOf(pageProveedor.getTotalElements()).intValue();
			proveedores = pageProveedor.getContent();
		} else {
			proveedores = this.proveedorRepository.findAll(specfProveedor,
					sortProveedor);
			total = proveedores.size();
		}
		parametros.put("total", total);
		parametros.put("proveedores", proveedores);
		return parametros;
	}

	public List<Proveedor> consultarProveedoresHaAprobar(
			Requerimiento requerimiento) {
		Specification<Proveedor> specfProveedor = (new ProveedorDAO())
				.consultarProveedoresHaAprobar(requerimiento);
		return proveedorRepository.findAll(specfProveedor);
	}

	public Map<String, Object> consultarProveedores(Proveedor proveedor,
			int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Proveedor> proveedores = null;
		Sort sortProveedor = new Sort(Sort.Direction.ASC, "id");
		Specification<Proveedor> specfProveedor = (new ProveedorDAO())
				.consultarPersona(proveedor);
		if (limit > 0) {
			Page<Proveedor> pageProveedor = this.proveedorRepository
					.findAll(specfProveedor, new PageRequest(page, limit,
							sortProveedor));
			total = Long.valueOf(pageProveedor.getTotalElements()).intValue();
			proveedores = pageProveedor.getContent();
		} else {
			proveedores = this.proveedorRepository.findAll(specfProveedor,
					sortProveedor);
			total = proveedores.size();
		}
		parametros.put("total", total);
		parametros.put("proveedores", proveedores);
		return parametros;
	}

	public Map<String, Object> consultarProveedoresConSolicitudCotizaciones(
			Proveedor proveedor, Integer idRequerimiento, String fieldSort,
			Boolean sortDirection, int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Proveedor> proveedores = null;
		Sort sortProveedor = new Sort(getDirection(sortDirection,
				Sort.Direction.ASC), getFieldSort(fieldSort, "id"));
		Specification<Proveedor> specfProveedor = (new ProveedorDAO())
				.consultarProveedoresConSolicitudCotizaciones(proveedor,
						idRequerimiento);
		if (limit > 0) {
			Page<Proveedor> pageProveedor = this.proveedorRepository
					.findAll(specfProveedor, new PageRequest(page, limit,
							sortProveedor));
			total = Long.valueOf(pageProveedor.getTotalElements()).intValue();
			proveedores = pageProveedor.getContent();
		} else {
			proveedores = this.proveedorRepository.findAll(specfProveedor,
					sortProveedor);
			total = proveedores.size();
		}
		parametros.put("total", total);
		parametros.put("proveedores", proveedores);
		return parametros;
	}

	public Proveedor consultarProveedor(Proveedor proveedor) {

		Sort sortProveedor = new Sort(Sort.Direction.ASC, "id");
		Specification<Proveedor> specfProveedor = (new ProveedorDAO())
				.consultarPersona(proveedor);
		List<Proveedor> proveedores = this.proveedorRepository.findAll(
				specfProveedor, sortProveedor);
		if (proveedores != null && proveedores.size() > 0)
			return proveedores.get(0);
		return null;
	}
	
	//Metodo para consultar el correo Proveedor
	public Boolean consultarCorreoProveedor(String correo) {
		Proveedor proveedor=new Proveedor();

		proveedor=this.proveedorRepository.findByCorreo(correo);

		if(proveedor!=null){
			System.out.println("correo existe es ---- "+proveedor.getCorreo());
			return true;
		}else{
			return false;
		}

	}

	// Clasificacion Repuesto
	public Map<String, Object> consultarClasificacionRepuesto(int page,
			int limit) {
		Map<String, Object> Parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<ClasificacionRepuesto> clasfRepuestos = null;
		if (limit > 0) {
			Page<ClasificacionRepuesto> pageClasfRepuesto = this.clasificacionRepuestoRepository
					.findAll(new PageRequest(page, limit));
			total = Long.valueOf(pageClasfRepuesto.getTotalElements())
					.intValue();
			clasfRepuestos = pageClasfRepuesto.getContent();
		} else {
			clasfRepuestos = this.clasificacionRepuestoRepository.findAll();
			total = clasfRepuestos.size();
		}
		Parametros.put("total", total);
		Parametros.put("clasificacionRepuesto", clasfRepuestos);
		return Parametros;
	}

	public Map<String, Object> consultarClasificacionRepuestoProveedor(
			Integer idProveedor, int page, int limit) {
		Map<String, Object> Parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<ClasificacionRepuesto> clasfRepuestos = null;
		if (limit > 0) {
			Page<ClasificacionRepuesto> pageClasfRepuesto = this.clasificacionRepuestoRepository
					.findByProveedoresId(idProveedor, new PageRequest(page,
							limit));
			total = Long.valueOf(pageClasfRepuesto.getTotalElements())
					.intValue();
			clasfRepuestos = pageClasfRepuesto.getContent();
		} else {
			clasfRepuestos = this.clasificacionRepuestoRepository
					.findByProveedoresId(idProveedor);
			total = clasfRepuestos.size();
		}
		Parametros.put("total", total);
		Parametros.put("clasificacionRepuesto", clasfRepuestos);
		return Parametros;
	}

	// Motor
	public Map<String, Object> consultarMotores(Motor motor, String fieldSort,
			Boolean sortDirection, int page, int limit) {
		Map<String, Object> Parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Motor> motores = null;
		Sort sortMotor = new Sort(getDirection(sortDirection,
				Sort.Direction.ASC), getFieldSort(fieldSort, "idMotor"));
		Specification<Motor> specfMotor = (new MotorDAO())
				.consultarMotores(motor);
		if (limit > 0) {
			Page<Motor> pageMotor = this.motorRepository.findAll(specfMotor,
					new PageRequest(page, limit, sortMotor));
			total = Long.valueOf(pageMotor.getTotalElements()).intValue();
			motores = pageMotor.getContent();
		} else {
			motores = this.motorRepository.findAll(specfMotor, sortMotor);
			total = motores.size();
		}
		Parametros.put("total", total);
		Parametros.put("motores", motores);
		return Parametros;
	}

	// Banco
	public Map<String, Object> consultarBancos(int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Banco> bancos = null;
		if (limit > 0) {
			Page<Banco> pageBanco = this.bancoRepository
					.findAll(new PageRequest(page, limit));
			total = Long.valueOf(pageBanco.getTotalElements()).intValue();
			bancos = pageBanco.getContent();
		} else {
			bancos = this.bancoRepository.findAll();
			total = bancos.size();
		}
		parametros.put("total", total);
		parametros.put("bancos", bancos);
		return parametros;
	}

	// Pais
	public Map<String, Object> consultarPaises(int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Pais> paises = null;
		if (limit > 0) {
			Page<Pais> pagePais = this.paisRepository.findAll(new PageRequest(
					page, limit));
			total = Long.valueOf(pagePais.getTotalElements()).intValue();
			paises = pagePais.getContent();
		} else {
			paises = this.paisRepository.findAll();
			total = paises.size();
		}
		parametros.put("total", total);
		parametros.put("paises", paises);
		return parametros;
	}
	
	//Vehiculos
	@Override
	public Map<String, Object> consultarVehiculos(Vehiculo vehiculo, int page, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Vehiculo> vehiculos = null;
		if (limit > 0) {
			Page<Vehiculo> pageVehiculo = this.vehiculoRepository.findByCliente(vehiculo.getCliente(), new PageRequest(page, limit));
			total = Long.valueOf(pageVehiculo.getTotalElements()).intValue();
			vehiculos = pageVehiculo.getContent();
		} else {
			vehiculos = this.vehiculoRepository.findByCliente(vehiculo.getCliente());
			total = vehiculos.size();
		}
		
		parametros.put("total", total);
		parametros.put("vehiculos", vehiculos);
		return parametros;
	}
	
	@Override
	public Vehiculo registrarVehiculo(Vehiculo vehiculo){
		vehiculo.setEstatus(EEstatusGeneral.ACTIVO);
		return this.vehiculoRepository.save(vehiculo);
	}
	
	//metodo funcional muestrala lista mas no filtra = /
	public Map<String, Object> consultarVehiculosCliente(
			Integer idCliente, int page, int limit) {
		Map<String, Object> Parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Vehiculo> vehiculos = null;
		
		
		
		if (limit > 0) {
			Page<Vehiculo> pageVehiculo = this.vehiculoRepository.findByClienteId(idCliente, new PageRequest(page,
					limit));
			total = Long.valueOf(pageVehiculo.getTotalElements())
					.intValue();
			vehiculos = pageVehiculo.getContent();
		} else {
			vehiculos = this.vehiculoRepository.findByClienteId(idCliente);
			total = vehiculos.size();
		}
		Parametros.put("total", total);
		Parametros.put("vehiculos", vehiculos);
		return Parametros;
	}
	
	/*@Override
	public Map<String, Object> consultarVehiculosF(Vehiculo vehiculo, String fieldSort,
			Boolean sortDirection, int page, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Vehiculo> vehiculos = null;
		
		Sort sortVehiculo = new Sort(getDirection(sortDirection,
				Sort.Direction.ASC), getFieldSort(fieldSort, "id"));
		Specification<Vehiculo> specfVehiculo = (new VehiculoDAO()).
				consultarVehiculos(vehiculo);
		
		if (limit > 0) {
			Page<Vehiculo> pageVehiculo = this.vehiculoRepository.findAll(specfVehiculo,
					new PageRequest(page, limit, sortVehiculo));
			total = Long.valueOf(pageVehiculo.getTotalElements()).intValue();
			vehiculos = pageVehiculo.getContent();
		} else {
			vehiculos = this.vehiculoRepository.findAll(specfVehiculo, sortVehiculo);
			total = vehiculos.size();
		}
		
		if (limit > 0) {
			Page<Vehiculo> pageVehiculo = this.vehiculoRepository.findByCliente(vehiculo.getCliente(), new PageRequest(page, limit));
			total = Long.valueOf(pageVehiculo.getTotalElements()).intValue();
			vehiculos = pageVehiculo.getContent();
		} else {
			vehiculos = this.vehiculoRepository.findByCliente(vehiculo.getCliente());
			total = vehiculos.size();
		}
		
		parametros.put("total", total);
		parametros.put("vehiculos", vehiculos);
		return parametros;
	}*/
	
	// Metodo no funcional 
	/*public Map<String, Object> consultarVehiculosF(Integer idCliente,Vehiculo vehiculo, String fieldSort,
			Boolean sortDirection, int page, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Vehiculo> vehiculos = null;
		Sort sortVehiculo = new Sort(getDirection(sortDirection,
				Sort.Direction.ASC), getFieldSort(fieldSort, "id"));
		Specification<Vehiculo> specfVehiculo = (new VehiculoDAO()).
				consultarVehiculos(vehiculo);
		if (limit > 0) {
			Page<Vehiculo> pageVehiculo = this.vehiculoRepository.findByClienteId(idCliente,specfVehiculo,
					new PageRequest(page, limit,sortVehiculo ));
			total = Long.valueOf(pageVehiculo.getTotalElements()).intValue();
			vehiculos = pageVehiculo.getContent();
		} else {
			vehiculos = this.vehiculoRepository.findByCliente(vehiculo.getCliente());
			total = vehiculos.size();
			total = vehiculos.size();
		}
		parametros.put("total", total);
		parametros.put("vehiculos", vehiculos);
		return parametros;
	}*/

	/** METODOS PROPIOS DE LA CLASE */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T extends PersonaRepository, Y extends Persona> T determinarPersonaDAO(
			Class<Y> clase) {
		T dao = null;
		if (clase != null) {
			if (clase.equals(Analista.class))
				dao = (T) this.analistaRepository;
			else if (clase.equals(Proveedor.class))
				dao = (T) this.proveedorRepository;
			else if (clase.equals(Cliente.class))
				dao = (T) this.clienteRepository;
		}
		return dao;
	}

	public Moneda registrarMoneda(Moneda moneda) {
		return this.monedaRepository.save(moneda);
	}
	
	public HistoricoMoneda registrarHistorico(HistoricoMoneda historico) {
		return this.historicoRepository.save(historico);
	}

}
