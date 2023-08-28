package uol.compass.challenge3;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class Challenge3Application {

	public static void main(String[] args) {
		SpringApplication.run(Challenge3Application.class, args);
		
	}

}
