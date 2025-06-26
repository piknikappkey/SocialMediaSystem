package top.zymd3414.sm_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import top.zymd3414.sm_springboot.component.StaticResourceProperties;

@SpringBootApplication
@EnableConfigurationProperties(StaticResourceProperties.class)
public class SmSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmSpringbootApplication.class, args);
    }

}
