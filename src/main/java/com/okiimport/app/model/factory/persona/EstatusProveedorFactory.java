package com.okiimport.app.model.factory.persona;

public class EstatusProveedorFactory extends EstatusPersonaFactory {
	
	//Enum
	protected static String SOLICITANTE="solicitante";
	
	/**METODOS OVERRIDE*/
	@Override
	protected IEstatusPersona getEstatus(String estatus) {
		if(estatus.equalsIgnoreCase(SOLICITANTE))
			return getEstatusSolicitante();	
		
		return null;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/	
	public static IEstatusPersona getEstatusSolicitante(){
		return new IEstatusPersona(){

			@Override
			public String getValue() {
				return SOLICITANTE;
			}
			
		};
	}

}
