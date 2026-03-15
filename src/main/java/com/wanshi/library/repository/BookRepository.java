package com.wanshi.library.repository;

import com.wanshi.library.dto.BookDTO;
import com.wanshi.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
    SELECT new com.wanshi.library.dto.BookDTO(
    c.name,
    COUNT(b)
    )
    FROM Book b
    JOIN b.category c
    GROUP BY c.name
    """)
    List<BookDTO> countBooksByCategory();
}
