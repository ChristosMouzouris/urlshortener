package io.github.christosmouzouris.urlshortener.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record UrlRequestDto(
        @NotBlank(message = "URL must not be empty.")
        @Pattern(
                regexp = "^(https?://)([\\w.-]+)(:[0-9]+)?(/.*)?$",
                message = "URL is not valid"
        )
        String longUrl
) {}
