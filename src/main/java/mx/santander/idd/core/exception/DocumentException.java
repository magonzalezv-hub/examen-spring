package mx.santander.idd.core.exception;

import org.springframework.http.HttpStatus;

import mx.santander.idd.core.domain.enumeration.ErrorEnum;

/**
 * @author Minsait
 * Clase de excepciones que genera los diferentes tipos de https codes
 */
public class DocumentException extends Exception{
	/**
	 * serialVersionUID Variable para serializar la clase. 
	 */
	private static final long serialVersionUID = 1L;

	
	/** La Constante ERROR_DE_SERVIDOR. Obtiene el mensaje de error correspodiente */
	public static final String MSG_ERROR_EN_SERVIDOR = "ERRORES_DEL_SERVIDOR";
	/** La Constante ERRORES_DEL_CLIENTE. Obtiene el mensaje de error correspodiente */
	public static final String MSG_ERROR_EN_CLIENTE = "ERRORES_DEL_CLIENTE";
	/** La Constante MSG_ERROR_DE_CONEXION. Obtiene el mensaje de error correspodiente */
	public static final String MSG_ERROR_DE_CONEXION = "MSG_ERROR_DE_CONEXION";
	
	
	/**
	 * Constructor que recibe el mensaje de error
	 * @param msg Mensaje del la excepción
	 * */
	public DocumentException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructor que recibe una causa
	 * @param cause excepción lanzable
	 * */
	public DocumentException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor que recibe el mensaje de error y causa
	 * @param msg mensaje
	 * @param cause excepción lanzable
	 * */
	public DocumentException(String msg, Throwable cause) {		
		super(msg, cause);
	}

	/**
	 * Método para devolver el código http
	 * @return http estatus
	 * */
    public HttpStatus getHttpCode() {
    	HttpStatus httpstatus;
		switch(getMessage().split("\\|")[0]) {
			
			case MSG_ERROR_EN_CLIENTE:
				httpstatus =   HttpStatus.NOT_FOUND;
				break;
			case MSG_ERROR_EN_SERVIDOR:
				httpstatus =   HttpStatus.INTERNAL_SERVER_ERROR;
				break;
			case MSG_ERROR_DE_CONEXION:
				httpstatus = HttpStatus.SERVICE_UNAVAILABLE;
				break;
			default:
				httpstatus =   HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return httpstatus;
	}
    

    /**
	 * Método para devolver el mensaje de la excepción
	 * @return error mensaje.
	 * */
    public ErrorEnum getHttpError() {
    	ErrorEnum error;
		switch(getMessage().split("\\|")[0]) {
			case MSG_ERROR_DE_CONEXION:
				error = ErrorEnum.EXC_ERROR_DE_CONEXION;
				break;
			
			default:				
				error = ErrorEnum.EXC_GENERICO;
		}
		return error;
	}
}