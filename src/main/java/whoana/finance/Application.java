/**
 *
 * Main Application
 *
 */
package whoana.finance;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ImportResource("classpath:finance-context.xml")
@ComponentScan({"whoana.finance"})
//@EnableScheduling
public class Application {

	public static void main(String[] args) {

		//SpringApplication.run(Application.class, args);

		SpringApplicationBuilder sab = new SpringApplicationBuilder(Application.class);
		sab.build().addListeners(new ApplicationPidFileWriter("./finance.pid"));
		sab.run(args);


	}


}
