package com.wanshi.library.service;

import com.wanshi.library.dto.BorrowRecordDTO;
import com.wanshi.library.entity.Book;
import com.wanshi.library.entity.BorrowRecord;
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

        List<BorrowRecord> borrowRecords = borrowRecordRepository.findByMemberUsername(username);

        return borrowRecords.stream()
                .map(borrowRecord -> BorrowRecordDTO.builder()
                        .id(borrowRecord.getId())
                        .bookTitle(borrowRecord.getBook().getTitle())
                        .bookAuthor(borrowRecord.getBook().getAuthor())
                        .isbn(borrowRecord.getBook().getIsbn())
                        .status(borrowRecord.getStatus().name())
                        .borrowDate(borrowRecord.getBorrowDate().toString())
                        .dueDate(borrowRecord.getDueDate().toString())
                        .returnDate(
                                borrowRecord.getReturnDate() != null
                                        ? borrowRecord.getReturnDate().toString()
                                        : null
                        )
                        .build())
                .toList();
    }

    // US7 – Member Returns a Book
    @Transactional
    public void returnBook(String username, Long recordId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));


        borrowRecord.setStatus(BorrowingStatus.RETURNED);
        borrowRecord.setReturnDate(LocalDate.now());

        Book book = borrowRecord.getBook();
        book.setStatus(BookStatus.AVAILABLE);

        borrowRecordRepository.save(borrowRecord);
        bookRepository.save(book);
    }

    // US8 – Librarian views all borrow records
    public List<BorrowRecordDTO> getAllBorrowRecords() {

        List<BorrowRecord> records = borrowRecordRepository.findAll();

        return records.stream()
                .map(borrowRecord -> BorrowRecordDTO.builder()
                        .id(borrowRecord.getId())

                        .memberUsername(borrowRecord.getMember().getUsername())
                        .memberEmail(borrowRecord.getMember().getEmail())

                        .bookTitle(borrowRecord.getBook().getTitle())
                        .isbn(borrowRecord.getBook().getIsbn())

                        .borrowDate(borrowRecord.getBorrowDate().toString())
                        .dueDate(borrowRecord.getDueDate().toString())

                        .returnDate(
                                borrowRecord.getReturnDate() != null
                                        ? borrowRecord.getReturnDate().toString()
                                        : null
                        )

                        .status(borrowRecord.getStatus().name())

                        .build())
                .toList();
    }

    //US9– Admin Views Borrowing Statistics
    public List<BorrowRecordDTO> getMostBorrowedBooks() {
        return borrowRecordRepository
                .findMostBorrowedBooks()
                .stream()
                .limit(5)
                .toList();
    }
}
