package com.okiimport.app.service.web.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.Pais;
import com.okiimport.app.resource.service.model.Oficina;
import com.okiimport.app.service.web.SLocalizacion;
import com.okiimport.persistencia.AbstractJpaConfiguration;

public class SLocalizacionImpl implements SLocalizacion {
	
	private static final Gson GSON = new GsonBuilder().create();
	private static final double RadioTierraKm = 6378;//.137;
	
	private GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyDK6oh7fuTOOWIMh9OSJSxDSvJkw7jdrmQ");

	@Override
	public double calcularDistancia(Ciudad ciudadOrigen, Ciudad ciudadDestino) {
		double distancia = 0;
		if(!ciudadOrigen.equals(ciudadDestino)){
			try {
				DistanceMatrix result = DistanceMatrixApi.getDistanceMatrix(context, 
						new String[]{ciudadOrigen.ubicacion(",")}, new String[]{ciudadDestino.ubicacion(",")}).await();
				DistanceMatrixElement element = result.rows[0].elements[0];
				if(element.status == DistanceMatrixElementStatus.OK)
					distancia = result.rows[0].elements[0].distance.inMeters;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return distancia;
	}
	
	@Override
	public double calcularDistancia(Pais paisOrigen, Pais paisDestino) {
		double result = 0;
		if(!paisOrigen.equals(paisDestino)){
			try {
				GeocodingResult[] resultsOrigen =  GeocodingApi.geocode(context, paisOrigen.getNombre()).await();
				GeocodingResult[] resultsDestino =  GeocodingApi.geocode(context, paisDestino.getNombre()).await();
				if(resultsOrigen.length > 0 && resultsDestino.length > 0)
					result = formulaHaversine(resultsOrigen[0].geometry.location, resultsDestino[0].geometry.location);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public double calcularFleteZoomConPesoYDistancia(Compra compra, Ciudad ciudadDestino) {
		double flete = 0;
		
		Ciudad ciudadDestinatario = obtenerCiudadZoomDestinatario(ciudadDestino);
		if(ciudadDestinatario!=null){
			System.out.println("Ciudad Encontrada "+ciudadDestinatario.getCodigo());
			Oficina oficina = obtenerOficinaARetirarZoom(ciudadDestinatario);
			if(oficina!=null){
				System.out.println("Oficina Encontrada");
				HashMap<String, String> parametros = new HashMap<String, String>();
				parametros.put("tipo_tarifa", "2");
				parametros.put("modalidad_tarifa", "2");
				parametros.put("ciudad_remitente", "44");
				parametros.put("ciudad_destinatartio", ciudadDestinatario.getCodigo());
				parametros.put("oficina_retirar", oficina.getCodigo());
				parametros.put("cantidad_piezas", "8"); //Por Ahora estatica
				parametros.put("peso", "20"); //Por Ahora estatica
			}
			else
				System.out.println("Oficina No Encontrada");
		}
		else
			System.out.println("Ciudad No Encontrada");
		
		return flete;
	}
	
	@Override
	public Ciudad obtenerCiudadZoomDestinatario(Ciudad ciudadDestino){
		try {
			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("filtro", "nacional");
			HttpResponse response = requestPostZoom("/getCiudades", GSON.toJson(parametros));
			if(response!=null && response.getStatusLine().getStatusCode() == 200){ //Respuesta del server correcta
				Reader reader = new InputStreamReader(response.getEntity().getContent());
				Type listType = new TypeToken<ArrayList<Ciudad>>() {}.getType();
				List<Ciudad> ciudades = GSON.fromJson(reader, listType);
				
				if(ciudades!=null){
					for(Ciudad ciudad : ciudades){
						if (ciudad.isIgualEstado(ciudadDestino.getEstado()) && ciudad.equals(ciudadDestino))
							return ciudad;
					}
				}
			}
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Oficina obtenerOficinaARetirarZoom(Ciudad ciudadZoomRemitente){
		try {
			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("codigo_ciudad_destino", ciudadZoomRemitente.getCodigo());
			parametros.put("tipo_tarifa", "2");
			HttpResponse response = requestPostZoom("/getOficinas", GSON.toJson(parametros));
			if(response!=null && response.getStatusLine().getStatusCode() == 200){ //Respuesta del server correcta
				Reader reader = new InputStreamReader(response.getEntity().getContent());
				Type listType = new TypeToken<HashMap<String, String>>() {}.getType();
				HashMap<String, String> oficinas = GSON.fromJson(reader, listType);
				if(oficinas!=null && !oficinas.isEmpty()){
					//Se tomara una Oficina Aleatoria.
					int posc = (int)(Math.random()*oficinas.size());
					String key = new ArrayList<String>(oficinas.keySet()).get(posc);
					if(!key.equalsIgnoreCase("errormessage"))
						return new Oficina(key, oficinas.get(key));
				}
				else
					System.out.println("No Existen Oficinas");
			}
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	/**
	 * Descripcion: permitira calcular la diferencia entre dos coordenadas usando la formula de Haversine,
	 * esta distancia tomara en cuenta el radio de la tierra en km, dando la respuesta en las mismas unidades.
	 * Parametros: 
	 * @param locationOrigen: coordenadas del punto de origen
	 * @param locationDestino: coordenadas del punto de destino
	 * Retorno: @return valor double: distancia en km entre las coordenadas
	 * Nota: Ninguna
	 * */
	private double formulaHaversine(LatLng locationOrigen, LatLng locationDestino) {
		double difLatitud = Math.toRadians(locationDestino.lat - locationOrigen.lat);
		double difLongitud = Math.toRadians(locationDestino.lng - locationOrigen.lng);
		double latOrigen = Math.toRadians(locationOrigen.lat);
		double latDestino = Math.toRadians(locationDestino.lat);
		double a = Math.pow(Math.sin(difLatitud/2), 2)
				+ Math.pow(Math.sin(difLongitud/2), 2) * Math.cos(latOrigen) * Math.cos(latDestino);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		return RadioTierraKm * c;
	}
	
	private HttpResponse requestPostZoom(String service, String entity){
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(AbstractJpaConfiguration.ZOOM_URL+service);
			if(entity!=null){
				request.addHeader("Content-Type", "application/json");
				StringEntity entityRequest = new StringEntity(entity);
				request.setEntity(entityRequest);
			}
			return client.execute(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
