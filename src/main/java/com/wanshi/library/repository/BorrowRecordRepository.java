package com.wanshi.library.repository;

import com.wanshi.library.dto.BorrowRecordDTO;
import com.wanshi.library.entity.BorrowRecord;
import com.wanshi.library.entity.User;
import com.wanshi.library.enumtype.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    long countByMemberAndStatus(User member, BorrowingStatus status);

    List<BorrowRecord> findByMemberUsername(String username);

    @Query("""
    SELECT new com.wanshi.library.dto.BorrowRecordDTO(
    b.title,
    b.category.name,
    COUNT(br)
    )
    FROM BorrowRecord br
    JOIN br.book b
    GROUP BY b.title, b.category.name
    ORDER BY COUNT(br) DESC
    """)
    List<BorrowRecordDTO> findMostBorrowedBooks();

}
