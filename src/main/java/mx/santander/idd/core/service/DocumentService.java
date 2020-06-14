package mx.santander.idd.core.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;
import mx.santander.idd.core.domain.enumeration.Evidencias;
import mx.santander.idd.core.domain.model.ExpedienteDTO;
import mx.santander.idd.core.mq.service.ProducerDossierSenderService;
import mx.santander.idd.core.exception.DocumentException;

/*import mx.santander.pid.logadapter.core.types.service.ILogAdapterAuditService;
import mx.santander.pid.logadapter.exception.LogAdapterException;*/


/**
 * Encargada de subir los distintos tipos de documentos del expediente.
 * @author Mintsait
 *
 */
@Service
@Slf4j
public class DocumentService implements IDocumentService {

	@Autowired private EvidenciaGuardarService evidenciaService;
	
	@Autowired
	private RestTemplate clienteRest;

	/**Dependencia para la clase ProducerDossierSenderService*/
	@Autowired
	private ProducerDossierSenderService producerDossierSender;
	/**Enpoint para base URL dossier*/
	@Value("${ms.dossier.baseURL}")
	private String dossierBaseURL;
	/**Enpoint para dossier*/
	@Value("${ms.dossier.basePath}")
	private String dossierBasePATH;
	/**enpoint para documentDossier*/
	@Value("${ms.dossier.documentPATH}")
	private String dossierDocumentPATH;
	/**enpoint para guardar el documento*/
	@Value("${ms.dossier.document.actionPATH}")
	private String dossierDocumentActionPATH;
	
	 /**Dependencia paraguardar mensajes de auditoria*/
	/*
	 * @Autowired //private MockPistas auditService; private ILogAdapterAuditService
	 * auditService;
	 */

	/**
	 * Sube el documento anverso a la cola.
	 * @param id ID del expediente.
	 * @param idSesion ID de la sesión.
	 * @param archivo Archivo a subir.
	 * @throws DocumentException. 
	 */
	@Override
	public Boolean subirAnverso(String id, String idSesion, MultipartFile archivo) throws DocumentException {
		return uploadDocument(id, idSesion, archivo,  Evidencias.DOCUMENTO_ANVERSO);
	}

	
	/**
	 * Sube el documento anverso flash a la cola.
	 * @param id ID del expediente.
	 * @param idSesion ID de la sesión.
	 * @param archivo Archivo a subir.
	 * @throws DocumentException. 
	 */
	@Override
	public Boolean subirAnversoFlash(String id, String idSesion, MultipartFile archivo) throws DocumentException {
		return uploadDocument(id, idSesion, archivo,  Evidencias.DOCUMENTO_ANVERSO_FLASH);
	}

	
	/**
	 * Sube el documento reverso a la cola.
	 * @param id ID del expediente.
	 * @param idSesion ID de la sesión.
	 * @param archivo Archivo a subir.
	 * @throws DocumentException. 
	 */
	@Override
	public Boolean subirReverso(String idExpediente, String idSesion, MultipartFile archivo) throws DocumentException {
		return uploadDocument(idExpediente, idSesion, archivo, Evidencias.DOCUMENTO_REVERSO);
	}

	
	
