package io.github.christosmouzouris.urlshortener.UtilTests;

import io.github.christosmouzouris.urlshortener.model.ClientType;
import io.github.christosmouzouris.urlshortener.util.RequestInfoUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua_parser.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestsInfoUtilTests {

    @Mock
    private Parser uaParser;

    @InjectMocks
    private RequestInfoUtil requestInfoUtil;


    @ParameterizedTest
    @CsvSource({
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0, Chrome, Windows, Other, CHROME",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) Firefox/110.0, Firefox, Mac OS X, Other, FIREFOX",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) Safari/16.0, Safari, iOS, iPhone, SAFARI",
            "' ', , , , UNKNOWN",
            "null, null, null, null, UNKNOWN"
    })
    public void shouldReturnCorrectClientFromUserAgent(
            String userAgentHeader,
            String uaFamily,
            String osFamily,
            String deviceFamily,
            String expectedClientTypeStr
    ) {
        // Given:
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent"))
                .thenReturn(nullStringToNull(userAgentHeader));

        if (uaFamily != null && !uaFamily.isBlank()) {
            Client client = createClient(uaFamily, osFamily, deviceFamily);
            lenient().when(uaParser.parse(anyString()))
                    .thenReturn(client);
        }

        // When:
        ClientType result = requestInfoUtil.getClient(request);

        // Then:
        assertThat(result.name()).isEqualTo(expectedClientTypeStr);
    }



    // Private helper methods
    private Client createClient(String uaFamily, String osFamily, String deviceFamily) {
        UserAgent ua = new UserAgent(nullStringToNull(uaFamily), null, null, null);
        OS os = new OS(nullStringToNull(osFamily), null, null, null, null);
        Device device = new Device(nullStringToNull(deviceFamily));

        return new Client(ua, os, device);
    }

    private String nullStringToNull(String str) {
        return str.equals("null") ? null : str;
    }
}
