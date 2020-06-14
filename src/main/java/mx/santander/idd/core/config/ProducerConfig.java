package mx.santander.idd.core.config;



import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import lombok.Data;

/**
 * @author MintSait
 *
* Esta es la clase que permite por medio de sus anotaciones
 * inyectar las configuraciones personalizadas externalizadas 
 * (ya sea por medio del archivo bootstrap.yml
 * O bien por el servicio de configuracion referenciado por el mismo)
 */
@Data
@Configuration

public class ProducerConfig {

	@Value("${doc.santander.queue}")
	private String queueName;

	@Value("${doc.santander.exchange}")
	private String exchange;

	@Value("${doc.santander.routingkey}")
	private String routingkey;
	
	/**
	 * Metodo encargado retornar el el valor del MQ
	 * @return regresa el valor de MQ
	 */
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}
	/**
	 * Metodo encargado de realizar el intercambio de los MQ 
	 * @return el valor del DirectExchange del intercambio
	 */
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(exchange);
	}

	/**
	 * Metodo para enlazar DirectExchange con la cola 
	 * @param queue valor de la cola
	 * @param exchange valor del intercambio 
	 * @return regresa el valor de la contruccion de MQ
	 */
	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(routingkey);
	}

	/**
	 * Metodo de convertir el MQ en Jason
	 * @return regresa el valor de MQ a Json
	 */
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	/**
	 * Metodo encargado de realizar la conexion con el server MQ
	 * @param connectionFactory valores de configuracion
	 * @return regresa el mensaje MQ
	 */
	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
	
}
