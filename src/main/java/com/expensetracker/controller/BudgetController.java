package com.expensetracker.controller;
import com.expensetracker.dto.Dtos.BudgetReq; import com.expensetracker.service.FinanceService; import jakarta.validation.Valid;
import org.springframework.http.HttpStatus; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/budgets")
public class BudgetController {
  private final FinanceService s; public BudgetController(FinanceService s){ this.s=s; }
  @GetMapping public List<Map<String,Object>> list(Authentication a){ return s.budgets(a.getName()); }
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> create(Authentication a,@Valid @RequestBody BudgetReq r){ return s.createBudget(a.getName(),r); }
  @PutMapping("/{id}") public Map<String,Object> update(Authentication a,@PathVariable Long id,@Valid @RequestBody BudgetReq r){ return s.updateBudget(a.getName(),id,r); }
  @DeleteMapping("/{id}") public Map<String,Object> delete(Authentication a,@PathVariable Long id){ s.deleteBudget(a.getName(),id); return Map.of("success",true,"message","Budget deleted"); }
}
