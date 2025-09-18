package io.github.christosmouzouris.urlshortener.mapper;

import io.github.christosmouzouris.urlshortener.dto.ClickEventResponseDto;
import io.github.christosmouzouris.urlshortener.model.ClickEvent;

import java.util.List;

public interface ClickEventMapper {

    ClickEventResponseDto toResponseDto(ClickEvent clickEvent);

    List<ClickEventResponseDto> toResponseDtoList(List<ClickEvent> clickEvents);
}
