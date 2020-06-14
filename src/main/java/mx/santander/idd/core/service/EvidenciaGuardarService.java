package mx.santander.idd.core.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import mx.santander.idd.core.domain.enumeration.Evidencias;
import mx.santander.idd.core.exception.DocumentException;

/*import mx.santander.pid.logadapter.core.types.service.ILogAdapterAuditService;
import mx.santander.pid.logadapter.exception.LogAdapterException;*/


/**
 * Encargado de guardar las evidencias.
 * @author Mintsait
 *
 */
@Slf4j
@Service
public class EvidenciaGuardarService implements IEvidenciaGuardarService{

	@Value("${ms.cipher.baseURL}")
	private String cipherEndPoint;

	@Value("${ms.cipher.contextPath}")
	private String cipherContext;
	
	@Value("${ms.cipher.uploadPath}")
	private String cipherUploadPath;
	
	 /**Dependencia paraguardar mensajes de auditoria*/
	/*
	 * @Autowired //private MockPistas auditService; private ILogAdapterAuditService
	 * auditService;
	 */
	
	/**
	 * Guarda la evidencia.
	 * @param id Id del expediente.
	 * @param mpFile Archivo multiparte.
	 * @param evidencia Valor de la evidencia.
	 * @throws DocumentException 
	 * @throws DocumentException 
	 */
	public void guardarEvidencia(String id, MultipartFile mpFile, Evidencias evidencia) throws DocumentException {
		
		/*
		 * try { auditService.audit("GuardarEvidencia: "+
		 * id+" Archivo: "+mpFile.getOriginalFilename()+" Tamanio: "+mpFile.getBytes().
		 * length, "GuardaEvidencia", "Guarda: "+mpFile.getOriginalFilename());
		 * }catch(LogAdapterException | IOException e){
		 * log.error("Error en ejecucion de auditoria", e); }
		 */
		try {
			Optional<String> oextension = Optional.ofNullable(mpFile.getOriginalFilename()).filter(f -> f.contains(".")).map(f -> f.substring(mpFile.getOriginalFilename().lastIndexOf(".") + 1));
			String extension = null;
			if(oextension.isPresent()) {
				extension = oextension.get();
			}
			guardarEvidencia(id, mpFile.getBytes(), evidencia, extension);
		} catch (IOException | DocumentException e) {
			throw new DocumentException(e.getMessage(),e);
		}
	}

	/**
	 * Guarda la evidencia.
	 * @param id Id del expediente.
	 * @param bytes Arreglo de bytes que contienen al archivo.
	 * @param evidencia Valor de la evidencia.
     * @throws DocumentException 
	 */
	public void guardarEvidencia(String id, byte[] bytes, Evidencias evidencia, String extension) throws DocumentException {
		URI url=null;
		
		CloseableHttpResponse response = null;
		try {
			url = new URI(cipherEndPoint+cipherContext+cipherUploadPath);
			
			String fileName = id + "_" + evidencia.name();
			if (extension != null) {
				fileName = fileName + "." + extension;
			}
			
			HttpPost post = new HttpPost(url);
			ByteArrayBody body = new ByteArrayBody(bytes, fileName);
			// 
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addPart("file", body);

			HttpEntity entity = builder.build();
			//
			post.setEntity(entity);
			CloseableHttpClient client = HttpClientBuilder.create()
			          .build();
			response = client.execute(post);


		}catch (IOException | URISyntaxException  e) {
			throw new DocumentException(DocumentException.MSG_ERROR_DE_CONEXION +  ":url:"+ url +e.getMessage(), e);
		}finally {
			if (response!=null)
				try {
					response.close();
				} catch (IOException e) {
					log.error("EvidenciaGuardarService:guardarEvidencia:Error al cerrar response", e);
				}
		}
		
		if(response != null) {
			if(HttpStatus.valueOf(response.getStatusLine().getStatusCode()).is4xxClientError()) {
				throw new DocumentException(DocumentException.MSG_ERROR_EN_CLIENTE + ":" + url +" estatus: " +  response.getStatusLine().getStatusCode());
			}
			if(HttpStatus.valueOf(response.getStatusLine().getStatusCode()).is5xxServerError()){
				throw new DocumentException(DocumentException.MSG_ERROR_EN_SERVIDOR + ":" + url +" estatus: " +  response.getStatusLine().getStatusCode());
		
			}

		}

	}
}
