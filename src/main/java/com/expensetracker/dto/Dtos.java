package com.expensetracker.dto;
import jakarta.validation.constraints.*; import java.time.LocalDate;
public final class Dtos {
  public record RegisterReq(@NotBlank(message="Name is required") String name,
    @NotBlank(message="Email is required") @Email(message="Email must be valid") String email,
    @NotBlank @Size(min=6,message="Password must be at least 6 characters") String password){}
  public record LoginReq(@NotBlank(message="Email is required") String email,@NotBlank(message="Password is required") String password){}
  public record TxReq(@NotBlank(message="Type is required") String type,
    @Positive(message="Amount must be greater than 0") double amount,
    @NotBlank(message="Category is required") String category,
    @NotNull(message="Date is required") LocalDate date, String paymentMethod, String description){}
  public record BudgetReq(@Min(value=1,message="Month must be 1-12") @Max(value=12,message="Month must be 1-12") int month,
    @Min(value=2000,message="Year is invalid") int year, @Positive(message="Budget amount must be greater than 0") double amount){}
}
