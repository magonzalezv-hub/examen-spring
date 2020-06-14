package mx.santander.idd.core.service;

import org.springframework.web.multipart.MultipartFile;

import mx.santander.idd.core.exception.DocumentException;

/**
 * Encargada de subir los distintos tipos de documentos del expediente.
 *
 * @author Mintsait
 * 
 */
public interface IDocumentService {

	/**
	 * Sube el documento anverso a la cola.
	 * @param id ID del expediente.
	 * @param idSesion ID de la sesión.
	 * @param archivo Archivo a subir.
	 * @throws DocumentException 
	 */
	public Boolean subirAnverso(String id, String idSesion, MultipartFile archivo) throws DocumentException;
	
	/**
	 * Sube el documento anverso flash a la cola.
	 * @param id ID del expediente.
	 * @param idSesion ID de la sesión.
	 * @param archivo Archivo a subir.
	 */
	public Boolean subirAnversoFlash(String id, String idSesion, MultipartFile archivo) throws DocumentException ;
	
	/**
	 * Sube el documento reverso a la cola.
	 * @param id ID del expediente.
	 * @param idSesion ID de la sesión.
	 * @param archivo Archivo a subir.
	 */
	public Boolean subirReverso(String idExpediente, String idSesion, MultipartFile archivo) throws DocumentException;
	 
	
	
	
}
