package com.wanshi.library.unit.service;

import com.wanshi.library.dto.BorrowRecordDTO;
import com.wanshi.library.entity.Book;
import com.wanshi.library.entity.BorrowRecord;
import com.wanshi.library.entity.User;
import com.wanshi.library.enumtype.BookStatus;
import com.wanshi.library.enumtype.BorrowingStatus;
import com.wanshi.library.repository.BookRepository;
import com.wanshi.library.repository.BorrowRecordRepository;
import com.wanshi.library.repository.UserRepository;
import com.wanshi.library.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BorrowService borrowService;

    private BorrowRecord borrowRecord;
    private Book book;
    private  User member;

    @BeforeEach
    void setUp() {

        book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .status(BookStatus.BORROWED)
                .build();
        member = User.builder()
                .id(1L)
                .username("john")
                .email("john@test.com")
                .build();

        borrowRecord = BorrowRecord.builder()
                .id(1L)
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BorrowingStatus.BORROWED)
                .build();
    }

    @Test
    void returnBook_shouldUpdateStatusAndBookAvailability() {

        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(borrowRecord));

        borrowService.returnBook("john", 1L);

        assertEquals(BorrowingStatus.RETURNED, borrowRecord.getStatus());
        assertEquals(BookStatus.AVAILABLE, book.getStatus());

        verify(borrowRecordRepository).save(borrowRecord);
        verify(bookRepository).save(book);
    }

    @Test
    void returnBook_shouldThrowWhenRecordNotFound() {

        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> borrowService.returnBook("john", 1L));
    }

    @Test
    void getMemberBorrowRecords_shouldReturnEmptyWhenNoRecords() {
        when(borrowRecordRepository.findByMemberUsername("john")).thenReturn(List.of());

        List<BorrowRecordDTO> result = borrowService.getMemberBorrowRecords("john");

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllBorrowRecords_shouldReturnDTOList() {

        when(borrowRecordRepository.findAll()).thenReturn(List.of(borrowRecord));

        List<BorrowRecordDTO> result = borrowService.getAllBorrowRecords();

        assertEquals(1, result.size());
    }

    @Test
    void getMostBorrowedBooks_shouldReturnTopBooks() {

        BorrowRecordDTO dto = BorrowRecordDTO.builder()
                .bookTitle("Clean Code")
                .build();

        when(borrowRecordRepository.findMostBorrowedBooks())
                .thenReturn(List.of(dto));

        List<BorrowRecordDTO> result = borrowService.getMostBorrowedBooks();

        assertEquals(1, result.size());
    }

    @Test
    void getMostBorrowedBooks_shouldReturnEmptyListWhenNoData() {
        when(borrowRecordRepository.findMostBorrowedBooks()).thenReturn(List.of());

        List<BorrowRecordDTO> result = borrowService.getMostBorrowedBooks();

        assertTrue(result.isEmpty());
    }
}