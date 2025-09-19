package io.github.christosmouzouris.urlshortener.util;

import io.github.cdimascio.dotenv.Dotenv;
import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HashidsUtil {

    private final Hashids hashids;
    private final long offset;

    private static final int DEFAULT_MIN_LENGTH = 6;
    private static final long DEFAULT_OFFSET = 100_000L;

    private static final Logger log = LoggerFactory.getLogger(HashidsUtil.class);

    public HashidsUtil() {
        Dotenv dotenv = Dotenv.load();

        String salt =  dotenv.get("HASHIDS_SALT");
        if (salt == null || salt.isEmpty()) {
            throw new IllegalStateException("HASHIDS_SALT must be set in .env");
        }

        int minLength = DEFAULT_MIN_LENGTH;
        String minLengthStr = dotenv.get("HASHIDS_MIN_LENGTH");
        if (minLengthStr != null) {
            try {
                minLength = Integer.parseInt(minLengthStr);
            } catch (NumberFormatException e) {
                log.warn("Unable to load HASHIDS_MIN_LENGTH from dotenv, using default: " + DEFAULT_MIN_LENGTH);
            }
        }

        long idOffset = DEFAULT_OFFSET;
        String offsetStr = dotenv.get("ID_OFFSET");
        if (offsetStr != null) {
            try {
                idOffset = Long.parseLong(offsetStr);
            } catch (NumberFormatException e) {
                // LOG
                log.warn("Unable to load ID_OFFSET from dotenv, using default: " + DEFAULT_OFFSET);
            }
        }

        this.hashids = new Hashids(salt, minLength);
        this.offset = idOffset;
    }

    public String encodeId(long id) {
        return hashids.encode(id + offset);
    }
}
