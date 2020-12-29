package org.fzb.demo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;


/**
 * GatewayApplication
 *
 * @author fengzhenbing
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@RestController
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
