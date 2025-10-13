package io.github.christosmouzouris.urlshortener.service;

import io.github.christosmouzouris.urlshortener.exception.UrlNotFoundException;
import io.github.christosmouzouris.urlshortener.exception.UrlUpdateFailedException;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.repository.UrlRepository;
import io.github.christosmouzouris.urlshortener.util.HashidsUtil;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
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
    @Transactional
    public Url shortenUrl(Url entity) {

        // Check if long URL already exists and return shortUrl associated with it if yes
        Url existing = urlRepository.findByLongUrl(entity.getLongUrl()).orElse(null);
        if (existing != null) {
            existing.setLastAccessedDate(LocalDateTime.now());
            return existing;
        }

        entity.setCreationDate(LocalDateTime.now());
        entity.setLastAccessedDate(LocalDateTime.now());

        try {
            urlRepository.saveAndFlush(entity); // ensures ID is generated
            entity.setShortUrl(hashidsUtil.encodeId(entity.getId()));
        } catch (DataIntegrityViolationException e) {
            existing = urlRepository.findByLongUrl(entity.getLongUrl())
                    .orElseThrow(() -> new RuntimeException("Unexpected DB state"));
            existing.setLastAccessedDate(LocalDateTime.now());
            return existing;
        }

        return entity;
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
     * Updates last accessed time stamp
     *
     * @param shortUrl The short URL segment to look up
     * @return the updated URL entity
     * @throws UrlNotFoundException if no URL is associated with the given shortUrl
     * @throws UrlUpdateFailedException if the update of the last accessed date failed in the DB
     */
    @Transactional
    public Url accessUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortUrl));

        updateLastAccessedDate(shortUrl);
        return url;
    }

    @Async("taskExecutor")
    public void updateLastAccessedDate(String shortUrl) {
        int updatedRows = urlRepository.updateLastAccessedDate(shortUrl, LocalDateTime.now());
        if (updatedRows == 0) {
            throw new UrlUpdateFailedException(shortUrl);
        }
    }

    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }
}
