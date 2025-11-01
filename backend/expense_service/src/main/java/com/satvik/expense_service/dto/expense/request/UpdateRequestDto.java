package com.satvik.expense_service.dto.expense.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRequestDto {
    private String title;
    private BigDecimal amount;
    private String description;
    private String category;
}
