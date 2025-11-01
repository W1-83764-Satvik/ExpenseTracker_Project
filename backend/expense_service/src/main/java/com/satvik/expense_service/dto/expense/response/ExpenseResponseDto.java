package com.satvik.expense_service.dto.expense.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseResponseDto {
    private Long id;
    private String title;
    private BigDecimal amount;
    private String description;
    private String category;
    private LocalDate date;
    private String userEmail;
}
