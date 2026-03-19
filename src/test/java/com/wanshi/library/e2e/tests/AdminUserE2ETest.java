package com.wanshi.library.e2e.tests;

import com.wanshi.library.e2e.base.BaseE2ETest;
import com.wanshi.library.e2e.util.AuthHelper;
import com.wanshi.library.e2e.util.TestDataFactory;

import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminUserE2ETest extends BaseE2ETest {

    @Test
    void adminCanCreateUser() {

        AuthHelper.loginAsAdmin(driver, wait, baseUrl);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'User Management')]")
        )).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Add New User')]")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("newUsername")
        ));

        String username = TestDataFactory.randomUsername();
        String email = TestDataFactory.randomEmail();

        driver.findElement(By.id("newUsername")).sendKeys(username);

        driver.findElement(By.id("newEmail"))
                .sendKeys(email);

        driver.findElement(By.id("newPassword"))
                .sendKeys("Password123!");

        driver.findElement(By.id("newRole"))
                .sendKeys("MEMBER");

        driver.findElement(By.id("createUserBtn")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@id='usersTable']//td[contains(text(),'" + username + "')]")
        ));
        assertTrue(driver.getPageSource().contains(username));
    }
}