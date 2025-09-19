package io.github.christosmouzouris.urlshortener.model;

import java.util.Arrays;

/**
 * Type of client that triggered the click event
 * Useful in classifying analytics
 */
public enum ClientType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    BRAVE("brave"),
    SAFARI("safari"),
    EDGE("edge"),
    BOT("bot", "spider", "crawler"),
    UNKNOWN();

    private final String[] identifiers;

    ClientType(String... identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * Maps a user agent string to a corresponding ClientType
     * Only browsers and bots are considered, no mobile app
     *
     * @param uaFamily the browser family extracted from the user agent
     * @param device the device family extracted from the user agent
     * @param uaString the full raw user agent string from the HTTP request
     * @return the corresponding ClientType enum value or UNKNOWN if no match is found
     */
    public static ClientType fromIdentifier(String uaFamily, String device, String uaString) {
        String all = (uaFamily + " " + device + " " + uaString).toLowerCase();

        return Arrays.stream(values())
                .filter(clientType -> Arrays.stream(clientType.identifiers)
                .anyMatch(all::contains))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
