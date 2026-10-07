package com.expensetracker.controller;
import com.expensetracker.dto.Dtos.TxReq; import com.expensetracker.entity.Transaction; import com.expensetracker.service.FinanceService; import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat; import org.springframework.http.HttpStatus; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;
import java.time.LocalDate; import java.util.Map;
@RestController @RequestMapping("/api/transactions")
public class TransactionController {
  private final FinanceService s; public TransactionController(FinanceService s){ this.s=s; }
  @GetMapping public Map<String,Object> list(Authentication a,@RequestParam(required=false) String search,@RequestParam(required=false) String category,@RequestParam(required=false) String type,
    @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size){
    return s.list(a.getName(),search,category,type,date,page,Math.max(size,1)); }
  @GetMapping("/{id}") public Transaction get(Authentication a,@PathVariable Long id){ return s.get(a.getName(),id); }
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Transaction create(Authentication a,@Valid @RequestBody TxReq r){ return s.create(a.getName(),r); }
  @PutMapping("/{id}") public Transaction update(Authentication a,@PathVariable Long id,@Valid @RequestBody TxReq r){ return s.update(a.getName(),id,r); }
  @DeleteMapping("/{id}") public Map<String,Object> delete(Authentication a,@PathVariable Long id){ s.delete(a.getName(),id); return Map.of("success",true,"message","Transaction deleted"); }
}
