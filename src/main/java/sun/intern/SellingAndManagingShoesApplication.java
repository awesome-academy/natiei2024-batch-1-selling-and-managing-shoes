package sun.intern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import sun.intern.config.JwtApplicationProperty;

@SpringBootApplication
@EnableConfigurationProperties({JwtApplicationProperty.class})
public class SellingAndManagingShoesApplication {
    public static void main(String[] args) {
        SpringApplication.run(SellingAndManagingShoesApplication.class, args);
    }

}
