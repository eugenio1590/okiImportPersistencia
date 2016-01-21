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
	
	private static final Float RadioTierraKm = new Float(6378.0);
	
	private GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyDK6oh7fuTOOWIMh9OSJSxDSvJkw7jdrmQ");

	@Override
	public long calcularDistancia(Ciudad ciudadOrigen, Ciudad ciudadDestino) {
		// TODO Auto-generated method stub
		long distancia = 0;
		if(!ciudadOrigen.equals(ciudadDestino)){
			try {
				DistanceMatrix result = DistanceMatrixApi.getDistanceMatrix(context, 
						new String[]{ciudadOrigen.ubicacion(",")}, new String[]{ciudadDestino.ubicacion(",")}).await();
				DistanceMatrixElement element = result.rows[0].elements[0];
				if(element.status == DistanceMatrixElementStatus.OK)
					distancia = result.rows[0].elements[0].distance.inMeters;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return distancia;
	}
	
	@Override
	public long calcularDistancia(Pais paisOrigen, Pais paisDestino) {
		// TODO Auto-generated method stub
		long result = 0;
		try {
			GeocodingResult[] resultsOrigen =  GeocodingApi.geocode(context, paisOrigen.getNombre()).await();
			GeocodingResult[] resultsDestino =  GeocodingApi.geocode(context, paisDestino.getNombre()).await();
			result = formulaHaversine(resultsOrigen[0].geometry.location, resultsDestino[0].geometry.location);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	private long formulaHaversine(LatLng locationOrigen, LatLng locationDestino) {
		// TODO Auto-generated method stub
		return 0;
	}
}
