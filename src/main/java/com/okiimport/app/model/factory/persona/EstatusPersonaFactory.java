package com.okiimport.app.model.factory.persona;

import com.okiimport.app.model.Persona;

public abstract class EstatusPersonaFactory {
	
	//ENUM
	protected static String ACTIVO="activo";
	protected static String INACTIVO="inactivo";
	
	//INTERFACE
	public interface IEstatusPersona {	
		String getValue();
	}

	//CONSTRUCTOR
	public EstatusPersonaFactory() {
		// TODO Auto-generated constructor stub
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	/**
	 * Descripcion: permitira obtener el estatus de una persona a la interfaz respectiva
	 * Parametros: @param persona: persona con el estatus en formato String
	 * Retorno:  @return iEstatus: interfaz del estatus de la persona
	 * Nota: Ninguno
	 * */
	public IEstatusPersona getEstatus(Persona persona){
		String estatus = persona.getEstatus();
		IEstatusPersona iEstatus = null;
		try {
			if(estatus.equalsIgnoreCase(ACTIVO))
				iEstatus = getEstatusActivo();
			else if(estatus.equalsIgnoreCase(INACTIVO))
				iEstatus = getEstatusInactivo();
			else
				iEstatus = getEstatus(estatus);
		}
		catch (Exception e){
			e.printStackTrace();
			throw e;
		}
		persona.setiEstatus(iEstatus);
		return iEstatus;
	}
	
	/**METODOS ESTATICOS DE LA CLASE*/
	public static IEstatusPersona getEstatusActivo(){
		return new IEstatusPersona(){

			@Override
			public String getValue() {
				return ACTIVO;
			}
			
		};
	}
	
	public static IEstatusPersona getEstatusInactivo(){
		return new IEstatusPersona(){

			@Override
			public String getValue() {
				return INACTIVO;
			}
			
		};
	}

	/**METODOS ABSTRACTOS DE LA CLASE*/
	/**
	 * Descripcion: obtendra el metodo para calcular la interfaz de estatus respectivo a travez de su string
	 * Parametros: @param estatus: string del estatus de la persona
	 * Retorno: @return iEstatusPersona: interfaz del estatus persona
	 * Nota: Ninguna
	 * */
	protected abstract IEstatusPersona getEstatus(String estatus);
}
