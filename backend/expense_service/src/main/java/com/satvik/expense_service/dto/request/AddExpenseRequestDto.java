package com.satvik.expense_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddExpenseRequestDto {

    private String title;

    private BigDecimal amount;

    private String desc;
}