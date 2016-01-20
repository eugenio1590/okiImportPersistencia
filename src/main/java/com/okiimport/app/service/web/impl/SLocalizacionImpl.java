package com.okiimport.app.service.web.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.okiimport.app.model.Pais;
import com.okiimport.app.service.web.SLocalizacion;

public class SLocalizacionImpl implements SLocalizacion {
	
	private GeoApiContext context = null;

	@Override
	public String calcularDistanciaEntrePaises(Pais paisOrigen, Pais paisDestino) {
		// TODO Auto-generated method stub
		String result = null;
		try {
			context = new GeoApiContext().setApiKey("AIzaSyDK6oh7fuTOOWIMh9OSJSxDSvJkw7jdrmQ");
			GeocodingResult[] results =  GeocodingApi.geocode(context, paisOrigen.getNombre()).await();
			System.out.println(results[0].formattedAddress);
			result = results[0].formattedAddress;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
