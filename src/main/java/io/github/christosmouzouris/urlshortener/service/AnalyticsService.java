package io.github.christosmouzouris.urlshortener.service;

import io.github.christosmouzouris.urlshortener.dto.*;
import io.github.christosmouzouris.urlshortener.exception.UrlNotFoundException;
import io.github.christosmouzouris.urlshortener.mapper.ClicksTrendProjection;
import io.github.christosmouzouris.urlshortener.model.ClickEvent;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.repository.ClickEventRepository;
import io.github.christosmouzouris.urlshortener.repository.UrlRepository;
import io.github.christosmouzouris.urlshortener.util.GeoResult;
import io.github.christosmouzouris.urlshortener.util.RequestInfoUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service layer for managing analytics for short URLs
 * Handles collection of data based on short URL used and user agent
 */
@Service
public class AnalyticsService {

    private final ClickEventRepository clickEventRepository;
    private final UrlRepository urlRepository;
    private final RequestInfoUtil requestInfoUtil;

    public AnalyticsService(ClickEventRepository clickEventRepository,
                            RequestInfoUtil requestInfoUtil,
                            UrlRepository urlRepository) {

        this.clickEventRepository = clickEventRepository;
        this.urlRepository = urlRepository;
        this.requestInfoUtil = requestInfoUtil;
    }

    /**
     * Handles the collection of all requires analytics for internal use such as,
     * short URL segment, time stamp, client type, country code and any extra location details
     *
     * @param url the URL entity that was created when the request was submitted
     * @param request the HTTP request
     */
    public void handleClickEvent(Url url, HttpServletRequest request) {
        ClickEvent clickEvent = new ClickEvent();

        clickEvent.setUrl(url);
        clickEvent.setTimestamp(LocalDateTime.now());
        clickEvent.setClientType(requestInfoUtil.getClient(request));

        GeoResult geoResult = requestInfoUtil.getGeoResult(request);
        clickEvent.setCountryCode(geoResult.getCountryCode());
        clickEvent.setLocationDetails(geoResult.getLocationDetails());

        clickEventRepository.save(clickEvent);
    }

    public StatsResponseDto getStats(){
        return new StatsResponseDto(clickEventRepository.count(), urlRepository.count());
    }

    public long getTotalClicks() {
        return clickEventRepository.count();
    }

    /**
     * Retrieves the count of clicks by each client type from the repository and maps the result
     * to the specified response DTO record
     *
     * @return the list of client types with their counted clicks
     */
    public List<ClicksByBrowserResponseDto> getTotalClicksByBrowser() {
        return clickEventRepository.findTotalClicksByBrowser().stream()
                .map(ce -> new ClicksByBrowserResponseDto(ce.getCount(), ce.getClientType()))
                .toList();
    }

    /**
     * Retrieves the count of clicks by each client type for a specified short URL and maps
     * the result to the specified response DTO record
     *
     * @param shortUrl the URL to filter the clicks by
     * @return the list of client types with their counted clicks for the specified URL
     */
    public List<ClicksByBrowserResponseDto> getTotalClicksByBrowserForUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortUrl));
        return clickEventRepository.findTotalClicksByBrowserForUrl(url.getId()).stream()
                .map(ce -> new ClicksByBrowserResponseDto(ce.getCount(), ce.getClientType()))
                .toList();
    }

    /**
     * Retrieves the count of clicks by each location and maps the result to the specified
     * response DTO record
     *
     * @return the list of locations with their counted clicks
     */
    public List<ClicksByLocationResponseDto> getTotalClicksByLocation() {
        return clickEventRepository.findTotalClicksByLocation().stream()
                .map(ce -> new ClicksByLocationResponseDto(ce.getCount(), ce.getCountryCode()))
                .toList();
    }

    /**
     * Retrieves the count of click by each location (country code) for a specified short URL and maps
     * the result to the specified response DTO record
     *
     * @param shortUrl the URL to filter the clicks by
     * @return the list of country codes with their counted clicks for the specified URL
     */
    public List<ClicksByLocationResponseDto> getTotalClicksByLocationForUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortUrl));
        return clickEventRepository.findTotalClicksByLocationForUrl(url.getId()).stream()
                .map(ce -> new ClicksByLocationResponseDto(ce.getCount(), ce.getCountryCode()))
                .toList();
    }

    public List<ClickEvent> getAllClicksForLocation(String location) {
        return clickEventRepository.findByLocation(location);
    }

    /**
     * Retrieve all clicks for specified URL that are not submitted by suspicious activity such as bots
     *
     * @param shortUrl the URL to filter clicks by
     * @return the list of clicks
     */
    public List<ClickEvent> getRealClicks(String shortUrl){
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortUrl));
        return clickEventRepository.findAllClickEventsFilterByBots(url.getId());
    }

    /**
     * Retrieve the URLs that have had the most clicks in descending order
     *
     * @param limit the number of entries to limit the search to
     * @return list of URLs with a count of their clicks, short urls and original domain
     */
    public List<TopUrlsResponseDto> getTopUrls(int limit){
        return clickEventRepository.findTopUrls(limit).stream()
                .map(ce -> new TopUrlsResponseDto(ce.getCount(), ce.getShortUrl(), ce.getLongUrl()))
                .toList();
    }


    /**
     * Retrieve an ordered list of dates with the amount of clicks tracked for each
     *
     * @param days the number of days to go back starting from today
     * @return a list of records containing dates and number of clicks
     */
    public List<ClicksTrendResponseDto> getClicksTrends(int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        LocalDateTime cutoff = startDate.atStartOfDay();

        List<ClicksTrendProjection> rawTrends = clickEventRepository.findClicksTrend(cutoff);

        Map<LocalDate, Long> clicksByDate = rawTrends.stream()
                .collect(Collectors.toMap(
                        ClicksTrendProjection::getDate,
                        ClicksTrendProjection::getClicks,
                        (a, b) -> a, LinkedHashMap::new
                ));

        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(days + 1L)
                .map(date -> new ClicksTrendResponseDto(date, clicksByDate.getOrDefault(date, 0L)))
                .toList();
    }
}
