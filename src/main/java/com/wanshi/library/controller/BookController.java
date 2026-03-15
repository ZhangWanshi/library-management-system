package com.wanshi.library.controller;

import com.wanshi.library.dto.AdminDashboardDTO;
import com.wanshi.library.dto.BookDTO;

import com.wanshi.library.service.BookService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping // US4 - Librarian
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN','MEMBER')")
    public List<BookDTO> getBooks() {
        return bookService.getAllBooks();
    }

    //US9– Admin Views Borrowing Statistics
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/summary")
    public AdminDashboardDTO summary() {
        return bookService.getSummaryStats();
    }

    //US9– Admin Views Borrowing Statistics
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-category")
    public List<BookDTO> booksByCategory() {
        return bookService.getBooksByCategory();
    }
}