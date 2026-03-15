package com.wanshi.library.unit.service;

import com.wanshi.library.dto.BorrowingRuleDTO;
import com.wanshi.library.entity.BorrowingRule;
import com.wanshi.library.repository.BorrowingRuleRepository;
import com.wanshi.library.service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuleServiceTest {

    @Mock
    private BorrowingRuleRepository ruleRepository;

    @InjectMocks
    private RuleService ruleService;

    private BorrowingRule rule;

    @BeforeEach
    void setUp() {

        rule = new BorrowingRule();
        rule.setId(1L);
        rule.setMaxBooksAllowed(5);
        rule.setBorrowDurationDays(14);
    }

    @Test
    void updateRules_shouldUpdateExistingRule() {

        BorrowingRuleDTO dto = new BorrowingRuleDTO();
        dto.setMaxBooksAllowed(10);
        dto.setBorrowDurationDays(30);

        when(ruleRepository.findById(1L)).thenReturn(Optional.of(rule));

        ruleService.updateRules(dto);

        assertEquals(10, rule.getMaxBooksAllowed());
        assertEquals(30, rule.getBorrowDurationDays());

        verify(ruleRepository).save(rule);
    }

    @Test
    void updateRules_shouldCreateNewRuleWhenNotExists() {
        BorrowingRuleDTO dto = new BorrowingRuleDTO();
        dto.setMaxBooksAllowed(3);
        dto.setBorrowDurationDays(7);

        when(ruleRepository.findById(1L)).thenReturn(Optional.empty());

        ruleService.updateRules(dto);

        verify(ruleRepository).save(any(BorrowingRule.class));
    }

    @Test
    void getRules_shouldReturnRuleDTO() {

        when(ruleRepository.findById(1L)).thenReturn(Optional.of(rule));

        BorrowingRuleDTO result = ruleService.getRules();

        assertEquals(5, result.getMaxBooksAllowed());
        assertEquals(14, result.getBorrowDurationDays());
    }

    @Test
    void getRules_shouldThrowException_whenRuleMissing() {

        when(ruleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> ruleService.getRules());
    }
}