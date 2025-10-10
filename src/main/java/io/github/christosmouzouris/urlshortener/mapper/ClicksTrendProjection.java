package io.github.christosmouzouris.urlshortener.mapper;

import java.time.LocalDate;

public interface ClicksTrendProjection {
    LocalDate getDate();
    long getClicks();
}
