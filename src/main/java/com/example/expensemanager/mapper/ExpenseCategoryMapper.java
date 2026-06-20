package com.example.expensemanager.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.expensemanager.entity.ExpenseCategory;

@Mapper
public interface ExpenseCategoryMapper {

    List<ExpenseCategory> findAll();

    Optional<ExpenseCategory> findById(Long id);

    Optional<ExpenseCategory> findByName(String name);

    void insert(ExpenseCategory category);

    void update(ExpenseCategory category);

    void delete(Long id);
}
