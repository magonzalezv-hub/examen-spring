package mx.santander.idd.core.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mx.santander.idd.core.exception.DocumentException;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import mx.santander.idd.core.service.IDocumentService;
import lombok.extern.slf4j.Slf4j;

/*import mx.santander.pid.logadapter.core.types.service.ILogAdapterAuditService;
import mx.santander.pid.logadapter.exception.LogAdapterException;*/


/**
 * Se encarga de subir a la cola los documentos anverso y reverso.
 * @author Mintsait
 *
 */
@Slf4j
@RestController
@RequestMapping("/documentos")
public class DocumentController {
	
	@Autowired
	private IDocumentService documentService;
	
	 /**Dependencia paraguardar mensajes de auditoria*/
	/*
	 * @Autowired //private MockPistas auditService; private ILogAdapterAuditService
	 * auditService;
	 */

	/**
	 * Sube el documento anverso a la cola.
	 * @param idExpediente ID del expediente.
	 * @param idSesion ID de la sesión
	 * @param file Archivo a subir.
	 * @return Respuesta del servicio. OK si se subió de manera correcta.
	 * @throws DocumentException 
	 */
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "The document was incorporated correctly to the dossier"),
            @ApiResponse(code = 422, message = "The document could not be processed"),
            @ApiResponse(code = 500, message = "The service faild, check configuration"),
            @ApiResponse(code = 503, message = "Service Unavailable")
    })
	@PostMapping("/anversos")
	public ResponseEntity<String> anverso(@Valid @RequestParam(name="idExpediente") String idExpediente, @Valid @RequestParam(name="idSesion") String idSesion, @Valid @RequestParam MultipartFile file) throws DocumentException {
		HttpStatus code=null;
		String	mensaje=null;
		/*
		 * try {
		 * auditService.audit("DocumentController/anversos/idExpediente: "+idExpediente,
		 * "DocControllerAnv", "Recibe Anverso"); } catch (LogAdapterException e) {
		 * log.error("Error en ejecucion de auditoria", e); }
		 */
		
		try {
			Boolean resultado = documentService.subirAnverso(idExpediente, idSesion, file);
			if(resultado) {
				code = HttpStatus.OK;
			}else {
				code = HttpStatus.UNPROCESSABLE_ENTITY;
				throw new DocumentException("Error al subir el Anverso"+ code);
			}
		} catch (DocumentException exIdd) {		
			throw new DocumentException(exIdd.getMessage(), exIdd); 
		}
		
		return new ResponseEntity<>(mensaje,code);
	}


	/**
     * Sube el documento reverso a la cola.
	 * @param idExpediente ID del expediente.
	 * @param idSesion ID de la sesión
	 * @param file Archivo a subir.
	 * @return Respuesta del servicio. OK si se subió de manera correcta.
	 * @throws DocumentException 
	 */
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "The document was incorporated correctly to the dossier"),
            @ApiResponse(code = 422, message = "The document could not be processed"),
            @ApiResponse(code = 500, message = "The service faild, check configuration")
    })
	@PostMapping("/reversos")
	public ResponseEntity<String> reverso(@Valid @RequestParam(name="idExpediente") String idExpediente, @Valid @RequestParam(name="idSesion") String idSesion, @Valid @RequestBody MultipartFile file) throws DocumentException{
		HttpStatus code=null;
		String	mensaje=null;
		
		/*
		 * try {
		 * auditService.audit("DocumentController/reversos/idExpediente: "+idExpediente,
		 * "DocControllerRev", "Recibe Reverso"); } catch (LogAdapterException e) {
		 * log.error("Error en ejecucion de auditoria", e); }
		 */

		try {
			Boolean resultado = documentService.subirReverso(idExpediente, idSesion, file);
			
			if(resultado) {
				code = HttpStatus.OK;
			}else {
				code = HttpStatus.UNPROCESSABLE_ENTITY;
				throw new DocumentException("Error al subir el Reverso"+ code);
			}
			
		} catch(DocumentException exIdd) {	
			throw new DocumentException(exIdd.getMessage() + ":", exIdd);
		}
		
		
		
		return new ResponseEntity<>(mensaje,code);
	}

}

