package com.wanshi.library.e2e.tests;

import com.wanshi.library.e2e.base.BaseE2ETest;
import com.wanshi.library.e2e.util.AuthHelper;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LogoutE2ETest extends BaseE2ETest {

    @Test
    void logoutWorks() {

        AuthHelper.loginAsAdmin(driver, wait, baseUrl);

        AuthHelper.logout(driver, wait);

        assertTrue(driver.findElement(By.id("username")).isDisplayed());
    }
}