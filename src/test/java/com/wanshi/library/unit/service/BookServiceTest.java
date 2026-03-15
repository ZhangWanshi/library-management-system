package com.wanshi.library.unit.service;

import com.wanshi.library.dto.AdminDashboardDTO;
import com.wanshi.library.dto.BookDTO;
import com.wanshi.library.entity.*;
import com.wanshi.library.enumtype.BookStatus;
import com.wanshi.library.enumtype.BorrowingStatus;
import com.wanshi.library.exception.BookNotAvailableException;
import com.wanshi.library.exception.BookNotFoundException;
import com.wanshi.library.exception.BorrowLimitExceededException;
import com.wanshi.library.exception.UserNotFoundException;
import com.wanshi.library.repository.*;
import com.wanshi.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {


    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowingRuleRepository ruleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BorrowRecordRepository borrowRecordRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private User user;
    private BorrowingRule rule;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert Martin")
                .isbn("123")
                .status(BookStatus.AVAILABLE)
                .build();

        user = User.builder().id(1L).username("john").build();

        rule = new BorrowingRule();
        rule.setMaxBooksAllowed(2);
        rule.setBorrowDurationDays(14);
    }

    @Test
    void addBook_shouldCreateBookSuccessfully() {

        BookDTO dto = BookDTO.builder()
                .title("Clean Code")
                .author("Robert Martin")
                .isbn("123")
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.addBook(dto);

        assertEquals("Clean Code", result.getTitle());

        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void getAllBooks_shouldReturnBookList() {

        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDTO> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
    }

    @Test
    void addBook_shouldAssignDefaultCoverAndCategory() {
        BookDTO dto = BookDTO.builder()
                .title("New Book")
                .author("Author")
                .isbn("ISBN")
                .coverImageUrl("")
                .category("Programming")
                .build();

        when(categoryRepository.findByName("Programming")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookDTO result = bookService.addBook(dto);

        assertEquals("/images/default-book.png", result.getCoverImageUrl());
        assertEquals("Programming", result.getCategory());
    }

    @Test
    void borrowBook_shouldThrowWhenBookNotFound() {

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.borrowBook("john", 1L));
    }

    @Test
    void borrowBook_shouldThrowWhenBookNotAvailable() {

        book.setStatus(BookStatus.BORROWED);

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BookNotAvailableException.class,
                () -> bookService.borrowBook("john", 1L));
    }

    @Test
    void borrowBook_shouldThrowUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bookService.borrowBook("unknown", 1L));
    }

    @Test
    void borrowBook_shouldThrowBookNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.borrowBook("john", 1L));
    }

    @Test
    void borrowBook_shouldThrowBookNotAvailable() {
        book.setStatus(BookStatus.BORROWED);

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BookNotAvailableException.class,
                () -> bookService.borrowBook("john", 1L));
    }

    @Test
    void borrowBook_shouldThrowWhenLimitExceeded() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(ruleRepository.findById(1L)).thenReturn(Optional.of(rule));
        when(borrowRecordRepository.countByMemberAndStatus(user, BorrowingStatus.BORROWED)).thenReturn(2L);

        assertThrows(BorrowLimitExceededException.class,
                () -> bookService.borrowBook("john", 1L));
    }

    @Test
    void getSummaryStats_shouldReturnDashboardStats() {

        when(bookRepository.count()).thenReturn(10L);
        when(borrowRecordRepository.count()).thenReturn(5L);
        when(userRepository.countByRole_Name("MEMBER")).thenReturn(3L);

        AdminDashboardDTO result = bookService.getSummaryStats();

        assertEquals(10, result.getTotalBooks());
        assertEquals(5, result.getTotalBorrowRecords());
        assertEquals(3, result.getTotalMembers());
    }
}