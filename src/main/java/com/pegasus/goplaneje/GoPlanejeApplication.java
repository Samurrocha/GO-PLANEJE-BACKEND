package com.pegasus.goplaneje;

import com.pegasus.goplaneje.config.AppProperties;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class GoPlanejeApplication {

    public static void main(String[] args) {

        SpringApplication.run(GoPlanejeApplication.class, args);
    }

}
