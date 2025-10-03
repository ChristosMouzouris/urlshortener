package io.github.christosmouzouris.urlshortener.dto;

public record StatsResponseDto(
        long clicks,
        long urls
) {}
