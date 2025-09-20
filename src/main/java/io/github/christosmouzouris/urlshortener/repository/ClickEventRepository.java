package io.github.christosmouzouris.urlshortener.repository;

import io.github.christosmouzouris.urlshortener.mapper.ClicksByBrowserProjection;
import io.github.christosmouzouris.urlshortener.mapper.ClicksByLocationProjection;
import io.github.christosmouzouris.urlshortener.mapper.TopUrlsProjection;
import io.github.christosmouzouris.urlshortener.model.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

    @Query("SELECT COUNT(ce) AS count, ce.clientType AS clientType " +
            "FROM ClickEvent ce GROUP BY ce.clientType ORDER BY ce.clientType ASC")
    List<ClicksByBrowserProjection> findTotalClicksByBrowser();

    @Query("SELECT COUNT(ce) AS count, ce.clientType AS clientType " +
            "FROM ClickEvent ce WHERE ce.shortUrlId = :shortUrlId " +
            "GROUP BY ce.clientType ORDER BY ce.clientType ASC")
    List<ClicksByBrowserProjection> findTotalClicksByBrowserForUrl(@Param("shortUrlId") long shortUrlId);

    @Query("SELECT COUNT(ce) AS count, ce.countryCode AS countryCode " +
            "FROM ClickEvent ce " +
            "GROUP BY ce.countryCode ORDER BY ce.countryCode ASC")
    List<ClicksByLocationProjection> findTotalClicksByLocation();

    @Query("SELECT ce FROM ClickEvent ce WHERE ce.countryCode = :countryCode")
    List<ClickEvent> findByLocation(@Param("countryCode") String countryCode);

    @Query("SELECT COUNT(ce) AS count, ce.url.shortUrl as shortUrl " +
            "FROM ClickEvent ce ORDER BY COUNT(ce) DESC " +
            "LIMIT :limit")
    List<TopUrlsProjection> findTopUrls(@Param("limit") int limit);

    @Query("Select ce FROM ClickEvent ce " +
            "WHERE ce.clientType != 'Bots' AND ce.shortUrlId = :shortUrlid")
    List<ClickEvent> findAllClickEventsFilterByBots(@Param("shortUrlId") long shortUrlId);
}
