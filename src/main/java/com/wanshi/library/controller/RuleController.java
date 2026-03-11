package com.wanshi.library.controller;

import com.wanshi.library.dto.BorrowingRuleDTO;
import com.wanshi.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {
    private final BookService bookService;

    @PutMapping // US3 - Admin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRules(@RequestBody BorrowingRuleDTO dto) {
        bookService.updateRules(dto);
        return ResponseEntity.ok().build();
    }
}