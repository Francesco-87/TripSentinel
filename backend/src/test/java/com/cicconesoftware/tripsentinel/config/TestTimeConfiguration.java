package com.cicconesoftware.tripsentinel.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/** Keeps time-dependent integration tests stable regardless of when they run. */
@TestConfiguration(proxyBeanMethods = false)
public class TestTimeConfiguration {

    @Bean
    @Primary
    Clock testClock() {
        return Clock.fixed(Instant.parse("2026-09-05T00:00:00Z"), ZoneOffset.UTC);
    }
}
