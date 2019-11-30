package pl.mwiski.dieticianfrontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableFeignClients
public class DieticianFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DieticianFrontendApplication.class, args);
    }

}
