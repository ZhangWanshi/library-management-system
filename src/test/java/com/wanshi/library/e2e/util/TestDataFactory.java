package com.wanshi.library.e2e.util;

import java.util.UUID;

public class TestDataFactory {

    public static String randomUsername() {
        return "seleniumUser_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String randomEmail() {
        return "selenium_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
    }

}