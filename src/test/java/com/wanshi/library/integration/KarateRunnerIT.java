package com.wanshi.library.integration;

import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class KarateRunnerIT {
    @LocalServerPort
    int port;

    @Karate.Test
    Karate testAdminUsers() {

        return Karate.run("classpath:karate")
                .systemProperty("karate.port", String.valueOf(port));
    }
}