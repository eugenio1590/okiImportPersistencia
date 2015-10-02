package com.okiimport.app.service.mail.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class AbstractMailImpl {
	protected static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	protected static Calendar calendar = GregorianCalendar.getInstance();

	protected void sendMail(Runnable hilo){		
		new Thread(hilo).start();
	}
}
