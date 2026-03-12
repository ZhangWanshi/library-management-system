package com.wanshi.library.service;

import com.wanshi.library.dto.BorrowRecordDTO;
import com.wanshi.library.entity.Book;
import com.wanshi.library.entity.BorrowRecord;
import com.wanshi.library.entity.User;
import com.wanshi.library.enumtype.BookStatus;
import com.wanshi.library.enumtype.BorrowingStatus;
import com.wanshi.library.repository.BookRepository;
import com.wanshi.library.repository.BorrowRecordRepository;
import com.wanshi.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public List<BorrowRecordDTO> getMemberBorrowRecords(String username) {

        List<BorrowRecord> records = borrowRecordRepository.findByMemberUsername(username);

        return records.stream()
                .map(record -> BorrowRecordDTO.builder()
                        .id(record.getId())
                        .bookTitle(record.getBook().getTitle())
                        .bookAuthor(record.getBook().getAuthor())
                        .isbn(record.getBook().getIsbn())
                        .status(record.getStatus().name())
                        .borrowDate(record.getBorrowDate().toString())
                        .dueDate(record.getDueDate().toString())
                        .returnDate(
                                record.getReturnDate() != null
                                        ? record.getReturnDate().toString()
                                        : null
                        )
                        .build())
                .toList();
    }

    /**
     * US7 – Member Returns a Book
     */
    @Transactional
    public void returnBook(String username, Long bookId) {
        User user = userRepository.findByUsername(username).orElseThrow();

        BorrowRecord record = borrowRecordRepository.findByMemberIdAndBookIdAndStatus(user.getId(), bookId, BorrowingStatus.BORROWED)
                .orElseThrow(() -> new RuntimeException("No active borrowing record found for this book"));

        // 更新记录状态
        record.setStatus(BorrowingStatus.RETURNED);
        record.setReturnDate(LocalDate.now());

        // 更新书籍状态为可用
        Book book = record.getBook();
        book.setStatus(BookStatus.AVAILABLE);

        borrowRecordRepository.save(record);
        bookRepository.save(book);
    }
}
