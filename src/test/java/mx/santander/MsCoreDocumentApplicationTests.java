package mx.santander;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan("com.santander")
@TestPropertySource(properties= {"AMBIENTE=local"})
@SpringBootTest
public class MsCoreDocumentApplicationTests {

	@Test
	public void contextLoads() {
	}

}
