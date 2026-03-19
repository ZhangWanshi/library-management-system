package com.wanshi.library.controller;

import com.wanshi.library.dto.BorrowingRuleDTO;
import com.wanshi.library.service.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BorrowingRuleDTO getRules() {
        return ruleService.getRules();
    }

    @PutMapping // US3 - Admin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRules(@Valid @RequestBody BorrowingRuleDTO dto) {
        ruleService.updateRules(dto);
        return ResponseEntity.ok().build();
    }
}