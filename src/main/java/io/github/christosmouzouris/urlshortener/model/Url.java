package io.github.christosmouzouris.urlshortener.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "url",
        indexes = {
                @Index(name = "idx_shortUrl", columnList = "shortUrl"),
                @Index(name = "idx_longUrl", columnList = "longUrl")
        }
)
public class Url {

    @Id
    @SequenceGenerator(
            name = "url_seq_gen",
            sequenceName = "url_seq",
            allocationSize = 50
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "url_seq_gen"
    )
    private Long id;

    @Column(nullable = true, unique = true)
    private String shortUrl;

    @Column(nullable = false, unique = true)
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
