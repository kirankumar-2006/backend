package com.expensetracker.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="budgets",uniqueConstraints=@UniqueConstraint(columnNames={"user_id","month","year"}))
public class Budget {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="user_id") @JsonIgnore public User user;
  @Column(name="month",nullable=false) public int month;
  @Column(name="year",nullable=false) public int year;
  @Column(nullable=false) public double amount;
  public LocalDateTime createdAt=LocalDateTime.now();
}
