package io.github.christosmouzouris.urlshortener.repository;

import io.github.christosmouzouris.urlshortener.mapper.ClicksByBrowserProjection;
import io.github.christosmouzouris.urlshortener.mapper.ClicksByLocationProjection;
import io.github.christosmouzouris.urlshortener.mapper.ClicksTrendProjection;
import io.github.christosmouzouris.urlshortener.mapper.TopUrlsProjection;
import io.github.christosmouzouris.urlshortener.model.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

    @Query("SELECT COUNT(ce) AS count, ce.clientType AS clientType " +
            "FROM ClickEvent ce " +
            "GROUP BY ce.clientType " +
            "ORDER BY ce.clientType ASC")
    List<ClicksByBrowserProjection> findTotalClicksByBrowser();

    @Query("SELECT COUNT(ce) AS count, ce.clientType AS clientType " +
            "FROM ClickEvent ce WHERE ce.shortUrlId = :shortUrlId " +
            "GROUP BY ce.clientType " +
            "ORDER BY ce.clientType ASC")
    List<ClicksByBrowserProjection> findTotalClicksByBrowserForUrl(@Param("shortUrlId") long shortUrlId);

    @Query("SELECT COUNT(ce) AS count, ce.countryCode AS countryCode " +
            "FROM ClickEvent ce " +
            "GROUP BY ce.countryCode " +
            "ORDER BY ce.countryCode ASC")
    List<ClicksByLocationProjection> findTotalClicksByLocation();

    @Query("SELECT COUNT(ce) AS count, ce.countryCode AS countryCode " +
            "FROM ClickEvent ce WHERE ce.shortUrlId = :shortUrlId " +
            "GROUP BY ce.countryCode " +
            "ORDER BY ce.countryCode ASC")
    List<ClicksByLocationProjection> findTotalClicksByLocationForUrl(@Param("shortUrlId") long shortUrlId);

    @Query("SELECT ce FROM ClickEvent ce " +
            "WHERE ce.countryCode = :countryCode")
    List<ClickEvent> findByLocation(@Param("countryCode") String countryCode);

    @Query(
            value = "SELECT COUNT(ce.id) AS count, u.short_url AS shortUrl, u.long_url AS longUrl " +
                    "FROM click_event ce " +
                    "JOIN url u ON ce.short_url_id = u.id " +
                    "GROUP BY u.short_url, u.long_url " +
                    "ORDER BY COUNT(ce.id) DESC " +
                    "LIMIT :limit",
            nativeQuery = true
    )
    List<TopUrlsProjection> findTopUrls(@Param("limit") int limit);

    @Query("SELECT ce FROM ClickEvent ce " +
            "WHERE ce.clientType != 'BOT' AND ce.shortUrlId = :shortUrlId")
    List<ClickEvent> findAllClickEventsFilterByBots(@Param("shortUrlId") long shortUrlId);

    @Query(
            value = "SELECT DATE(ce.timestamp) AS date, COUNT(*) AS clicks " +
                    "FROM click_event ce " +
                    "WHERE ce.timestamp >= :cutoff " +
                    "GROUP BY DATE(ce.timestamp) " +
                    "ORDER BY DATE(ce.timestamp)",
            nativeQuery = true
    )
    List<ClicksTrendProjection> findClicksTrend(@Param("cutoff") LocalDateTime cutoff);
}
