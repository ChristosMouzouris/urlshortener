package io.github.christosmouzouris.urlshortener.service;

import io.github.christosmouzouris.urlshortener.dto.ClicksByBrowserResponseDto;
import io.github.christosmouzouris.urlshortener.dto.ClicksByLocationResponseDto;
import io.github.christosmouzouris.urlshortener.dto.TopUrlsResponseDto;
import io.github.christosmouzouris.urlshortener.exception.UrlNotFoundException;
import io.github.christosmouzouris.urlshortener.mapper.ClicksByBrowserProjection;
import io.github.christosmouzouris.urlshortener.mapper.ClicksByLocationProjection;
import io.github.christosmouzouris.urlshortener.mapper.TopUrlsProjection;
import io.github.christosmouzouris.urlshortener.model.ClickEvent;
import io.github.christosmouzouris.urlshortener.model.ClientType;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.repository.ClickEventRepository;
import io.github.christosmouzouris.urlshortener.repository.UrlRepository;
import io.github.christosmouzouris.urlshortener.util.GeoResult;
import io.github.christosmouzouris.urlshortener.util.RequestInfoUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTests {

    @Mock
    private RequestInfoUtil requestInfoUtil;

    @Mock
    private ClickEventRepository clickEventRepository;

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    public void shouldPersistClickEventWithNecessaryValues() {
        // Given: a Url object and an HTTP request
        Url url = createUrl();
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(requestInfoUtil.getClient(any()))
                .thenReturn(ClientType.CHROME);
        when(requestInfoUtil.getGeoResult(any()))
                .thenReturn(createGeoResult());

        // When: the handlClickEvent method is called with a url object and an http request
        analyticsService.handleClickEvent(url, request);

        // Then: the constructed click event is persisted in the database
        ArgumentCaptor<ClickEvent> captor = ArgumentCaptor.forClass(ClickEvent.class);
        verify(clickEventRepository, times(1)).save(captor.capture());

        ClickEvent clickEvent = captor.getValue();

        // And: has the correct values in its fields
        assertThat(clickEvent).isNotNull();
        assertThat(clickEvent.getUrl()).isEqualTo(url);
        assertThat(clickEvent.getTimestamp()).isNotNull();
        assertThat(clickEvent.getClientType()).isEqualTo(ClientType.CHROME);
        assertThat(clickEvent.getCountryCode()).isEqualTo("UK");
    }

    @Test
    public void shouldReturnTotalClicksByBrowser() {
        // Given: two clicks by browser projections found by the repository
        ClicksByBrowserProjection projection1 = mock(ClicksByBrowserProjection.class);
        when(projection1.getCount()).thenReturn(5L);
        when(projection1.getClientType()).thenReturn("CHROME");

        ClicksByBrowserProjection projection2 = mock(ClicksByBrowserProjection.class);
        when(projection2.getCount()).thenReturn(10L);
        when(projection1.getClientType()).thenReturn("BRAVE");

        when(clickEventRepository.findTotalClicksByBrowser())
                .thenReturn(List.of(projection1, projection2));


        // When: the getTotalClicksByBrowser is called
        List<ClicksByBrowserResponseDto> result = analyticsService.getTotalClicksByBrowser();

        // Then: the projections should be mapped to the correct DTO and have the correct values
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).clicks()).isEqualTo(projection1.getCount());
        assertThat(result.get(0).browser()).isEqualTo(projection1.getClientType());
        assertThat(result.get(1).clicks()).isEqualTo(projection2.getCount());
        assertThat(result.get(1).browser()).isEqualTo(projection2.getClientType());
    }

    @Test
    public void shouldReturnTotalCLicksByBrowserForUrl() {
        // Given: a Url object found by the repository and
        Url url = createUrl();

        // And: two clicks by browser projections found by the repository
        ClicksByBrowserProjection projection1 = mock(ClicksByBrowserProjection.class);
        when(projection1.getCount()).thenReturn(5L);
        when(projection1.getClientType()).thenReturn("CHROME");

        ClicksByBrowserProjection projection2 = mock(ClicksByBrowserProjection.class);
        when(projection2.getCount()).thenReturn(10L);
        when(projection1.getClientType()).thenReturn("BRAVE");

        when(urlRepository.findByShortUrl(url.getShortUrl()))
                .thenReturn(Optional.of(url));

        when(clickEventRepository.findTotalClicksByBrowserForUrl(url.getId()))
                .thenReturn(List.of(projection1, projection2));

        // When: the getTotalClicksByBrowserForUrl is called with a short Url
        List<ClicksByBrowserResponseDto> result = analyticsService.getTotalClicksByBrowserForUrl(url.getShortUrl());

        // Then: the projections should be mapped to the correct DTO and have the correct values
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).clicks()).isEqualTo(projection1.getCount());
        assertThat(result.get(0).browser()).isEqualTo(projection1.getClientType());
        assertThat(result.get(1).clicks()).isEqualTo(projection2.getCount());
        assertThat(result.get(1).browser()).isEqualTo(projection2.getClientType());
    }

    @Test
    public void shouldThrowErrorIfUrlDoesNotExistWhenReturningTotalClicksByBrowserForUrl() {
        // Given: a short Url string
        String shortUrl = "short";

        when(urlRepository.findByShortUrl(shortUrl))
                .thenReturn(Optional.empty());

        // When: the getTotalClicksByBrowserForUrl is called with a non existing short Url
        // Then: a UrlNotFoundException is thrown with the correct message
        assertThatThrownBy(() -> analyticsService.getTotalClicksByBrowserForUrl(shortUrl))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessage("Url not found: " + shortUrl);

        // And: the findTotalClicksByBrowserForUrl repository method is never called
        verify(clickEventRepository, never()).findTotalClicksByBrowserForUrl(anyLong());
    }

    @Test
    public void shouldReturnTotalClicksByLocation() {
        // Given: two clicks by location projections returned by the repository
        ClicksByLocationProjection projection1 = mock(ClicksByLocationProjection.class);
        when(projection1.getCount()).thenReturn(5L);
        when(projection1.getCountryCode()).thenReturn("UK");
        ClicksByLocationProjection projection2 = mock(ClicksByLocationProjection.class);
        when(projection2.getCount()).thenReturn(10L);
        when(projection1.getCountryCode()).thenReturn("US");

        when(clickEventRepository.findTotalClicksByLocation())
                .thenReturn(List.of(projection1, projection2));

        // When: getTotalClicksByLocation is called
        List<ClicksByLocationResponseDto> result = analyticsService.getTotalClicksByLocation();

        // Then: the projections should be mapped to the correct DTO and have the correct values
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).clicks()).isEqualTo(projection1.getCount());
        assertThat(result.get(0).location()).isEqualTo(projection1.getCountryCode());
        assertThat(result.get(1).clicks()).isEqualTo(projection2.getCount());
        assertThat(result.get(1).location()).isEqualTo(projection2.getCountryCode());
    }

    @Test
    public void shouldReturnTopUrls() {
        // Given: two top url projections found by the repository
        TopUrlsProjection projection1 = mock(TopUrlsProjection.class);
        when(projection1.getCount()).thenReturn(5L);
        when(projection1.getShortUrl()).thenReturn("short");
        TopUrlsProjection projection2 = mock(TopUrlsProjection.class);
        when(projection2.getCount()).thenReturn(10L);
        when(projection1.getShortUrl()).thenReturn("short1");

        when(clickEventRepository.findTopUrls(2))
                .thenReturn(List.of(projection1, projection2));

        // When: the getTopUrls method is called
        List<TopUrlsResponseDto> result = analyticsService.getTopUrls(2);

        // Then: the projections should be mapped to the correct DTO and have the correct values
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).clicks()).isEqualTo(projection1.getCount());
        assertThat(result.get(0).shortUrl()).isEqualTo(projection1.getShortUrl());
        assertThat(result.get(1).clicks()).isEqualTo(projection2.getCount());
        assertThat(result.get(1).shortUrl()).isEqualTo(projection2.getShortUrl());
    }

    // Private methods to construct objects
    private Url createUrl() {
        Url url = new Url();
        url.setId(1L);
        url.setShortUrl("short");
        url.setLongUrl("https://urlshortener.com");
        url.setLastAccessedDate(LocalDateTime.now());
        url.setCreationDate(LocalDateTime.now());

        return url;
    }

    private GeoResult createGeoResult() {
        Map<String, String> locationDetails = new HashMap<>();
        locationDetails.put("City", "London");
        locationDetails.put("Region", "London");

        return new GeoResult("UK", locationDetails);
    }
}
