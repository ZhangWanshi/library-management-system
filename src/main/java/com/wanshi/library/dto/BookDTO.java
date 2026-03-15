package com.wanshi.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String status;
    private String category;
    private String coverImageUrl;

    // for admin statistics
    private long bookCount;
    public BookDTO(String category, long bookCount) {
        this.category = category;
        this.bookCount = bookCount;
    }
}
