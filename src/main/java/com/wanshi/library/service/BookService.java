package com.wanshi.library.service;

import com.wanshi.library.dto.BookDTO;
import com.wanshi.library.entity.Book;
import com.wanshi.library.enumtype.BookStatus;
import com.wanshi.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    /**
     * US4 – Librarian Adds New Books
     */
    public BookDTO addBook(BookDTO dto) {
        String imageUrl = (dto.getCoverImageUrl() != null && !dto.getCoverImageUrl().trim().isEmpty())
                ? dto.getCoverImageUrl().trim()
                : "/images/default-book.png";

        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .status(BookStatus.AVAILABLE)
                .coverImageUrl(imageUrl)
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
    // Helper: Entity to DTO
    private BookDTO mapToBookDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .status(book.getStatus().name())
                .coverImageUrl(book.getCoverImageUrl())
                .build();
    }
}
