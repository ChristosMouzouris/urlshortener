package io.github.christosmouzouris.urlshortener.mapper;

public interface TopUrlsProjection {
    long getCount();
    String getShortUrl();
    String getLongUrl();
}
