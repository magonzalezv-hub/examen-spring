package mx.santander.idd.core.mq.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mx.santander.idd.core.domain.model.ExpedienteDTO;

/**
 * Servicio que envia el expediente al MQ.
 * @author MintSait
 *
 */
@Service
public class ProducerDossierSenderService implements IProducerDossierSenderService {
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${doc.santander.exchange}")
	private String exchange;
	
	@Value("${doc.santander.routingkey}")
	private String routingkey;	
	
	/**
	 * Envia el expediente.
	 * @param expediente Contiene las propiedades del expediente.
	 */
	public void send(ExpedienteDTO expediente) {
		amqpTemplate.convertAndSend(exchange, routingkey, expediente);
	}
}

