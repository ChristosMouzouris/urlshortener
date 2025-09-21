package io.github.christosmouzouris.urlshortener.repository;

import io.github.christosmouzouris.urlshortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    @Modifying
    @Query("UPDATE Url u SET u.lastAccessedDate = :now " +
            "WHERE u.shortUrl = :shortUrl")
    int updateLastAccessedDate(@Param("shortUrl") String shortUrl, @Param("now") LocalDateTime now);

    Optional<Url> findByShortUrl(String shortUrl);
    Optional<Url> findByLongUrl(String longUrl);
}
