package com.wanshi.library.e2e.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseE2ETest {

    protected WebDriver driver;

    protected WebDriverWait wait;

    @LocalServerPort
    protected int port;

    protected String baseUrl;

    @BeforeEach
    void setup() {

        WebDriverManager.chromedriver().setup();

        //  ChromeOptions
        ChromeOptions options = new ChromeOptions();
        // Jenkins / CI
        options.addArguments("--headless=new");
        // Linux CI
        options.addArguments("--no-sandbox");
        // Docker / CI memory fix
        options.addArguments("--disable-dev-shm-usage");
        // GPU
        options.addArguments("--disable-gpu");
        // window size for headless
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void teardown() {

        if (driver != null) {
            driver.quit();
        }
    }
}