package io.github.christosmouzouris.urlshortener;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class UrlshortenerApplication {

    public static void main(String[] args) {
        // Load environment variables from .env
        Dotenv dotenv = Dotenv.load();

        System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
        System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
        System.setProperty("POSTGRES_DB", dotenv.get("POSTGRES_DB"));
        System.setProperty("POSTGRES_HOST", dotenv.get("POSTGRES_HOST"));
        System.setProperty("POSTGRES_PORT", dotenv.get("POSTGRES_PORT"));

        SpringApplication.run(UrlshortenerApplication.class, args);
    }
}
