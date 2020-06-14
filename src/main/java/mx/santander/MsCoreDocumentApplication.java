package mx.santander;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 *Clase principal en donde corre spring.
 * @author Mintsait
 *
 
 */
@SpringBootApplication
@ComponentScan({"mx.santander","mx.santander.idd","mx.santander.pid"})
@EnableCaching
public class MsCoreDocumentApplication {

	/**
     * Metodo principal para el MS Video-service
     * @param args valores que recibe el MS
     */
	public static void main(String[] args) {
		SpringApplication.run(MsCoreDocumentApplication.class, args);
	}

}
