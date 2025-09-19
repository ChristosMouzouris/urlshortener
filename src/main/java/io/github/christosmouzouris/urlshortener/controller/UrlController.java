package io.github.christosmouzouris.urlshortener.controller;

import io.github.christosmouzouris.urlshortener.dto.UrlRequestDto;
import io.github.christosmouzouris.urlshortener.dto.UrlResponseDto;
import io.github.christosmouzouris.urlshortener.mapper.UrlMapper;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.service.UrlService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService urlService;
    private final UrlMapper urlMapper;

    public UrlController(UrlService urlService, UrlMapper urlMapper) {
        this.urlService = urlService;
        this.urlMapper = urlMapper;
    }

    @PostMapping
    public UrlResponseDto createShortUrl(@RequestBody UrlRequestDto urlRequestDto) {
        Url entity = urlMapper.toEntity(urlRequestDto);
        Url saved = urlService.shortenUrl(entity);
        return urlMapper.toResponseDto(saved);
    }

    @GetMapping("/{shortUrl}")
    public UrlResponseDto getShortUrl(@PathVariable String shortUrl){
        Url entity = urlService.accessUrl(shortUrl);
        return urlMapper.toResponseDto(entity);
    }

    @GetMapping
    public List<UrlResponseDto> getAllUrls() {
        List<Url> urls = urlService.getAllUrls();
        return urlMapper.toResponseDtoList(urls);
    }
}
