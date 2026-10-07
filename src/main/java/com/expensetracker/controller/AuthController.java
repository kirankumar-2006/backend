package com.expensetracker.controller;
import com.expensetracker.dto.Dtos.*; import com.expensetracker.service.AuthService; import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestController @RequestMapping("/api/auth")
public class AuthController {
  private final AuthService s; public AuthController(AuthService s){ this.s=s; }
  @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> register(@Valid @RequestBody RegisterReq r){ return s.register(r); }
  @PostMapping("/login") public Map<String,Object> login(@Valid @RequestBody LoginReq r){ return s.login(r); }
}
