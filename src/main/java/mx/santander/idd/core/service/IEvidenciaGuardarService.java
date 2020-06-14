package mx.santander.idd.core.service;

import org.springframework.web.multipart.MultipartFile;
import mx.santander.idd.core.domain.enumeration.Evidencias;
import mx.santander.idd.core.exception.DocumentException;

/**
 * Encargado de guardar las evidencias.
 * @author Mintsait
 *
 */
public interface IEvidenciaGuardarService {
	
	/**
	 * Guarda la evidencia.
	 * @param id Id del expediente.
	 * @param mpFile Archivo multiparte.
	 * @param evidencia Valor de la evidencia.
	 * @throws DocumentException 
	 * @throws ExcepcionIdd 
	 */
	public void guardarEvidencia(String id, MultipartFile mpFile, Evidencias evidencia) throws DocumentException;
	
	/**
	 * Guarda la evidencia.
	 * @param id Id del expediente.
	 * @param bytes Arreglo de bytes que contienen al archivo.
	 * @param evidencia Valor de la evidencia.
	 * @throws DocumentException 
     * @throws ExcepcionIdd 
	 */
	public void guardarEvidencia(String id, byte[] bytes, Evidencias evidencia, String extension) throws DocumentException;
	
}
