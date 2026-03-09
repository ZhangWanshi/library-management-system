package com.wanshi.library.e2e.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AuthHelper {

    public static void login(
            WebDriver driver,
            WebDriverWait wait,
            String baseUrl,
            String username,
            String password) {

        driver.get(baseUrl);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));

        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(username);

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.cssSelector("button")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar")));
    }

    public static void loginAsAdmin(
            WebDriver driver,
            WebDriverWait wait,
            String baseUrl) {

        login(driver, wait, baseUrl, "admin", "admin");
    }

    public static void loginAsLibrarian(
            WebDriver driver,
            WebDriverWait wait,
            String baseUrl) {

        login(driver, wait, baseUrl, "librarian", "librarian");
    }

    public static void loginAsMember(
            WebDriver driver,
            WebDriverWait wait,
            String baseUrl) {

        login(driver, wait, baseUrl, "member", "member");
    }

    public static void logout(WebDriver driver, WebDriverWait wait) {

        driver.findElement(By.id("logoutBtn")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
    }
}