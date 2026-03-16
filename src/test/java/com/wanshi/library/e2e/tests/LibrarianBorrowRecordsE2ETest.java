package com.wanshi.library.e2e.tests;

import com.wanshi.library.e2e.base.BaseE2ETest;
import com.wanshi.library.e2e.util.AuthHelper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LibrarianBorrowRecordsE2ETest extends BaseE2ETest {

    @Test
    void librarianCanViewAllBorrowRecords() {
        AuthHelper.loginAsLibrarian(driver, wait, baseUrl);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar")));

        WebElement menuBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("menuBorrowManagement")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", menuBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuBtn);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("borrowManagementTable")));

        WebElement recordRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@id='borrowManagementTable']//tr[contains(., 'The Great Gatsby')]")
        ));

        assertTrue(recordRow.getText().contains("member"), "member");
    }
}