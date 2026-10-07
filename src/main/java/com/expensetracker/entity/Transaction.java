package com.expensetracker.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*; import java.time.*;
@Entity @Table(name="transactions")
public class Transaction {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="user_id") @JsonIgnore public User user;
  @Column(nullable=false) public String type;
  @Column(nullable=false) public double amount;
  @Column(nullable=false) public String category;
  @Column(name="tx_date",nullable=false) public LocalDate date;
  public String paymentMethod;
  public String description;
  public LocalDateTime createdAt=LocalDateTime.now();
}
