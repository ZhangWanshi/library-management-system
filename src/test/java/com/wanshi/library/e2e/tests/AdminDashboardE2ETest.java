package com.wanshi.library.e2e.tests;

import com.wanshi.library.e2e.base.BaseE2ETest;
import com.wanshi.library.e2e.util.AuthHelper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminDashboardE2ETest extends BaseE2ETest {

    @Test
    void adminCanViewBorrowingStatistics() {
        AuthHelper.loginAsAdmin(driver, wait, baseUrl);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar")));

        WebElement menuBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuAnalyticDashboard")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", menuBtn);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuBtn);
        wait.until(d -> !driver.findElement(By.id("totalBooks")).getText().isEmpty());
        assertFalse(driver.findElement(By.id("totalBooks")).getText().isEmpty(), "Total Books should not be empty");

        assertTrue(driver.findElement(By.id("categoryChart")).isDisplayed());
        assertTrue(driver.findElement(By.id("borrowChart")).isDisplayed());

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Boolean isCategoryChartLoaded = (Boolean) js.executeScript(
                "return Chart.getChart('categoryChart') !== undefined;"
        );
        assertTrue(isCategoryChartLoaded, "Category Chart should be initialized");
    }
}