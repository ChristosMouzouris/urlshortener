package io.github.christosmouzouris.urlshortener.service;

import io.github.christosmouzouris.urlshortener.exception.UrlNotFoundException;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.repository.UrlRepository;
import io.github.christosmouzouris.urlshortener.util.HashidsUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final HashidsUtil hashidsUtil;

    public UrlService(UrlRepository urlRepository, HashidsUtil hashidsUtil) {
        this.urlRepository = urlRepository;
        this.hashidsUtil = hashidsUtil;
    }

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

    public Url getUrlByShortUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortUrl));
    }

    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }
}
