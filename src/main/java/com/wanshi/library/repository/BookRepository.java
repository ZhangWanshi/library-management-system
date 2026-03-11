package com.wanshi.library.repository;

import com.wanshi.library.entity.Book;
import com.wanshi.library.enumtype.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByStatus(BookStatus status);
}
