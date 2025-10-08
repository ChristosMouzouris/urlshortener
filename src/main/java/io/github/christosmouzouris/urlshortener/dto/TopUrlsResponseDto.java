package io.github.christosmouzouris.urlshortener.dto;

import java.net.URI;
import java.net.URISyntaxException;

public record TopUrlsResponseDto(
        long clicks,
        String shortUrl,
        String domain
) {
    public TopUrlsResponseDto {
        domain = extractDomain(domain);
    }

    private static String extractDomain(String longUrl) {
        if (longUrl == null || longUrl.isEmpty()) {
            return "";
        }

        try {
            URI uri = new URI(longUrl);
            String host = uri.getHost();
            return host != null ? host : longUrl.split("/")[0];
        } catch (URISyntaxException e) {
            return longUrl.split("/")[0];
        }
    }
}
