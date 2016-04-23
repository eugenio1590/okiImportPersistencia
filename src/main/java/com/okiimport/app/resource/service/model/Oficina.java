package com.okiimport.app.resource.service.model;

public class Oficina {
	
	private String codigo;
	
	private String nombre;

	public Oficina() {
	}
	
	public Oficina(String codigo, String nombre){
		this();
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
