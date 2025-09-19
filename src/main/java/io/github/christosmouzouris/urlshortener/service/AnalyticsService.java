package io.github.christosmouzouris.urlshortener.service;

import io.github.christosmouzouris.urlshortener.model.ClickEvent;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.repository.ClickEventRepository;
import io.github.christosmouzouris.urlshortener.util.GeoResult;
import io.github.christosmouzouris.urlshortener.util.RequestInfoUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Request;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service layer for managing analytics for short URLs
 * Handles collection of data based on short URL used and user agent
 */
@Service
public class AnalyticsService {

    private final ClickEventRepository clickEventRepository;
    private final RequestInfoUtil requestInfoUtil;

    public AnalyticsService(ClickEventRepository clickEventRepository,  RequestInfoUtil requestInfoUtil) {
        this.clickEventRepository = clickEventRepository;
        this.requestInfoUtil = requestInfoUtil;
    }

    /**
     * Handles the collection of all requires analytics for internal use such as,
     * short URL segment, time stamp, client type, country code and any extra location details
     *
     * @param url the URL entity that was created when the request was submitted
     * @param request the HTTP request
     */
    public void handleClickEvent(Url url, HttpServletRequest request) {
        ClickEvent clickEvent = new ClickEvent();

        clickEvent.setUrl(url);
        clickEvent.setTimestamp(LocalDateTime.now());
        clickEvent.setClientType(requestInfoUtil.getClient(request));

        GeoResult geoResult = requestInfoUtil.getGeoResult(request);
        clickEvent.setCountryCode(geoResult.getCountryCode());
        clickEvent.setLocationDetails(geoResult.getLocationDetails());

        clickEventRepository.save(clickEvent);
    }
}
