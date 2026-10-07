package com.expensetracker.exception;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*; import java.time.LocalDateTime; import java.util.*; import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
  private ResponseEntity<Map<String,Object>> body(HttpStatus s,String m){
    return ResponseEntity.status(s).body(Map.of("success",false,"message",m,"timestamp",LocalDateTime.now().toString())); }
  @ExceptionHandler(ApiException.class) ResponseEntity<Map<String,Object>> api(ApiException e){ return body(e.status,e.getMessage()); }
  @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,Object>> val(MethodArgumentNotValidException e){
    return body(HttpStatus.BAD_REQUEST,e.getBindingResult().getFieldErrors().stream().map(f->f.getDefaultMessage()).collect(Collectors.joining("; "))); }
  @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class) ResponseEntity<Map<String,Object>> bad(Exception e){ return body(HttpStatus.BAD_REQUEST,"Malformed request body"); }
  @ExceptionHandler(Exception.class) ResponseEntity<Map<String,Object>> other(Exception e){ e.printStackTrace(); return body(HttpStatus.INTERNAL_SERVER_ERROR,"Something went wrong"); }
}
