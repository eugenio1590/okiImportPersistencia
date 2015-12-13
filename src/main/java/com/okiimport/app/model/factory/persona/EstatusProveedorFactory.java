package com.okiimport.app.model.factory.persona;

public class EstatusProveedorFactory extends EstatusPersonaFactory {
	
	//Enum
	protected static String SOLICITANTE="solicitante";
	protected static String ELIMINADO="eliminado";
	
	/**METODOS OVERRIDE*/
	@Override
	protected IEstatusPersona getEstatus(String estatus) {
		if(estatus.equalsIgnoreCase(SOLICITANTE))
			return getEstatusSolicitante();	
		
		if(estatus.equalsIgnoreCase(ELIMINADO))
			return getEstatusEliminado();
		
		return null;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	public static IEstatusPersona getEstatusEliminado() {
		// TODO Auto-generated method stub
		return new IEstatusPersona(){

			@Override
			public String getValue() {
				return ELIMINADO;
			}
			
		};
	}

		
	public static IEstatusPersona getEstatusSolicitante(){
		return new IEstatusPersona(){

			@Override
			public String getValue() {
				return SOLICITANTE;
			}
			
		};
	}

}
