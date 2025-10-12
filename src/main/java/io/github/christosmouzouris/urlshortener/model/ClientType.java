package io.github.christosmouzouris.urlshortener.model;

import java.util.Arrays;

/**
 * Type of client that triggered the click event
 * Useful in classifying analytics
 */
public enum ClientType {
    BRAVE("brave"),
    CHROME("chrome"),
    FIREFOX("firefox"),
    SAFARI("safari"),
    EDGE("edge"),
    BOT(
            "bot", "spider", "crawler", "crawl", "slurp", "fetch",
            "python", "scrapy", "httpclient", "java", "curl", "wget",
            "facebookexternalhit", "bingpreview", "google", "yandex", "baiduspider",
            "duckduckbot", "sogou", "mj12bot", "ahrefsbot", "semrushbot",
            "linkedinbot", "embedly", "pinterest", "bitlybot", "twitterbot",
            "discordbot", "slackbot", "telegrambot"
    ),
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
