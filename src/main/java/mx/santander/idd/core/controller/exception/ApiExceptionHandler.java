	package mx.santander.idd.core.controller.exception;

import org.owasp.encoder.Encode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import mx.santander.idd.core.domain.enumeration.ErrorEnum;
import mx.santander.idd.core.domain.model.DefaultErrorDTO;
import mx.santander.idd.core.domain.model.DefaultErrorListDTO;
import mx.santander.idd.core.exception.DocumentException;

/**
 * @author MINSAIT Esta clase se encarga de servir como apoyo al controller,
 *         manejando de manera desacoplada las excepciones esperadas en la
 *         aplicacion, y manejando el catalogo de errores con ayuda de un
 *         enumerador personalizado. Tambien tiene un manejo de errores
 *         genericos.
 */
@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {
	
	/**
	 * Manejo de excepciones generales de negocio
	 * @param pe Excepcion de tipo DocumentException
	 * @return La entidad de respuesta que maneja el error como objeto
	 */
	@ExceptionHandler(value = { DocumentException.class })
	public ResponseEntity<DefaultErrorListDTO> handleApiRequestException(DocumentException pe) {		
		String[] mesage = pe.getMessage().split("\\:");
	     String mensaje=Encode.forJava("Excepcion de logica de negocio");	
			log.error(mensaje,pe);
		return new ResponseEntity<>(
				new DefaultErrorListDTO(new DefaultErrorDTO(pe.getHttpError(), mesage[0], pe.getCause().toString())),
				pe.getHttpCode());		
	}	
	
	
	/**
	 * Manejo de excepcion generica
	 * 
	 * @param ex Excepcion generica de tipo Exception
	 * @return La entidad de respuesta que maneja el error como objeto
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<DefaultErrorListDTO> handleGenericException(Exception ex) {
     String mensaje=Encode.forJava("Excepcion generica");	
		log.error(mensaje,ex);
		return new ResponseEntity<>(
				new DefaultErrorListDTO(new DefaultErrorDTO(ErrorEnum.EXC_GENERICO)), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
