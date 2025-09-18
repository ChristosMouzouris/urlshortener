package io.github.christosmouzouris.urlshortener.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ClickEventResponseDto(
        String shortUrl,
        LocalDateTime timestamp,
        String clientType,
        String countryCode,
        Map<String, String> locationDetails
) {}
