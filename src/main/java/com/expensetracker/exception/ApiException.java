package com.expensetracker.exception;
import org.springframework.http.HttpStatus;
public class ApiException extends RuntimeException {
  public final HttpStatus status;
  public ApiException(HttpStatus s,String m){ super(m); status=s; }
  public static ApiException notFound(String m){ return new ApiException(HttpStatus.NOT_FOUND,m); }
  public static ApiException exists(String m){ return new ApiException(HttpStatus.CONFLICT,m); }
  public static ApiException invalidCredentials(){ return new ApiException(HttpStatus.UNAUTHORIZED,"Invalid email or password"); }
  public static ApiException invalid(String m){ return new ApiException(HttpStatus.BAD_REQUEST,m); }
}
