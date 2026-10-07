package com.expensetracker.controller;
import com.expensetracker.service.FinanceService; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/reports")
public class ReportController {
  private final FinanceService s; public ReportController(FinanceService s){ this.s=s; }
  @GetMapping("/daily") public List<Map<String,Object>> daily(Authentication a){ return s.daily(a.getName()); }
  @GetMapping("/weekly") public List<Map<String,Object>> weekly(Authentication a){ return s.weekly(a.getName()); }
  @GetMapping("/monthly") public List<Map<String,Object>> monthly(Authentication a){ return s.monthlyExpense(a.getName()); }
  @GetMapping("/category") public List<Map<String,Object>> category(Authentication a){ return s.categories(a.getName()); }
}
