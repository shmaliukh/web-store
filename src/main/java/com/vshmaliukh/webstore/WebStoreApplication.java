package com.vshmaliukh.webstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource({
        "classpath:application-oauth2.yaml",
        "classpath:application.yaml",
})
@SpringBootApplication
public class WebStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebStoreApplication.class, args);
    }

}
