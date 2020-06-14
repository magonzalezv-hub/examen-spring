package mx.santander.idd.core.mq.service;

import mx.santander.idd.core.domain.model.ExpedienteDTO;
/**
 * 
 * @author MinSanit
 * Interface para el envió de expendiente.
 */

public interface IProducerDossierSenderService {

	/**
	 * Método utilizado para el envió de expediente.
	 * @param expediente
	 */
	public void send(ExpedienteDTO expediente);

}
