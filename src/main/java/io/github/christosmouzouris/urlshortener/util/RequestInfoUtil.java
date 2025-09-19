package io.github.christosmouzouris.urlshortener.util;

import io.github.christosmouzouris.urlshortener.model.ClientType;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ua_parser.Client;
import ua_parser.Parser;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Stateless utility class for gathering data to be used in analytics
 */
@Component
public class RequestInfoUtil {

    private final Parser uaParser;
    private final RestTemplate restTemplate;

    private static final Logger log =  LoggerFactory.getLogger(RequestInfoUtil.class);

    public RequestInfoUtil(Parser uaParser,  RestTemplate restTemplate) {
        this.uaParser = uaParser;
        this.restTemplate = restTemplate;
    }

    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_AGENT);
    }

    /**
     * Retrieves the real client IP from the request, considering null or empty string possibilities
     * and "X-Forwarded-For" headers for proxies
     *
     * @param request the HTTP request
     * @return the client's IP address or the default value from getRemoteAdd() if it cannot be determined
     */
    public String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * Classifies the client the user submitted a request with via the ClientType enum,
     * using the enum's static method to assess the user agent
     *
     * @param request the HTTP request
     * @return the client type
     */
    public ClientType getClient(HttpServletRequest request) {
        String uaString = getUserAgent(request);
        if (uaString == null || uaString.isBlank()) {
            return ClientType.UNKNOWN;
        }

        Client client = uaParser.parse(uaString);


        return ClientType.fromIdentifier(
                client.userAgent.family,
                client.device.family,
                uaString
        );
    }

    /**
     * Generates desired analytics for the user's locale when they submit a request
     * Gathering information like country code, city and region based on extra location details
     *
     * @param request the HTTP request
     * @return a GeoResult object containing fields like country code and other location details
     */
    public GeoResult getGeoResult(HttpServletRequest request) {
        String ip = getClientIP(request);

        Map<String, Object> geoRaw;
        String url = "https://ipapi.co/" + ip + "/json/";

        try {
            geoRaw = Optional.ofNullable(
                            restTemplate.getForObject(url, Map.class))
                    .map(m -> (Map<String, Object>) m)
                    .orElse(Collections.emptyMap());
        } catch (RestClientException e) {
            log.warn("Geolocation look up failed for IP {}: {}", ip, e.getMessage());
            geoRaw = Collections.emptyMap();
        }


        String countryCode = geoRaw.getOrDefault("country_code", "N/A").toString();

        Map<String, String> locationDetails = geoRaw.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() != null ? e.getValue().toString() : "N/A"
                ));

        return new GeoResult(countryCode, locationDetails);
    }
}
