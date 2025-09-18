package io.github.christosmouzouris.urlshortener.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entity representing a single click on a shortened URL
 * Each click stores metadata that can be used for analytics
 */
@Entity
@Table(
        name = "click_event",
        indexes = {
            @Index(name = "idx_clickEvent_shortUrlId", columnList = "short_url_id"),
            @Index(name = "idx_clickEvent_countryCode", columnList = "country_code")
        }
)
public class ClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "short_url_id", nullable = false)
    private Url url;

    // Raw FK view (read only)
    @Column(name = "short_url_id", insertable = false, updatable = false)
    private Long shortUrlId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientType clientType;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> locationDetails;

    // Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Url getUrl() {return url;}
    public void setUrl(Url url) {this.url = url;}

    public Long getShortUrlId() {return shortUrlId;}

    public LocalDateTime getTimestamp() {return timestamp;}
    public void setTimestamp(LocalDateTime timestamp) {this.timestamp = timestamp;}

    public ClientType getClientType() {return clientType;}
    public void setClientType(ClientType clientType) {this.clientType = clientType;}

    public String getCountryCode() {return countryCode;}
    public void setCountryCode(String countryCode) {this.countryCode = countryCode;}

    public Map<String, String> getLocationDetails() {return locationDetails;}
    public void setLocationDetails(Map<String, String> locationDetails) {this.locationDetails = locationDetails;}
}
