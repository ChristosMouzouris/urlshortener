package io.github.christosmouzouris.urlshortener.model;

/**
 * Type of client that triggered the click event
 * Useful in classifying analytics
 */
public enum ClientType {
    CHROME,
    FIREFOX,
    BRAVE,
    SAFARI,
    EDGE,
    MOBILE_APP,
    OTHER
}
