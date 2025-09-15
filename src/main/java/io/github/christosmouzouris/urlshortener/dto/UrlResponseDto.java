package io.github.christosmouzouris.urlshortener.dto;

public record UrlResponseDto(
        String shortUrl,
        String longUrl
) {}
