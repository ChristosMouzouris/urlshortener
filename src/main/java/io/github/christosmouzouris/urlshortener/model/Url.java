package io.github.christosmouzouris.urlshortener.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "url",
        indexes = {
                @Index(name = "idx_shortUrl", columnList = "shortUrl")
        }
)

public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String shortUrl;

    @Column(nullable = false)
    private String longUrl;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private LocalDateTime lastAccessedDate;

    // Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getShortUrl() {return shortUrl;}
    public void setShortUrl(String shortUrl) {this.shortUrl = shortUrl;}

    public String getLongUrl() {return longUrl;}
    public void setLongUrl(String longUrl) {this.longUrl = longUrl;}

    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}

    public LocalDateTime getLastAccessedDate() {return lastAccessedDate;}
    public void setLastAccessedDate(LocalDateTime lastAccessedDate) {this.lastAccessedDate = lastAccessedDate;}
}
