package mx.santander.idd.core.domain.enumeration;

import mx.santander.idd.core.domain.utils.LevelsConstantes;

/**
 * The Enum ErrorEnum.
 * Esta clase permite la enumeracion de diferentes mensajes de excepcion
 * utilizados en los cuerpos de respuesta HTTP arrojados por el servicio
 * 
 * Es posible agregar nuevos mensajes personalizados para permitir que
 * el servicio sea mas explicito, recordando siempre que es importante
 * evitar arrojar informacion sensible
 * @author MINSAIT
 */
public enum ErrorEnum {

	EXC_GENERICO("EXC.000", "Error generico", "Error generico de servidor", LevelsConstantes.ERROR, ""),
	EXC_ERROR_DE_CONEXION("EXC.122", "Error de conexión", LevelsConstantes.ERROR, "Erro de conexión", "");
	
	/** variable code que encapsula el codigo del error */
	private final String code;

	/** variable message que encapsula el mensaje del error */
	private final String message;

	/** variable description que encapsula la descripcion del error*/
	private final String description;

	/** variable level que encapsula el nivel del error */
	private final String level;

	/** variable moreInfo que encapsula la informacion del error*/
	private final String moreInfo;

	/**
	 * Constructor que define el enum del error
	 * @param code codigo del error
	 * @param message mensaje del error
	 * @param description descripcion del error
	 * @param level nivel de error
	 * @param moreInfo informacion del error
	 */
	ErrorEnum(final String code, final String message, final String description, final String level,
			final String moreInfo) {
		this.code = code;
		this.message = message;
		this.description = description;
		this.level = level;
		this.moreInfo = moreInfo;
	}

	/**
	 * Metodo que retorna el codigo de error
	 * @return el codigo de error
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Metodo que retorna el mensaje de error
	 * @return el mensaje de error
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Metodo que retorna la descripcion del error
	 * @return al descripcion del error
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Metodo que retorna el nivel de error
	 * @return el nivel de error
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Metodo que retorna la informacion de error
	 * @return la informacion  del error
	 */
	public String getMoreInfo() {
		return moreInfo;
	}

}
