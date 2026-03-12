package com.wanshi.library.repository;

import com.wanshi.library.entity.BorrowRecord;
import com.wanshi.library.entity.User;
import com.wanshi.library.enumtype.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    long countByMemberAndStatus(User member, BorrowingStatus status);

    Optional<BorrowRecord> findByMemberIdAndBookIdAndStatus(Long memberId, Long bookId, BorrowingStatus status);

    List<BorrowRecord> findByMemberUsername(String username);

}
