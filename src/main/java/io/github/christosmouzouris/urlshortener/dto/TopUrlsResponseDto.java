package io.github.christosmouzouris.urlshortener.dto;

public record TopUrlsResponseDto(
        long clicks,
        String shortUrl
) {}
