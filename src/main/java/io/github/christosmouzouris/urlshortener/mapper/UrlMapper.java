package io.github.christosmouzouris.urlshortener.mapper;

import io.github.christosmouzouris.urlshortener.dto.UrlRequestDto;
import io.github.christosmouzouris.urlshortener.dto.UrlResponseDto;
import io.github.christosmouzouris.urlshortener.model.Url;

import java.util.List;


public interface UrlMapper {

    Url toEntity(UrlRequestDto dto);

    UrlResponseDto toResponseDto(Url entity);

    List<UrlResponseDto> toResponseDtoList(List<Url> urls);
}
