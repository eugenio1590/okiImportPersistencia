package com.okiimport.app.resource.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.okiimport.app.service.mail.MailService;

public abstract class AbstractServiceImpl {
	
	//Atributos
	protected Calendar calendar = GregorianCalendar.getInstance();

	@Autowired
	protected MailService mailService;

	public AbstractServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/**SETTERS Y GETTERS*/
	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
	/**SUMA Y RESTAS PARA FECHAS*/
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
	
	/**DIFERENCIA DE HORAS*/
	public static Long diferenciaHoras(Date fecha1, Date fecha2){
		Calendar calendar1 = GregorianCalendar.getInstance();
		calendar1.setTime(fecha1);
		Calendar calendar2 = GregorianCalendar.getInstance();
		calendar2.setTime(fecha2);
		long milis1 = calendar1.getTimeInMillis();
		long milis2 = calendar2.getTimeInMillis();
		return (milis2 - milis1) / (60 * 60 * 1000);
	}

}
