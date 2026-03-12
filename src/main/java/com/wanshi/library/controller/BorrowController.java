package com.wanshi.library.controller;

import com.wanshi.library.dto.BorrowRecordDTO;
import com.wanshi.library.service.BookService;
import com.wanshi.library.service.BorrowService;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/borrowing")
@RequiredArgsConstructor
public class BorrowController {
    private final BookService bookService;
    private final BorrowService borrowService;

    // US6 Borrow
    @PostMapping("/{bookId}") // US6 - Member
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Void> borrowBook(@PathVariable Long bookId, Principal principal) {
        bookService.borrowBook(principal.getName(), bookId);
        return ResponseEntity.ok().build();
    }

    // US7 Return
    @PostMapping("/return/{bookId}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Void> returnBook(@PathVariable Long bookId, Principal principal) {

        borrowService.returnBook(principal.getName(), bookId);

        return ResponseEntity.ok().build();
    }

    // US7 Browse Borrow Records
    @GetMapping("/my-records")
    @PreAuthorize("hasRole('MEMBER')")
    public List<BorrowRecordDTO> getMyRecords(Principal principal) {

        return borrowService.getMemberBorrowRecords(principal.getName());
    }

}