	/**
	 * Sube un documento a la cola.
	 * @param idExpediente ID del expediente.
	 * @param idSesion ID de la sesión.
	 * @param archivo Archivo a subir
	 * @param evidencia Valor de la evidencia.
	 * @throws DocumentException.
	 * @return verdadero si la subida fue exitosa.
	 */
	private Boolean uploadDocument(String idExpediente, String idSesion, MultipartFile archivo, Evidencias evidencia) throws DocumentException {
		Boolean uploadSuccessful=false;
		
		try {
			//if(checkEstatusForDocument(idExpediente, idSesion, evidencia)) {
				
				evidenciaService.guardarEvidencia(idExpediente, archivo, evidencia);
				if(Evidencias.DOCUMENTO_ANVERSO.equals(evidencia)) {
					updateEstatusForDocument(idExpediente, idSesion, Evidencias.DOCUMENTO_ANVERSO_GUARDADO);
					creaPista("UpdDoc/idExpediente: "+idExpediente, "UpdDocAnv","actualiza status del doc: "+ evidencia);
					
				}else if(Evidencias.DOCUMENTO_REVERSO.equals(evidencia)) {
					updateEstatusForDocument(idExpediente, idSesion, Evidencias.DOCUMENTO_REVERSO_GUARDADO);
					creaPista("UpdDoc/idExpediente: "+idExpediente, "UpdDocRev","actualiza status del doc: "+ evidencia);
					
				}
				//Obtiene la extensión del archivo para enviarla a la cola de mensajes
				Optional<String> oextension = Optional.ofNullable(archivo.getOriginalFilename()).filter(f -> f.contains(".")).map(f -> f.substring(archivo.getOriginalFilename().lastIndexOf(".") + 1));
				String extension = null;
				if(oextension.isPresent()) {
					extension = oextension.get();
				}
				producerDossierSender.send(new ExpedienteDTO(idExpediente, evidencia.toString(), extension, idSesion));
				/*
				if (Evidencias.DOCUMENTO_REVERSO.equals(evidencia)) {
					producerDossierSender.send(new ExpedienteDTO(idExpediente, Evidencias.DOCUMENTO_REVERSO.toString(), extension, idSesion));
				}
				*/
				uploadSuccessful=true;
			//}
		}catch(Exception ex){
			throw new DocumentException(ex.getMessage(), ex);
		} /*
			 * catch(DocumentException ex){ throw new DocumentException(ex.getMessage(),
			 * ex); }
			 */
		
		return uploadSuccessful;
	}

	

	
	/**
	 * Revisa si el microservicio dossier está listo.
	 * @param idExpediente ID del expediente.
	 * @param idSesion ID de la sesión.
	 * @param evidencia Valor de la evidencia.
	 * @return verdadero si el microservicio dossier está listo.
	 * @throws DocumentException 
	 */
	private Boolean checkEstatusForDocument(String idExpediente, String idSesion, Evidencias evidencia) throws DocumentException {
		ResponseEntity<Boolean> responseEntity=null;
		String url = null;
		Boolean dossierReady=false;
		
		try {
			url = dossierBaseURL+dossierBasePATH+dossierDocumentActionPATH;
		
			MultiValueMap<String, String> headers= new LinkedMultiValueMap<>();
			headers.add("Content-type", MediaType.TEXT_PLAIN.toString());
			HttpEntity<String> requestEntity = new HttpEntity<String>("y", headers);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
			        .queryParam("actionType", evidencia.toString())
			        .queryParam("idExpediente", idExpediente)
			        .queryParam("idSesion", idSesion);
		
			responseEntity=clienteRest.exchange(builder.build().encode().toString(), HttpMethod.GET, requestEntity, Boolean.class);
			
			/*
			 * try {
			 * auditService.audit("expediente: "+idExpediente+" llama a :"+url+" response:"
			 * +responseEntity.getStatusCode(), "CheckStatusDoc",
			 * "Valida status del doc llamando a Dossier"); } catch (LogAdapterException e)
			 * { log.error("Error en ejecucion de auditoria", e); }
			 */

		} catch(HttpClientErrorException | HttpServerErrorException ex) {																		
			if(HttpStatus.valueOf(ex.getRawStatusCode() ).is4xxClientError()) {
				throw new DocumentException(DocumentException.MSG_ERROR_EN_CLIENTE + ":" + url + "/" + evidencia.toString() + "&" + idExpediente + "| estatus: " + ex.getRawStatusCode(), ex);
			}
			if(HttpStatus.valueOf(ex.getRawStatusCode() ).is5xxServerError()){
				throw new DocumentException(DocumentException.MSG_ERROR_EN_SERVIDOR + ":" + url + "/" + evidencia.toString() + "&" + idExpediente + "| estatus: " + ex.getRawStatusCode(), ex);
		
			}										
		} catch(RestClientException ex) {
			// Cuando el servicio no esta disponible
			throw new DocumentException(DocumentException.MSG_ERROR_DE_CONEXION +  ":" + ex.getMessage(), ex);
		}
		
		if(responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
			dossierReady=responseEntity.getBody();
		}
		return dossierReady;
	}


	
	/**
	 * Revisa si el microservicio dossier está actualizado.
	 * @param idExpediente ID del expediente.
	 * @param idSesion ID de la sesión.
	 * @param evidencia Valor de la evidencia.
	 * @throws DocumentException 
	 * @return verdadero si el microservicio dossier está actualizado.
	 */
	private Boolean updateEstatusForDocument(String idExpediente, String idSesion, Evidencias evidencia)throws DocumentException  {
		ResponseEntity<String> responseEntity=null;
		String url = null;
		Boolean dossierUpdated=false;
		
		try {
			url = dossierBaseURL+dossierBasePATH+dossierDocumentPATH;
		
			MultiValueMap<String, String> headers= new LinkedMultiValueMap<>();
			headers.add("Content-type", MediaType.APPLICATION_JSON.toString());
			HttpEntity<String> requestEntity = new HttpEntity<String>("y", headers);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
			        .queryParam("actionType", evidencia.toString())
			        .queryParam("idExpediente", idExpediente)
			        .queryParam("idSesion", idSesion);
		
			responseEntity=clienteRest.exchange(builder.build().encode().toString(), HttpMethod.PUT, requestEntity, String.class);
			/*
			 * try {
			 * auditService.audit("expediente: "+idExpediente+" llama a :"+url+" response:"
			 * +responseEntity.getStatusCode(), "UpdStatusDoc", "Upd status del documento");
			 * } catch (LogAdapterException e) {
			 * log.error("Error en ejecucion de auditoria", e); }
			 */

		} catch(HttpClientErrorException | HttpServerErrorException ex) {																		
			
			if(HttpStatus.valueOf(ex.getRawStatusCode() ).is4xxClientError()) {
				throw new DocumentException(DocumentException.MSG_ERROR_EN_CLIENTE + ":" + url +" estatus: " +   ex.getRawStatusCode(), ex);
			}
			if(HttpStatus.valueOf(ex.getRawStatusCode() ).is5xxServerError()){
				throw new DocumentException(DocumentException.MSG_ERROR_EN_SERVIDOR + ":" + url +" estatus: " +   ex.getRawStatusCode(), ex);
		
			}							
		} catch(RestClientException ex){
			// Cuando el servicio no esta disponible
			throw new DocumentException(DocumentException.MSG_ERROR_DE_CONEXION+  ":" + ex.getMessage(), ex);
		}
		
		if(responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
			dossierUpdated=true;
		}
		
		return dossierUpdated;
	}
	/**
	 * Metodo que genera la pista
	 * @param msg descripcion de pista
	 * @param codAudit identificador la pista
	 * @param accionAuditar identificador de la accion realizada
	 */
	public void creaPista(String msg, String codAudit, String accionAuditar) {
		/*
		 * try { auditService.audit(msg,codAudit,accionAuditar); } catch
		 * (LogAdapterException e) { log.error("Error en ejecucion de auditoria", e); }
		 */
	}
	
}
