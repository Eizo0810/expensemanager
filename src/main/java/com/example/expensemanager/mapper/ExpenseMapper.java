package com.example.expensemanager.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.expensemanager.dto.CategorySummary;
import com.example.expensemanager.entity.Expense;

@Mapper
public interface ExpenseMapper {

    List<Expense> findAll(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    Optional<Expense> findById(Long id);

    int countByCategoryId(Long categoryId);

    void insert(Expense expense);

    void update(Expense expense);

    void delete(Long id);

    List<CategorySummary> summarizeByCategory(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
