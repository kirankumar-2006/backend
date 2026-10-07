package com.expensetracker.config;
import jakarta.servlet.*; import jakarta.servlet.http.*; import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException; import java.util.List;
@Component
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwt; public JwtFilter(JwtUtil j){ jwt=j; }
  @Override protected void doFilterInternal(HttpServletRequest q,HttpServletResponse r,FilterChain c) throws ServletException,IOException {
    String h=q.getHeader("Authorization");
    if(h!=null&&h.startsWith("Bearer ")){ try{ String e=jwt.parse(h.substring(7));
      SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(e,null,List.of())); }catch(Exception ignored){} }
    c.doFilter(q,r);
  }
}
