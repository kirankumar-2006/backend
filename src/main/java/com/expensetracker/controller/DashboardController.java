package com.expensetracker.controller;
import com.expensetracker.service.FinanceService; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
  private final FinanceService s; public DashboardController(FinanceService s){ this.s=s; }
  @GetMapping("/summary") public Map<String,Object> summary(Authentication a){ return s.summary(a.getName()); }
  @GetMapping("/monthly") public List<Map<String,Object>> monthly(Authentication a){ return s.monthly(a.getName()); }
  @GetMapping("/categories") public List<Map<String,Object>> categories(Authentication a){ return s.categories(a.getName()); }
}
