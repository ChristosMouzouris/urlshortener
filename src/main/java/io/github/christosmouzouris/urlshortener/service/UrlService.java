package io.github.christosmouzouris.urlshortener.service;

import io.github.christosmouzouris.urlshortener.exception.UrlNotFoundException;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.repository.UrlRepository;
import io.github.christosmouzouris.urlshortener.util.HashidsUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for managing shortened URLs
 * Handles creation of new URLs, resolves existing ones and
 * updates access metadata
 */
@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final HashidsUtil hashidsUtil;

    public UrlService(UrlRepository urlRepository, HashidsUtil hashidsUtil) {
        this.urlRepository = urlRepository;
        this.hashidsUtil = hashidsUtil;
    }

    /**
     * Generates shortened URL for the given entity
     * If the long URL already exists, returns the existing shortened version
     * Otherwise, creates a new entry, generates a short key, and saves it.
     *
     * @param entity Url entity containing the original URL
     * @return the persisted URL entity with shortUrl set
     */
    public Url shortenUrl(Url entity) {

        // Check if long URL already exists and return shortUrl associated with it if yes
        Url existing = urlRepository.findByLongUrl(entity.getLongUrl()).orElse(null);
        if (existing != null) {
            existing.setLastAccessedDate(LocalDateTime.now());
            return urlRepository.save(existing);
        }

        entity.setCreationDate(LocalDateTime.now());
        entity.setLastAccessedDate(LocalDateTime.now());

        urlRepository.save(entity);

        String shortUrl = hashidsUtil.encodeId(entity.getId());
        entity.setShortUrl(shortUrl);

        return urlRepository.save(entity);
    }

    /**
     * Retrieves a URL entity by its shortURL segment
     *
     * @param shortUrl The short Url segment to look up
     * @return the Url entity if found
     * @throws UrlNotFoundException if no URL is associated with the given shortUrl
     */
    public Url getUrlByShortUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortUrl));
    }

    /**
     * Retrieves and updates URL entity when accessed
     * Updates last accessed time stamp and click counter
     *
     * @param shortUrl The short URL segment to look up
     * @return the updated and persisted URL entity
     * @throws UrlNotFoundException if no URL is associated with the given shortUrl
     */
    public Url accessUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortUrl));

        url.setLastAccessedDate(LocalDateTime.now());
        // Update clicks
        return urlRepository.save(url);
    }

    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }
}
