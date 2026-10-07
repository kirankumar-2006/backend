package com.expensetracker.repository;
import com.expensetracker.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
  List<Transaction> findByUserOrderByDateDesc(User u); Optional<Transaction> findByIdAndUser(Long id,User u); }
