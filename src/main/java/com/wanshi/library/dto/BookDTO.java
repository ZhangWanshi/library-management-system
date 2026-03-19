package com.wanshi.library.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;
    @NotBlank(message = "Isbn is required")
    private String isbn;

    private String status;

    @NotBlank(message = "Category is required")
    private String category;

    private String coverImageUrl;

    // for admin statistics
    private long bookCount;
    public BookDTO(String category, long bookCount) {
        this.category = category;
        this.bookCount = bookCount;
    }
}
