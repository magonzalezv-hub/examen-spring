package mx.santander.idd.core.domain.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;


/**
 * Contiene las propiedades del expediente.
 * @author Mintsait
 *
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, scope = ExpedienteDTO.class)
public class ExpedienteDTO implements Serializable{
	
	/** Variable para serializar la clase. */
	private static final long serialVersionUID = 1L;
	/**
	 * idExpediente  variable del expediente.
	 */
	private String idExpediente;
	/**
	 * evidence identificador de evidencia.
	 */
	private String evidence;
	/**
	 * evidenceType. Tipo de archivo.
	 */
	private String evidenceType;
	/**
	 * idValidas identificador validas.
	 */
	private String idValidas;
	/**
	 * identificador de sesion.
	 */
	private String idSesion;
	
	/**
	*Constructor de la clase.
	*/
	public ExpedienteDTO() {
		
	}
	
	/**
	 * Constructor de la clase.
	 * @param idExpediente ID del expediente.
	 * @param evidence Valor de la evidencia.
	 * @param idSesion ID de la sesión.
	 */
	public ExpedienteDTO(String idExpediente, String evidence, String evidenceType, String idSesion) {
	    this.idExpediente=idExpediente;
	    this.evidence=evidence;
	    this.idSesion=idSesion;
	    this.evidenceType=evidenceType;
	}
	

	/**
	* Sobrecarga de toString,
	*/
	@Override
	public String toString() {
		return "ExpedienteDTO [idExpediente = " + idExpediente + "evidence = " + evidence +"idValidas = "+idValidas+"]";
	}
}

