package io.github.christosmouzouris.urlshortener.controller;

import io.github.christosmouzouris.urlshortener.dto.ClickEventResponseDto;
import io.github.christosmouzouris.urlshortener.mapper.ClickEventMapper;
import io.github.christosmouzouris.urlshortener.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final ClickEventMapper clickEventMapper;

    public AnalyticsController(AnalyticsService analyticsService, ClickEventMapper clickEventMapper) {
        this.analyticsService = analyticsService;
        this.clickEventMapper = clickEventMapper;
    }

    @GetMapping("/analytics")
    public ClickEventResponseDto getAnalytics() {
        return null;
    }
}
