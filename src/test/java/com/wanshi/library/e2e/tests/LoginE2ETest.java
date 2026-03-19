package com.wanshi.library.e2e.tests;

import com.wanshi.library.e2e.base.BaseE2ETest;
import com.wanshi.library.e2e.util.AuthHelper;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginE2ETest extends BaseE2ETest {

    @Test
    void adminLoginWorks() {

        AuthHelper.loginAsAdmin(driver, wait, baseUrl);

        assertTrue(driver.findElement(By.id("sidebar")).isDisplayed());
    }

    @Test
    void librarianLoginWorks() {

        AuthHelper.loginAsLibrarian(driver, wait, baseUrl);

        assertTrue(driver.findElement(By.id("sidebar")).isDisplayed());
    }

    @Test
    void memberLoginWorks() {

        AuthHelper.loginAsMember(driver, wait, baseUrl);

        assertTrue(driver.findElement(By.id("sidebar")).isDisplayed());
    }
}