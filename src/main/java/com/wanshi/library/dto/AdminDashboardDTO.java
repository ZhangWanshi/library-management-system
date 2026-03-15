package com.wanshi.library.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDashboardDTO {

    private long totalBooks;

    private long totalBorrowRecords;

    private long totalMembers;
}