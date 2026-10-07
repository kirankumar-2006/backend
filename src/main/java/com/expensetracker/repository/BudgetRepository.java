package com.expensetracker.repository;
import com.expensetracker.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface BudgetRepository extends JpaRepository<Budget,Long> {
  List<Budget> findByUserOrderByYearDescMonthDesc(User u); Optional<Budget> findByIdAndUser(Long id,User u);
  Optional<Budget> findByUserAndMonthAndYear(User u,int m,int y); }
