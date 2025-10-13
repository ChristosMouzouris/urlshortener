package io.github.christosmouzouris.urlshortener.config;

import io.github.christosmouzouris.urlshortener.model.ClickEvent;
import io.github.christosmouzouris.urlshortener.model.ClientType;
import io.github.christosmouzouris.urlshortener.model.Url;
import io.github.christosmouzouris.urlshortener.repository.ClickEventRepository;
import io.github.christosmouzouris.urlshortener.repository.UrlRepository;
import io.github.christosmouzouris.urlshortener.util.HashidsUtil;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Component
@Profile("demo")
public class DemoDataSeeder {
    public static final List<ClientType> BROWSERS = List.of(
            ClientType.CHROME,
            ClientType.FIREFOX,
            ClientType.EDGE,
            ClientType.BRAVE
    );
    public static final List<String> COUNTRIES = List.of("GB", "US", "DE", "FR", "CA");
    public static final List<String> TARGET_URLS = List.of(
            "https://github.com/ChristosMouzouris/urlshortener",
            "https://github.com/ChristosMouzouris/FinanceWebAppPublic",
            "https://spring.io",
            "https://react.dev",
            "https://docker.com",
            "https://www.google.com/"
    );

    @Bean
    public CommandLineRunner seedDemoData(
            UrlRepository urlRepository,
            ClickEventRepository clickEventRepository,
            HashidsUtil hashidsUtil
    ) {
        return args -> {
            if (urlRepository.count() > 0) {
                System.out.println("Demo data seeding skipped — existing data found.");
                return;
            }

            System.out.println("Seeding demo data...");

            List<Url> urls = IntStream.range(0, TARGET_URLS.size())
                    .mapToObj(i -> {
                        Url url = new Url();
                        url.setLongUrl(TARGET_URLS.get(i));
                        url.setCreationDate(LocalDateTime.now());
                        url.setLastAccessedDate(LocalDateTime.now());
                        url.setShortUrl(hashidsUtil.encodeId(i + 1000));

                        return url;
                    })
                    .toList();

            urlRepository.saveAll(urls);

            LocalDateTime now = LocalDateTime.now();
            int totalDays = 20;
            List<ClickEvent> allEvents = new ArrayList<>();

            for (Url url : urls) {
                for (int dayOffset = totalDays; dayOffset >= 0; dayOffset--) {
                    int clicksForDay = ThreadLocalRandom.current().nextInt(5, 20 + (totalDays - dayOffset) * 5);
                    LocalDateTime dayStart = now.minusDays(dayOffset).withHour(0).withMinute(0).withSecond(0);

                    for (int i = 0; i < clicksForDay; i++) {
                        ClickEvent clickEvent = new ClickEvent();
                        clickEvent.setUrl(url);
                        clickEvent.setTimestamp(randomTimestamp(dayStart, dayStart.plusDays(1)));
                        clickEvent.setClientType(random(BROWSERS));
                        clickEvent.setCountryCode(random(COUNTRIES));
                        allEvents.add(clickEvent);
                    }
                }
            }

            clickEventRepository.saveAll(allEvents);
            System.out.printf("Seeded %d URLs and %d click events.%n", urls.size(), allEvents.size());
        };
    }

    private static <T> T random(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    private static LocalDateTime randomTimestamp(LocalDateTime start, LocalDateTime end) {
        long startSeconds = start.toEpochSecond(java.time.ZoneOffset.UTC);
        long endSeconds = end.toEpochSecond(java.time.ZoneOffset.UTC);
        long random = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds);
        return LocalDateTime.ofEpochSecond(random, 0, java.time.ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
    }
}
