package com.wanshi.library.e2e.tests;

import com.wanshi.library.e2e.base.BaseE2ETest;
import com.wanshi.library.e2e.util.AuthHelper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;
class MemberBookE2ETest extends BaseE2ETest {

    @Test
    void memberCanViewPreloadedBooks() {
        AuthHelper.loginAsMember(driver, wait, baseUrl);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar")));

        WebElement menuBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("menuBookList")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuBtn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='booksGrid']//h6[text()='The Pragmatic Programmer']")
        ));

        assertTrue(driver.findElement(By.xpath("//h6[text()='The Pragmatic Programmer']")).isDisplayed());
    }
}