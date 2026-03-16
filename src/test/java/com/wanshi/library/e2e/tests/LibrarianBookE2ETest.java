package com.wanshi.library.e2e.tests;

import com.wanshi.library.e2e.base.BaseE2ETest;
import com.wanshi.library.e2e.util.AuthHelper;
import com.wanshi.library.e2e.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LibrarianBookE2ETest extends BaseE2ETest {

    @Test
    void librarianCanAddBook() {
        AuthHelper.loginAsLibrarian(driver, wait, baseUrl);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar")));
        WebElement menuBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("menuBookManagement")));
        js.executeScript("arguments[0].click();", menuBtn);

        WebElement addBookBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(text(),'Add New Book')]")
        ));
        js.executeScript("arguments[0].click();", addBookBtn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addBookModal")));

        String title = "Automated Test Book " + System.currentTimeMillis();
        String author = "Tester";
        String isbn = TestDataFactory.randomIsbn();

        js.executeScript("let el = document.getElementById('bookTitle');" +
                "el.value = arguments[0];" +
                "el.dispatchEvent(new Event('input'));", title);

        js.executeScript("let el = document.getElementById('bookAuthor');" +
                "el.value = arguments[0];" +
                "el.dispatchEvent(new Event('input'));", author);

        js.executeScript("let el = document.getElementById('bookIsbn');" +
                "el.value = arguments[0];" +
                "el.dispatchEvent(new Event('input'));", isbn);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#bookCategory option"), 1));
        js.executeScript("let sel = document.getElementById('bookCategory');" +
                "sel.selectedIndex = 1;" +
                "sel.dispatchEvent(new Event('change'));");

        WebElement saveBtn = driver.findElement(By.xpath("//button[contains(text(),'Save Book')]"));
        js.executeScript("arguments[0].click();", saveBtn);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("addBookModal")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("globalAlert")));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("booksTable"), title));

        assertTrue(driver.getPageSource().contains(title));
    }
}