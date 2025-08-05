package com.pegasus.goplaneje;

import com.pegasus.goplaneje.config.AppProperties;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class GoPlanejeApplication {

    public static void main(String[] args) {

        // Só carrega .env em ambiente local (dev)
        String activeProfile = System.getenv("SPRING_PROFILES_ACTIVE");
        if (activeProfile == null || activeProfile.equals("dev")) {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

            dotenv.entries().forEach(entry ->
                    System.setProperty(entry.getKey(), entry.getValue())
            );

            System.out.println("🚀 Ambiente "+activeProfile+": Variáveis de .env carregadas");
        } else {
            System.out.println("🚀 Ambiente " + activeProfile + ": Render ou Prod. Ignorando .env");
        }

        SpringApplication.run(GoPlanejeApplication.class, args);
    }

}
