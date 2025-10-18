package io.github.christosmouzouris.urlshortener.service;

import io.github.christosmouzouris.urlshortener.exception.UrlNotFoundException;
import io.github.christosmouzouris.urlshortener.exception.UrlUpdateFailedException;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.repository.UrlRepository;
import io.github.christosmouzouris.urlshortener.util.HashidsUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTests {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private HashidsUtil hashIdsUtil;

    @InjectMocks
    private UrlService urlService;

    @Test
    public void shouldReturnExistingUrlIfLongUrlAlreadyExists(){
        // Given: a new Url object with a long Url that already exists in the repository
        Url existingUrl = new Url();
        existingUrl.setId(1L);
        existingUrl.setShortUrl("short");
        existingUrl.setLongUrl("https://urlshortener.com");

        when(urlRepository.findByLongUrl("https://urlshortener.com"))
                .thenReturn(Optional.of(existingUrl));

        // When: the shortenUrl method is called with the duplicate long Url
        Url result = urlService.shortenUrl(existingUrl);

        // Then: the existing repository entry with the matching long Url is returned
        assertThat(result).isEqualTo(existingUrl);
        // And:  the last accessed date is updated
        assertThat(result.getLastAccessedDate()).isNotNull();
        // And: the urlRepository save method was called once
        verify(urlRepository, times(0)).save(existingUrl);
    }

    @Test
    public void shouldReturnNewUrlIfLongUrlDoesNotExist(){
        // Given: new Url object with a long Url that does not exist in the repository
        Url newUrl = new Url();
        newUrl.setLongUrl("https://urlshortener.com");

        when(urlRepository.findByLongUrl("https://urlshortener.com"))
                .thenReturn(Optional.empty());

        when(hashIdsUtil.encodeId(2L))
                .thenReturn("short");

        when(urlRepository.saveAndFlush(any(Url.class)))
                .thenAnswer(invocation -> {
                    Url saved =  invocation.getArgument(0);
                    if (saved.getId() == null) {
                        saved.setId(2L);
                    }
                    return saved;
                });

        // When: the shortenUrl method is called with the new long Url
        Url result = urlService.shortenUrl(newUrl);

        // Then: the new repository entry is returned with a new short Url
        assertThat(result).isEqualTo(newUrl);
        assertThat(result.getShortUrl()).isEqualTo("short");
        // And: the creation date and last accessed date are se
        assertThat(result.getCreationDate()).isNotNull();
        assertThat(result.getLastAccessedDate()).isNotNull();
        // And: the urlRepository save method was called twice
        verify(urlRepository, times(1)).saveAndFlush(any(Url.class));
    }

    @Test
    public void shouldUpdateUrlDateAndReturnUrlIfUrlExists() {
        // Given: a new Url object with a short Url that exists in the repository
        Url url = new Url();
        url.setId(1L);
        url.setShortUrl("short");
        url.setLongUrl("https://urlshortener.com");
        url.setLastAccessedDate(LocalDateTime.now().minusDays(1));

        LocalDateTime oldDate = url.getLastAccessedDate();

        when(urlRepository.findByShortUrl(url.getShortUrl()))
                .thenReturn(Optional.of(url));

        when(urlRepository.updateLastAccessedDate(eq(url.getShortUrl()), any(LocalDateTime.class)))
                .thenReturn(1);

        // When: the access Url method is called with the existing short Url
        Url result = urlService.accessUrl(url.getShortUrl());

        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(urlRepository).updateLastAccessedDate(eq(url.getShortUrl()), captor.capture());

        // Then: the existing repository entry is returned
        assertThat(result).isEqualTo(url);
        // And: the last accessed date was updated
        assertThat(result.getLastAccessedDate()).isNotNull();
        LocalDateTime updatedTimestamp = captor.getValue();
        assertThat(updatedTimestamp).isAfter(oldDate);
    }

    @Test
    public void shouldThrowErrorIfUrlDoesNotExist() {
        // Given: a short Url to find by
        String shortUrl = "short";

        when(urlRepository.findByShortUrl(shortUrl))
                .thenReturn(Optional.empty());

        // When: the access Url method is called with a short Url that does not exist
        // Then: a UrlNotFoundException is thrown with the correct message
        assertThatThrownBy(() -> urlService.accessUrl(shortUrl))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessage("Url not found: " + shortUrl);

        // And: the updatedLastAccessedDate repository method is never called
        verify(urlRepository, never()).updateLastAccessedDate(anyString(), any());
    }

    @Test
    public void shouldThrowErrorIfUrlExistsButUpdateFailed() {
        // Given: a new Url object with a short Url that exists in the repository
        Url url = new Url();
        url.setId(1L);
        url.setShortUrl("short");
        url.setLongUrl("https://urlshortener.com");
        url.setLastAccessedDate(LocalDateTime.now());

        when(urlRepository.findByShortUrl(url.getShortUrl()))
                .thenReturn(Optional.of(url));
        when(urlRepository.updateLastAccessedDate(eq(url.getShortUrl()), any()))
                .thenReturn(0);

        // When: The access Url method is called with an existing url but date update fails
        // Then: a UrlUpdateFailedException is thrown
        assertThatThrownBy(() -> urlService.accessUrl(url.getShortUrl()))
                .isInstanceOf(UrlUpdateFailedException.class)
                .hasMessage("Url: " + url.getShortUrl() + " was found but some concurrent operation or DB " +
                        "prevented the update");

        // And: the updateLastAccessedDate repository method is called once
        verify(urlRepository, times(1)).updateLastAccessedDate(eq("short"), any());
    }
}
