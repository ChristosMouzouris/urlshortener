package io.github.christosmouzouris.urlshortener.dto;

import java.time.LocalDate;

public record ClicksTrendResponseDto(
        LocalDate date,
        long clicks
) {}
