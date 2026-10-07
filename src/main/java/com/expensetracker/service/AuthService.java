package com.expensetracker.service;
import com.expensetracker.config.JwtUtil; import com.expensetracker.dto.Dtos.*; import com.expensetracker.entity.User; import com.expensetracker.exception.ApiException;
import com.expensetracker.repository.UserRepository; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service; import java.util.Map;
@Service
public class AuthService {
  private final UserRepository repo; private final PasswordEncoder enc; private final JwtUtil jwt;
  public AuthService(UserRepository r,PasswordEncoder e,JwtUtil j){ repo=r; enc=e; jwt=j; }
  public Map<String,Object> register(RegisterReq r){
    String email=r.email().trim().toLowerCase();
    if(repo.existsByEmail(email)) throw ApiException.exists("Email is already registered");
    User u=new User(); u.name=r.name().trim(); u.email=email; u.password=enc.encode(r.password()); repo.save(u);
    return Map.of("success",true,"message","Registration successful"); }
  public Map<String,Object> login(LoginReq r){
    User u=repo.findByEmail(r.email().trim().toLowerCase()).orElseThrow(ApiException::invalidCredentials);
    if(!enc.matches(r.password(),u.password)) throw ApiException.invalidCredentials();
    return Map.of("token",jwt.generate(u.email),"name",u.name,"email",u.email); }
}
