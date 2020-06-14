package mx.santander.idd.core.domain.enumeration;

/**
 * @author Mintsait
 * Esta clase permite la enumeracion de los tipos de evidencia
 */
public enum Evidencias {
	DOCUMENTO_ANVERSO,
	DOCUMENTO_ANVERSO_GUARDADO,
	DOCUMENTO_ANVERSO_CUT,
	DOCUMENTO_ANVERSO_PROCESADO,
	DOCUMENTO_ANVERSO_FLASH,
	DOCUMENTO_ANVERSO_FLASH_CUT,
	DOCUMENTO_REVERSO,
	DOCUMENTO_REVERSO_GUARDADO,
	DOCUMENTO_REVERSO_CUT,
	DOCUMENTO_REVERSO_PROCESANDO,
	DOCUMENTO_REVERSO_PROCESADO,
	BIOMETRIA_SELFIE,
	BIOMETRIA_SELFIE_GUARDADO,
	SELFIE,
	BIOMETRIA_VIDEO,
	BIOMETRIA_VIDEO_GUARDADO,
	BIOMETRIA_VIDEO_PROCESADO,
	DOCUMENTO_FOTO, 
	DOCUMENTO_FIRMA,
	OCR,
	SCORES_FINALES, 
	DOCUMENTACION_PDF, 
	DATOS_TRAMITACION, FECHAS;

	/**
	 * Metodo que retorna si es valido el nombre de la evidencia
	 * @param evidencia tipo de evidencia
	 * @return regresa valor booleano para la validacion
	 */
	public static boolean isEvidencia(String evidencia) {
		for (Evidencias e : values())
			if (evidencia.equals(e.toString()))
				return true;
		return false;
	}

	/**
	 * Metodo que busca el tipo de evidencia
	 * @param evidencia tipo de eviencia
	 * @return regresa si el tipo de evidencia coincide con el recibido 
	 */
	public static Evidencias findEvidencia(String evidencia) {
		for (Evidencias e : values())
			if (evidencia.equals(e.toString()))
				return e;
		return null;
	}

	/**
	 * Metodo que valida si el archivo es imagen
	 * @param evidencia tipo de eviencia
	 * @return regresa valor booleano de la validacion
	 */
	public static boolean isImagen(Evidencias evidencia) {
		switch (evidencia) {
			case BIOMETRIA_SELFIE:
			case DOCUMENTO_ANVERSO:
			case DOCUMENTO_ANVERSO_CUT:
			case DOCUMENTO_ANVERSO_FLASH:
			case DOCUMENTO_ANVERSO_FLASH_CUT:
			case DOCUMENTO_REVERSO:
			case DOCUMENTO_REVERSO_CUT:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Metodo que valida si el archivo es video
	 * @param evidencia tipo de eviencia
	 * @return regresa valor booleano de la validacion
	 */
	public boolean isImagen() {
		return isImagen(this);
	}

	/**
	 * Metodo que returna si el archivo es video
	 * @return metodo con parametros si el arhivo es video
	 */
	public static boolean isVideo(Evidencias evidencia) {
		switch (evidencia) {
			case BIOMETRIA_VIDEO:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Metodo que regresa la validacion si el video es de datos_tramitacion o fechas
	 * @param evidencia the value to be used
	 * @return regresa el valor booleano de la validacion
	 */
	public boolean isVideo() {
		return isVideo(this);
	}



	/**
	 * Metodo para regresar si la evidencia es datos o fechas
	 * @return regresa el metodo con parametros 
	 */
	public static boolean isGeneratedByArchivadoProcess(Evidencias evidencia) {
		switch (evidencia) {
			case DATOS_TRAMITACION:
			case FECHAS:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Metodo para regresar si la evidencia es datos o fechas
	 * @return regresa el metodo con parametros 
	 */
	public boolean isGeneratedByArchivadoProcess() {
		return isGeneratedByArchivadoProcess(this);
	}

	/**
	 * Metodo que valida si el archivo es PDF
	 * @param evidencia tipo de evidenci
	 * @return regresa valor booleana si el arhivo es pdf
	 */
	public static boolean isPdf(Evidencias evidencia) {
		switch (evidencia) {
			case DOCUMENTACION_PDF:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Metodo que retorna la validacion si el archivo es PDF
	 * @return retorna la validacion si es PDF
	 */
	public boolean isPdf() {
		return isPdf(this);
	}
}
