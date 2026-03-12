package com.wanshi.library.service;

import com.wanshi.library.dto.BookDTO;
import com.wanshi.library.entity.*;
import com.wanshi.library.enumtype.BookStatus;
import com.wanshi.library.enumtype.BorrowingStatus;
import com.wanshi.library.exception.BookNotAvailableException;
import com.wanshi.library.exception.BookNotFoundException;
import com.wanshi.library.exception.BorrowLimitExceededException;
import com.wanshi.library.exception.UserNotFoundException;
import com.wanshi.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BorrowingRuleRepository ruleRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final CategoryRepository categoryRepository;


    /**
     * US4 – Librarian Adds New Books
     */
    public BookDTO addBook(BookDTO dto) {
        String imageUrl = (dto.getCoverImageUrl() != null && !dto.getCoverImageUrl().trim().isEmpty())
                ? dto.getCoverImageUrl().trim()
                : "/images/default-book.png";
        Category category = null;

        if (dto.getCategory() != null) {

            category = categoryRepository
                    .findByName(dto.getCategory())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(dto.getCategory());
                        return categoryRepository.save(newCategory);
                    });
        }

        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .status(BookStatus.AVAILABLE)
                .coverImageUrl(imageUrl)
                .category(category)
                .build();
        Book saved = bookRepository.save(book);
        return mapToBookDTO(saved);
    }

    /**
     * US5 – View Available Books
     */
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToBookDTO)
                .toList();
    }

    /**
     * US6 – Member Borrows a Book
     */
    @Transactional
    public void borrowBook(String username, Long bookId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookNotAvailableException("Book is already borrowed");
        }

        BorrowingRule rule = ruleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Borrowing rules not configured by Admin"));

        long currentBorrowedCount = borrowRecordRepository.countByMemberAndStatus(user, BorrowingStatus.BORROWED);
        if (currentBorrowedCount >= rule.getMaxBooksAllowed()) {
            throw new BorrowLimitExceededException(
                    "You have reached your borrowing limit of "
                            + rule.getMaxBooksAllowed() + " books."
            );
        }

        book.setStatus(BookStatus.BORROWED);

        BorrowRecord borrowRecord = BorrowRecord.builder()
                .member(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(rule.getBorrowDurationDays()))
                .status(BorrowingStatus.BORROWED)
                .build();

        borrowRecordRepository.save(borrowRecord);
        bookRepository.save(book);
    }

    // Helper: Entity to DTO
    private BookDTO mapToBookDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .status(book.getStatus().name())
                .coverImageUrl(book.getCoverImageUrl())
                .category(
                        book.getCategory() != null
                                ? book.getCategory().getName()
                                : null
                )
                .build();
    }
}
