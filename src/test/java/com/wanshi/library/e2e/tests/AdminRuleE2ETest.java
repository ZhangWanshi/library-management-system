package com.wanshi.library.e2e.tests;

import com.wanshi.library.e2e.base.BaseE2ETest;
import com.wanshi.library.e2e.util.AuthHelper;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminRuleE2ETest extends BaseE2ETest {

    @Test
    void adminCanUpdateBorrowingRulesSuccessfully() {
        AuthHelper.loginAsAdmin(driver, wait, baseUrl);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Borrowing Rules')]")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("maxBooks")));

        String newMaxBooks = "7";
        String newDuration = "21";

        WebElement maxBooksInput = driver.findElement(By.id("maxBooks"));
        maxBooksInput.clear();
        maxBooksInput.sendKeys(newMaxBooks);

        WebElement borrowDurationInput = driver.findElement(By.id("borrowDuration"));
        borrowDurationInput.clear();
        borrowDurationInput.sendKeys(newDuration);

        driver.findElement(By.xpath("//button[contains(text(),'Save Rules')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rulesSuccess")));
        String successMsg = driver.findElement(By.id("rulesSuccess")).getText();
        assertTrue(successMsg.contains("updated successfully"));

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("currentRuleInfo"), newMaxBooks
        ));
        String currentRuleText = driver.findElement(By.id("currentRuleInfo")).getText();
        assertTrue(currentRuleText.contains("Max Books Allowed : " + newMaxBooks));
        assertTrue(currentRuleText.contains("Borrow Duration : " + newDuration));
    }

    @Test
    void adminCannotSetNegativeRules() {
        AuthHelper.loginAsAdmin(driver, wait, baseUrl);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Borrowing Rules')]")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("maxBooks")));

        WebElement maxBooksInput = driver.findElement(By.id("maxBooks"));
        maxBooksInput.clear();
        maxBooksInput.sendKeys("-1");

        WebElement borrowDurationInput = driver.findElement(By.id("borrowDuration"));
        borrowDurationInput.clear();
        borrowDurationInput.sendKeys("14");

        driver.findElement(By.xpath("//button[contains(text(),'Save Rules')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("maxBooksAlert")));
        String alertMsg = driver.findElement(By.id("maxBooksAlert")).getText();
        assertTrue(alertMsg.contains(">= 0"));
    }
}