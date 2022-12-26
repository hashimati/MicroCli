package ${pack}.${artifact};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Lazy;


@EnableEurekaServer
@SpringBootApplication
public class ${appName}Application {

    public static void main(String[] args) {
        SpringApplication.run(${appName}Application.class, args);
    }

}
