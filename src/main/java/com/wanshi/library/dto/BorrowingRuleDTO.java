package com.wanshi.library.dto;

import lombok.Data;

@Data
public class BorrowingRuleDTO {
    private int maxBooksAllowed;
    private int borrowDurationDays;
}