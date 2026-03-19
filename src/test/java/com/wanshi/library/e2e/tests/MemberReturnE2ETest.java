package com.wanshi.library.e2e.tests;

import com.wanshi.library.e2e.base.BaseE2ETest;
import com.wanshi.library.e2e.util.AuthHelper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberReturnE2ETest extends BaseE2ETest {

    @Test
    void memberCanReturnBook() {
        AuthHelper.loginAsMember(driver, wait, baseUrl);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar")));
        WebElement menuBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("menuMyBorrowing")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuBtn);

        By rowWithBorrowed = By.xpath("//table[@id='borrowTable']//tr[contains(., 'Borrowed')]");
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowWithBorrowed));

        WebElement returnBtn = row.findElement(By.xpath(".//button[contains(text(),'Return')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", returnBtn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("globalAlert")));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//table[@id='borrowTable']"),
                "Returned"
        ));

        assertTrue(driver.getPageSource().contains("Returned"));
    }
}