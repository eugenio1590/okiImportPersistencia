package com.okiimport.app.resource.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.xml.bind.DatatypeConverter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.okiimport.app.resource.model.patron.Prototipo;

@MappedSuperclass
public abstract class AbstractEntity implements Prototipo{
	
	protected static final Calendar CALENDAR = GregorianCalendar.getInstance();
	
	@CreatedDate
	@Column(name="fecha_creacion")
	protected Date fechaCreacion;
	
	@LastModifiedDate 
	@Column(name="fecha_ultima_modificacion")
	protected Date fechaUltimaModificacion;
	
	public static String decodificarImagen(byte[] imagen){
		if(imagen!=null && imagen.length>0){
			return "data:image/jpeg;base64,"+DatatypeConverter.printBase64Binary(imagen);
		}
		return null;
	}
	
	public static String decodificarDocumento(byte[] documento){
		if(documento!=null && documento.length>0){
			return "data:application/pdf;base64,"+DatatypeConverter.printBase64Binary(documento);
		}
		return null;
	}
	
	private static Date sumarORestarFecha(Date fecha, int field, int value){
		Calendar calendar = GregorianCalendar.getInstance();
		Date fechaTemp = calendar.getTime();
		if(fecha!=null){
			calendar.setTime(fecha);
			calendar.add(field, value);
			fecha = calendar.getTime();
			calendar.setTime(fechaTemp);
		}
		return fecha;
	}

	public static Date sumarORestarFDia(Date fecha, int dias){
		return sumarORestarFecha(fecha, Calendar.DAY_OF_YEAR, dias);
	}
	
	public static Date sumarORestarFMes(Date fecha, int meses){
		return sumarORestarFecha(fecha, Calendar.MONTH, meses);
	}
	
	public static Date sumarORestarFAnno(Date fecha, int annos){
		return sumarORestarFecha(fecha, Calendar.YEAR, annos);
	}
	
	public static Long diferenciaHoras(Date fecha1, Date fecha2){
		Calendar calendar1 = new GregorianCalendar();
		calendar1.setTime(fecha1);
		Calendar calendar2 = new GregorianCalendar();
		calendar2.setTime(fecha2);
		long milis1 = calendar1.getTimeInMillis();
		long milis2 = calendar2.getTimeInMillis();
		return (milis2 - milis1) / (60 * 60 * 1000);
	}
		
	/**GETTERS Y SETTERS*/
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getFechaUltimaModificacion() {
		return fechaUltimaModificacion;
	}

	public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
		this.fechaUltimaModificacion = fechaUltimaModificacion;
	}
	
	/**EVENTOS*/
	@PrePersist
	public void prePersist(){
		if(this.getFechaCreacion()==null)
			this.setFechaCreacion(new Timestamp(CALENDAR.getTime().getTime()));
	}
	
	@PreUpdate
	public void preUpdate(){
		this.setFechaUltimaModificacion(new Timestamp(CALENDAR.getTime().getTime()));
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	public int compareWithDate(AbstractEntity objectCompare){
		if(this.fechaUltimaModificacion!=null){
			if(objectCompare.fechaUltimaModificacion!=null)
				return this.fechaUltimaModificacion.compareTo(objectCompare.fechaUltimaModificacion);
			else
				return this.fechaUltimaModificacion.compareTo(objectCompare.fechaCreacion);
		}
		else if(objectCompare.fechaUltimaModificacion!=null)
			return this.fechaCreacion.compareTo(objectCompare.fechaUltimaModificacion);
		else
			return this.fechaCreacion.compareTo(objectCompare.fechaCreacion);
		
	}
	
	/**INTERFACE*/
	/**1. Interface Prototipo*/
	@SuppressWarnings("unchecked")
	public <T> T clon(){
		try {
			return (T) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return (T) new Object();
	}
}
