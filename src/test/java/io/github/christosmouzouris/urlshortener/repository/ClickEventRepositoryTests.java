package io.github.christosmouzouris.urlshortener.repository;

import io.github.christosmouzouris.urlshortener.mapper.ClicksByBrowserProjection;
import io.github.christosmouzouris.urlshortener.mapper.ClicksByLocationProjection;
import io.github.christosmouzouris.urlshortener.mapper.TopUrlsProjection;
import io.github.christosmouzouris.urlshortener.model.ClickEvent;
import io.github.christosmouzouris.urlshortener.model.ClientType;
import io.github.christosmouzouris.urlshortener.model.Url;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Testcontainers
@DataJpaTest
public class ClickEventRepositoryTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private ClickEventRepository clickEventRepository;
    @Autowired
    private UrlRepository urlRepository;

    @Test
    public void shouldPersistClickEventAndAssignIdWhenSaving() {
        // Given: an existing Url in the repository
        Url url = createUrl();
        Url savedUrl = urlRepository.save(url);

        // And: a click event linked to that Url
        ClickEvent clickEvent = createClickEvent(savedUrl);

        // When: the click event is saved
        ClickEvent savedClickEvent = clickEventRepository.save(clickEvent);

        // Then: the click event is not null, has an Id assigned and a Url assigned
        assertThat(savedClickEvent).isNotNull();
        assertThat(savedClickEvent.getId()).isGreaterThan(0);
        assertThat(savedClickEvent.getUrl()).isEqualTo(savedUrl);
    }

    @Test
    public void shouldFindClickEventWhenFindingById() {
        // Given: an existing Url in the repository
        Url url = createUrl();
        Url savedUrl = urlRepository.save(url);

        // And: an existing click event in the repository
        ClickEvent clickEvent = createClickEvent(savedUrl);
        ClickEvent savedClickEvent = clickEventRepository.save(clickEvent);

        // When: the click event is found by Id
        ClickEvent foundClickEvent = clickEventRepository.findById(savedClickEvent.getId())
                .orElseThrow(()-> new RuntimeException("Id not found"));

        // Then: the found click event is not null
        assertThat(foundClickEvent).isNotNull();
    }

    @Test
    public void shouldUpdateClickEventAndPersistWhenUpdating() {
        // Given: an existing Url in the repository
        Url url = createUrl();
        Url savedUrl = urlRepository.save(url);

        // And: an existing click event in the repository
        ClickEvent clickEvent = createClickEvent(savedUrl);
        ClickEvent savedClickEvent = clickEventRepository.save(clickEvent);

        // When: a click event is found
        ClickEvent foundClickEvent = clickEventRepository.findById(savedClickEvent.getId())
                .orElseThrow(()-> new RuntimeException("Id not found"));

        // And: the click event is updated
        foundClickEvent.setClientType(ClientType.FIREFOX);
        ClickEvent updatedClickEvent = clickEventRepository.save(foundClickEvent);

        // Then:the updated click event is not null and the new client is correct
        assertThat(updatedClickEvent).isNotNull();
        assertThat(updatedClickEvent.getClientType()).isEqualTo(ClientType.FIREFOX);
    }

    @Test
    public void shouldFindAllClickEventsWhenFindingAll() {
        // Given: an existing Url in the repository
        Url url = createUrl();
        Url savedUrl = urlRepository.save(url);

        // And: two existing click events in the repository
        ClickEvent clickEvent1 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent1);
        ClickEvent clickEvent2 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent2);

        // When: finding all click events
        List<ClickEvent> foundClickEvents = clickEventRepository.findAll();

        // Then: the list of found click events is not null and has a length of 2
        assertThat(foundClickEvents).isNotNull();
        assertThat(foundClickEvents.size()).isEqualTo(2);

    }

    @Test
    public void shouldFindTotalClickEventsWhenFindingByClientType() {
        // Given: an existing Url in the repository
        Url url = createUrl();
        Url savedUrl = urlRepository.save(url);

        // And: two existing click events in the repository
        ClickEvent clickEvent1 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent1);
        ClickEvent clickEvent2 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent2);

        // When: finding all click events by client type
        List<ClicksByBrowserProjection> foundClickEvents = clickEventRepository.findTotalClicksByBrowser();

        // Then: the projections contain expected results for client
        assertThat(foundClickEvents)
                .extracting(ClicksByBrowserProjection::getClientType, ClicksByBrowserProjection::getCount)
                .containsExactlyInAnyOrder(
                        tuple("CHROME", 2L)
                );
    }

    @Test
    public void shouldFindTotalClickEventsWhenFindingByClientTypeForUrl() {
        // Given: an existing Url in the repository
        Url url = createUrl();
        Url savedUrl = urlRepository.save(url);

        // And: two existing click events in the repository
        ClickEvent clickEvent1 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent1);
        ClickEvent clickEvent2 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent2);

        // When: find all click events by client type for specified Url
        List<ClicksByBrowserProjection> foundClickEvents = clickEventRepository.
                findTotalClicksByBrowserForUrl(savedUrl.getId());

        // Then: the projections contain expected results for client
        assertThat(foundClickEvents)
                .extracting(ClicksByBrowserProjection::getClientType, ClicksByBrowserProjection::getCount)
                .containsExactlyInAnyOrder(
                        tuple("CHROME", 2L)
                );
    }

    @Test
    public void shouldFindTotalClickEventsWhenFindingByLocation() {
        // Given: an existing Url in the repository
        Url url = createUrl();
        Url savedUrl = urlRepository.save(url);

        // And: two existing click events in the repository
        ClickEvent clickEvent1 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent1);
        ClickEvent clickEvent2 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent2);

        // When: find total click events by country code
        List<ClicksByLocationProjection> foundClickEvents = clickEventRepository.findTotalClicksByLocation();

        // Then: the projection contains expected results for country code
        assertThat(foundClickEvents)
                .extracting(ClicksByLocationProjection::getCountryCode, ClicksByLocationProjection::getCount)
                .containsExactlyInAnyOrder(
                        tuple("UK", 2L)
                );
    }

    @Test
    public void shouldFindAllClicksEventsWhenFindingByLocation() {
        // Given: an existing Url in the repository
        Url url = createUrl();
        Url savedUrl = urlRepository.save(url);

        // And: two existing click events in the repository
        ClickEvent clickEvent1 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent1);
        ClickEvent clickEvent2 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent2);

        // When: find all click events by country code
        List<ClickEvent> foundClickEvents = clickEventRepository.findByLocation("UK");

        // Then: the list of found click events is not null and has a length of 2
        assertThat(foundClickEvents).isNotNull();
        assertThat(foundClickEvents.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindTopUrlsWhenFindingByTopUrls() {
        // Given: an existing Url in the repository
        Url url1 = createUrl("short");
        url1.setLongUrl("long");
        Url savedUrl1 = urlRepository.save(url1);
        Url url2 = createUrl("short1");
        url2.setLongUrl("long1");
        Url savedUrl2 = urlRepository.save(url2);

        // And: three existing click events in the repository
        ClickEvent clickEvent1 = createClickEvent(savedUrl1);
        clickEventRepository.save(clickEvent1);
        ClickEvent clickEvent2 = createClickEvent(savedUrl1);
        clickEventRepository.save(clickEvent2);
        ClickEvent clickEvent3 = createClickEvent(savedUrl2);
        clickEventRepository.save(clickEvent3);

        // When: find the top clicked Urls
        List<TopUrlsProjection> topUrls = clickEventRepository.findTopUrls(1);

        // Then: the projection contains expected results for top Urls
        assertThat(topUrls).isNotNull();
        assertThat(topUrls.size()).isEqualTo(1);
        assertThat(topUrls)
                .extracting(TopUrlsProjection::getShortUrl, TopUrlsProjection::getCount)
                .containsExactlyInAnyOrder(
                        tuple(url1.getShortUrl(), 2L)
                );
    }

    @Test
    public void shouldFindAllNonBotClicksEventsWhenFilterByBots() {
        //Given: an existing Url in the repository
        Url url = createUrl();
        Url savedUrl = urlRepository.save(url);

        // And: two existing click events in the repository
        ClickEvent clickEvent1 = createClickEvent(savedUrl);
        clickEventRepository.save(clickEvent1);
        ClickEvent clickEvent2 = createClickEvent(savedUrl, ClientType.BOT);
        clickEventRepository.save(clickEvent2);

        // When:
        List<ClickEvent> clickEvents = clickEventRepository
                .findAllClickEventsFilterByBots(savedUrl.getId());

        // Then:
        assertThat(clickEvents).isNotNull();
        assertThat(clickEvents.size()).isEqualTo(1);
        assertThat(clickEvents)
                .extracting(ClickEvent::getClientType)
                .containsExactlyInAnyOrder(ClientType.CHROME);
    }

    // Methods to create objects required for testing
    private Url createUrl() {
        Url url = new Url();
        url.setShortUrl("short");
        url.setLongUrl("https://urlshortener.com");
        url.setCreationDate(LocalDateTime.now());
        url.setLastAccessedDate(LocalDateTime.now());

        return url;
    }

    private Url createUrl(String shortUrl) {
        Url url = new Url();
        url.setShortUrl(shortUrl);
        url.setLongUrl("https://urlshortener.com");
        url.setCreationDate(LocalDateTime.now());
        url.setLastAccessedDate(LocalDateTime.now());

        return url;
    }

    private ClickEvent createClickEvent(Url url) {
        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setUrl(url);
        clickEvent.setTimestamp(LocalDateTime.now());
        clickEvent.setClientType(ClientType.CHROME);
        clickEvent.setCountryCode("UK");
        clickEvent.setLocationDetails(Map.of("city", "London", "Region", "London"));

        return clickEvent;
    }

    private ClickEvent createClickEvent(Url url, ClientType clientType) {
        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setUrl(url);
        clickEvent.setTimestamp(LocalDateTime.now());
        clickEvent.setClientType(clientType);
        clickEvent.setCountryCode("UK");
        clickEvent.setLocationDetails(Map.of("city", "London", "Region", "London"));

        return clickEvent;
    }
}

