package pinup.backend.pinupeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class PinupEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinupEurekaApplication.class, args);
    }

}
