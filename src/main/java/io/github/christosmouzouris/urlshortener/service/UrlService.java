package io.github.christosmouzouris.urlshortener.service;

import io.github.christosmouzouris.urlshortener.exception.UrlNotFoundException;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url shortenUrl(Url entity) {
        // Generate short code
        // Build full shortUrl
        entity.setLongUrl("asdasd");
        entity.setShortUrl("asdasd");
        entity.setCreationDate(LocalDateTime.now());
        entity.setLastAccessedDate(LocalDateTime.now());

        return urlRepository.save(entity);
    }

    public Url getUrlByShortUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortUrl));
    }

//    public Url getUrlByLongUrl(String longUrl){
//        return urlRepository.findByLongUrl(longUrl)
//                .orElseThrow(() -> new UrlNotFoundException(longUrl));
//    }

    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }
}
