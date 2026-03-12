package com.wanshi.library.repository;

import com.wanshi.library.entity.BorrowRecord;
import com.wanshi.library.entity.User;
import com.wanshi.library.enumtype.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    long countByMemberAndStatus(User member, BorrowingStatus status);

}
