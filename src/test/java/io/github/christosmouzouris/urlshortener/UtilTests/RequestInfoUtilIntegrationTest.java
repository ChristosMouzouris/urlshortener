package io.github.christosmouzouris.urlshortener.UtilTests;

import io.github.christosmouzouris.urlshortener.model.ClientType;
import io.github.christosmouzouris.urlshortener.util.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RequestInfoUtilIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RequestInfoUtil requestInfoUtil;


    @ParameterizedTest
    @CsvSource({
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0, CHROME",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) Firefox/110.0, FIREFOX",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) Safari/16.0, SAFARI",
            "' ', UNKNOWN",
            "null, UNKNOWN"
    })
    public void shouldParseRealUserAgentsCorrectly(String uaString, String expectedClientType) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent")).thenReturn(nullStringToNull(uaString));


        ClientType result = requestInfoUtil.getClient(request);

        assertThat(result.name()).isEqualTo(expectedClientType);
    }

    @Test
    public void shouldReturnGeoResult() {
        // Given:
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr())
                .thenReturn("8.8.8.8");

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String fakeResponse = """
                {
                    "country_code": "UK",
                    "lat": "51.5",
                    "lon": "-0.1"
                }
                """;

        mockServer.expect(requestTo("https://ipapi.co/8.8.8.8/json/"))
                .andRespond(withSuccess(fakeResponse, MediaType.APPLICATION_JSON));

        // When:
        GeoResult result = requestInfoUtil.getGeoResult(request);

        // Then:
        assertThat(result.getCountryCode()).isEqualTo("UK");
        assertThat(result.getLocationDetails())
                .containsEntry("lat", "51.5")
                .containsEntry("lon", "-0.1");
    }

    @Test
    public void restTemplateCreated() {
        assertThat(restTemplate).isNotNull();
    }

    @Test
    public void contextLoads() {
        System.out.println("Spring context active");
        assertTrue(true);
    }

    // Helper method
    private String nullStringToNull(String str) {
        return str.equals("null") ? null : str;
    }
}
