package io.github.christosmouzouris.urlshortener.repository;

import io.github.christosmouzouris.urlshortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);
    Optional<Url> findByLongUrl(String url);
}
