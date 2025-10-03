package io.github.christosmouzouris.urlshortener.controller;

import io.github.christosmouzouris.urlshortener.dto.*;
import io.github.christosmouzouris.urlshortener.mapper.ClickEventMapper;
import io.github.christosmouzouris.urlshortener.model.ClickEvent;
import io.github.christosmouzouris.urlshortener.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final ClickEventMapper clickEventMapper;

    public AnalyticsController(AnalyticsService analyticsService, ClickEventMapper clickEventMapper) {
        this.analyticsService = analyticsService;
        this.clickEventMapper = clickEventMapper;
    }

    @GetMapping
    public long getTotalClicks() {
        return analyticsService.getTotalClicks();
    }

    @GetMapping("/stats")
    public StatsResponseDto getStats() {
        return analyticsService.getStats();
    }

    @GetMapping("/by-browser")
    public List<ClicksByBrowserResponseDto> getTotalClicksByBrowser() {
        return analyticsService.getTotalClicksByBrowser();
    }

    @GetMapping("/{shortUrl}/by-browser")
    public List<ClicksByBrowserResponseDto> getTotalClicksByBrowserForUrl(@PathVariable String shortUrl) {
        return analyticsService.getTotalClicksByBrowserForUrl(shortUrl);
    }

    @GetMapping("/by-location")
    public List<ClicksByLocationResponseDto> getTotalClicksByLocation() {
        return analyticsService.getTotalClicksByLocation();
    }

    @GetMapping("/by-location/{location}")
    public List<ClickEventResponseDto> getAllClicksForLocation(@PathVariable String location) {
        List<ClickEvent> clickEvents = analyticsService.getAllClicksForLocation(location);
        return clickEventMapper.toResponseDtoList(clickEvents);
    }

    @GetMapping("/top-urls")
    public List<TopUrlsResponseDto> getTopUrls(@RequestParam(defaultValue = "10") int limit) {
        return analyticsService.getTopUrls(limit);
    }

    @GetMapping("/{shortUrl}/real-clicks")
    public List<ClickEventResponseDto> getRealClicks(@PathVariable String shortUrl) {
        List<ClickEvent> clickEvents = analyticsService.getRealClicks(shortUrl);
        return clickEventMapper.toResponseDtoList(clickEvents);
    }
}
