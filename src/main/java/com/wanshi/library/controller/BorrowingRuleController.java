package com.wanshi.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BorrowingRuleController {

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "admin-access";
    }

    @GetMapping("/member")
    public String memberEndpoint() {
        return "member-access";
    }
}
