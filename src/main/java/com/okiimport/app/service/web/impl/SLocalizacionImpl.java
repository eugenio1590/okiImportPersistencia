package com.okiimport.app.service.web.impl;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.Pais;
import com.okiimport.app.service.web.SLocalizacion;

public class SLocalizacionImpl implements SLocalizacion {
	
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
}
