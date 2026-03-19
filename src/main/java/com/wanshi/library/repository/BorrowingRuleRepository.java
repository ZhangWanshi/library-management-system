package com.wanshi.library.repository;

import com.wanshi.library.entity.BorrowingRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRuleRepository extends JpaRepository<BorrowingRule, Long> {
}
