package io.github.christosmouzouris.urlshortener.repository;

import io.github.christosmouzouris.urlshortener.model.Url;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

@Testcontainers
@DataJpaTest
public class UrlRepositoryTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private UrlRepository urlRepository;

    @Test
    public void shouldPersistUrlAndAssignIdWhenSaving(){
        // Given: a new Url object
        Url url = new Url();
        url.setShortUrl("short");
        url.setLongUrl("https://urlshortener.com");
        url.setCreationDate(LocalDateTime.now());
        url.setLastAccessedDate(LocalDateTime.now());

        // When: the Url is saved
        Url saved = urlRepository.save(url);

        // Then: the saved Url is not null and has an Id assigned
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindUrlWhenFindingById(){
        // Given: a Url has been saved in the repository
        Url url = new Url();
        url.setShortUrl("short");
        url.setLongUrl("https://urlshortener.com");
        url.setCreationDate(LocalDateTime.now());
        url.setLastAccessedDate(LocalDateTime.now());
        Url saved = urlRepository.save(url);

        // When: a Url is found by Id
        Url found = urlRepository.findById(saved.getId())
                .orElseThrow(()-> new RuntimeException("Id not found"));

        // Then: the found Url is not null
        assertThat(found).isNotNull();
    }

    @Test
    public void shouldUpdateUrlAndPersistIdWhenUpdating(){
        // Given: a Url has been saved in the repository
        Url url = new Url();
        url.setShortUrl("short");
        url.setLongUrl("https://urlshortener.com");
        url.setCreationDate(LocalDateTime.now());
        url.setLastAccessedDate(LocalDateTime.now());
        Url saved = urlRepository.save(url);

        // When: a Url is found
        Url found = urlRepository.findById(saved.getId())
                .orElseThrow(()-> new RuntimeException("Id not found"));

        // And: the Url is updated
        found.setShortUrl("newShort");
        Url updated = urlRepository.save(found);

        // Then: the updated Url is not null and the new shortUrl is correct
        assertThat(updated).isNotNull();
        assertThat(updated.getShortUrl()).isEqualTo("newShort");
    }

    @Test
    public void shouldFindAllSavedUrlsWhenFindingAll(){
        // Given: two Urls have been saved in the repository
        Url url1 = new Url();
        url1.setShortUrl("short1");
        url1.setLongUrl("https://urlshortener.com");
        url1.setCreationDate(LocalDateTime.now());
        url1.setLastAccessedDate(LocalDateTime.now());
        urlRepository.save(url1);

        Url url2 = new Url();
        url2.setShortUrl("short2");
        url2.setLongUrl("https://urlshortener1.com");
        url2.setCreationDate(LocalDateTime.now());
        url2.setLastAccessedDate(LocalDateTime.now());
        urlRepository.save(url2);

        // When: finding all Urls
        List<Url> found = urlRepository.findAll();

        // Then: the list of found Urls is not null and has a length of 2
        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindUrlByShortUrlWhenFindingByShortUrl(){
        // Given: a Url has been saved in the repository
        Url url = new Url();
        url.setShortUrl("short");
        url.setLongUrl("https://urlshortener.com");
        url.setCreationDate(LocalDateTime.now());
        url.setLastAccessedDate(LocalDateTime.now());
        urlRepository.save(url);

        // When: a Url is found by shortUrl
        Url found = urlRepository.findByShortUrl("short")
                .orElseThrow(() -> new RuntimeException("Url not found")); // findByShortUrl returns optional and needs to be handled

        // Then: the found Url is not null and has the correct shortUrl
        assertThat(found).isNotNull();
        assertThat(found.getShortUrl()).isEqualTo("short");
    }

    @Test
    public void shouldFindUrlByLongUrlWhenFindingByLongUrl(){
        // Given: a Url has been saved in the repository
        Url url = new Url();
        url.setShortUrl("short");
        url.setLongUrl("https://urlshortener.com");
        url.setCreationDate(LocalDateTime.now());
        url.setLastAccessedDate(LocalDateTime.now());
        urlRepository.save(url);

        // When: a Url is found by longUrl
        Url found = urlRepository.findByLongUrl("https://urlshortener.com")
                .orElseThrow(() -> new RuntimeException("Url not found")); // findByLongUrl returns optional and needs to be handled

        // Then: the found Url is not null and has the correct longUrl
        assertThat(found).isNotNull();
        assertThat(found.getLongUrl()).isEqualTo("https://urlshortener.com");
    }
}
