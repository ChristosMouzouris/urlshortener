package io.github.christosmouzouris.urlshortener.dto;

public record ClicksByLocationResponseDto(
        long clicks,
        String location
) {}
