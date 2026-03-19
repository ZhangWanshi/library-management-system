package com.wanshi.library.service;

import com.wanshi.library.dto.BorrowingRuleDTO;
import com.wanshi.library.entity.BorrowingRule;
import com.wanshi.library.repository.BorrowingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RuleService {
    private final BorrowingRuleRepository ruleRepository;

    /**
     * US3 – Admin Configures Borrowing Rules
     */
    public void updateRules(BorrowingRuleDTO dto) {
        if (dto.getMaxBooksAllowed() < 0) {
            throw new IllegalArgumentException("Max Books Allowed must be >= 0");
        }

        if (dto.getBorrowDurationDays() < 0) {
            throw new IllegalArgumentException("Borrow Duration (Days) must be >= 0");
        }
        BorrowingRule rule = ruleRepository.findById(1L).orElse(new BorrowingRule());
        rule.setMaxBooksAllowed(dto.getMaxBooksAllowed());
        rule.setBorrowDurationDays(dto.getBorrowDurationDays());
        ruleRepository.save(rule);
    }

    public BorrowingRuleDTO getRules() {

        BorrowingRule rule = ruleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Borrowing rules not configured"));

        BorrowingRuleDTO dto = new BorrowingRuleDTO();
        dto.setMaxBooksAllowed(rule.getMaxBooksAllowed());
        dto.setBorrowDurationDays(rule.getBorrowDurationDays());

        return dto;
    }
}
