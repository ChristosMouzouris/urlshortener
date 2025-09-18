package io.github.christosmouzouris.urlshortener.mapper;


import io.github.christosmouzouris.urlshortener.dto.ClickEventResponseDto;
import io.github.christosmouzouris.urlshortener.model.ClickEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClickEventMapperImpl implements ClickEventMapper {

    @Override
    public ClickEventResponseDto toResponseDto(ClickEvent clickEvent) {
        return new ClickEventResponseDto(
                clickEvent.getUrl() != null ? clickEvent.getUrl().getShortUrl() : null,
                clickEvent.getTimestamp(),
                clickEvent.getClientType() != null ? clickEvent.getClientType().name() : null,
                clickEvent.getCountryCode(),
                clickEvent.getLocationDetails()
        );
    }

    @Override
    public List<ClickEventResponseDto> toResponseDtoList(List<ClickEvent> clickEvents) {
        return clickEvents.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
