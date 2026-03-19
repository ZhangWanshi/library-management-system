package com.wanshi.library.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BorrowingRuleDTO {
    @Min(value = 0, message = "Max Books Allowed must be >= 0")
    private int maxBooksAllowed;

    @Min(value = 0, message = "Borrow Duration (Days) must be >= 0")
    private int borrowDurationDays;
}