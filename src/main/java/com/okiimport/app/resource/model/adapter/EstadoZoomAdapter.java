package com.okiimport.app.resource.model.adapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.okiimport.app.model.Estado;

public class EstadoZoomAdapter extends TypeAdapter<Estado> {

	@Override
	public void write(JsonWriter out, Estado value) throws IOException {
		// TODO Auto-generated method stub
		//No Necesitado Por Ahora
	}

	@Override
	public Estado read(JsonReader in) throws IOException {
		return new Estado(in.nextString());
	}
	
	

}
