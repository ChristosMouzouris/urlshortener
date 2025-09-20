package io.github.christosmouzouris.urlshortener.mapper;

public interface ClicksByLocationProjection {
    Long getCount();
    String getCountryCode();
}
