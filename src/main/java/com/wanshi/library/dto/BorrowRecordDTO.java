package com.wanshi.library.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRecordDTO {

    private Long id;

    private String bookTitle;

    private String bookAuthor;

    private String isbn;

    private String status;

    private String borrowDate;

    private String dueDate;

    private String returnDate;

}