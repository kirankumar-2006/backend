package com.expensetracker.entity;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="users")
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @Column(nullable=false) public String name;
  @Column(nullable=false,unique=true) public String email;
  @Column(nullable=false) public String password;
  public LocalDateTime createdAt=LocalDateTime.now();
}
