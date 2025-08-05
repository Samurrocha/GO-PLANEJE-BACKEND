package com.pegasus.goplaneje.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final String jwtSecret;

    public AppProperties(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

}
