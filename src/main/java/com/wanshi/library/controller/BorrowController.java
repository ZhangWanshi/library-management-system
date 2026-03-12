package com.wanshi.library.controller;

import com.wanshi.library.service.BookService;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/borrowing")
@RequiredArgsConstructor
public class BorrowController {
    private final BookService bookService;

    @PostMapping("/{bookId}") // US6 - Member
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Void> borrowBook(@PathVariable Long bookId, Principal principal) {
        bookService.borrowBook(principal.getName(), bookId);
        return ResponseEntity.ok().build();
    }

}
