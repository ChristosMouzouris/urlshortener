package io.github.christosmouzouris.urlshortener.mapper;

import io.github.christosmouzouris.urlshortener.dto.UrlRequestDto;
import io.github.christosmouzouris.urlshortener.dto.UrlResponseDto;
import io.github.christosmouzouris.urlshortener.model.Url;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UrlMapperImpl implements UrlMapper {

    @Override
    public Url toEntity(UrlRequestDto dto) {
        Url url = new Url();
        url.setLongUrl(dto.longUrl()); // All other attributes are set in the service

        return url;
    }

    @Override
    public UrlResponseDto toResponseDto(Url entity) {
        return new UrlResponseDto(
                entity.getShortUrl(),
                entity.getLongUrl()
        );
    }

    public List<UrlResponseDto> toResponseDtoList(List<Url> urls) {
        return urls.stream()
                .map(url -> new UrlResponseDto(url.getShortUrl(), url.getLongUrl()))
                .collect(Collectors.toList());
    }
}
