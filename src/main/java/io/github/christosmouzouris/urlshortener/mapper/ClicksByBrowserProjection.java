package io.github.christosmouzouris.urlshortener.mapper;

public interface ClicksByBrowserProjection {
    Long getCount();
    String getClientType();
}
