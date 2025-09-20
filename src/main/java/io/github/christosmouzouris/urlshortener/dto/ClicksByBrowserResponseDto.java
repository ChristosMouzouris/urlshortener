package io.github.christosmouzouris.urlshortener.dto;

public record ClicksByBrowserResponseDto(
        long clicks,
        String browser
) {}
