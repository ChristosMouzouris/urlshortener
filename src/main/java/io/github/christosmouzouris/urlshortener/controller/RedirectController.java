package io.github.christosmouzouris.urlshortener.controller;


import io.github.christosmouzouris.urlshortener.exception.UrlNotFoundException;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;

@Controller
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Handles GET requests to redirect based on the given short URL segment
     * Looks up the original URL using the short URL segment and updates access metadata
     * Responds with an HTTP 302 redirect to the original URL
     *
     * @param shortUrl the short URL segment to identify the orignal URL
     * @return a ResponseEntity with HTTP status 302 and Location header set to the original URL
     * @throws UrlNotFoundException if the short URL does not exist in the database
     */
    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> getUrl(@PathVariable String shortUrl){
        Url entity = urlService.accessUrl(shortUrl);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(entity.getLongUrl()))
                .build();
    }
}
