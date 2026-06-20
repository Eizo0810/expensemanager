package com.example.expensemanager.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expensemanager.entity.ExpenseCategory;
import com.example.expensemanager.mapper.ExpenseCategoryMapper;
import com.example.expensemanager.mapper.ExpenseMapper;

@Service
public class ExpenseCategoryService {

    private final ExpenseCategoryMapper categoryMapper;
    private final ExpenseMapper expenseMapper;

    public ExpenseCategoryService(
            ExpenseCategoryMapper categoryMapper,
            ExpenseMapper expenseMapper) {
        this.categoryMapper = categoryMapper;
        this.expenseMapper = expenseMapper;
    }

    public List<ExpenseCategory> findAll() {
        return categoryMapper.findAll();
    }

    public ExpenseCategory findById(Long id) {
        return categoryMapper.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("カテゴリが見つかりません。"));
    }

    @Transactional
    public void register(ExpenseCategory category) {
        normalize(category);

        if (categoryMapper.findByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("このカテゴリ名はすでに登録されています。");
        }

        categoryMapper.insert(category);
    }

    @Transactional
    public void update(Long id, ExpenseCategory category) {
        findById(id);
        category.setId(id);
        normalize(category);

        categoryMapper.findByName(category.getName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("このカテゴリ名はすでに登録されています。");
                });

        categoryMapper.update(category);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);

        if (expenseMapper.countByCategoryId(id) > 0) {
            throw new IllegalArgumentException("経費が登録されているカテゴリは削除できません。");
        }

        categoryMapper.delete(id);
    }

    private void normalize(ExpenseCategory category) {
        category.setName(normalizeRequired(category.getName(), "カテゴリ名"));

        if (category.getMonthlyBudget() == null) {
            category.setMonthlyBudget(BigDecimal.ZERO);
        }
        if (category.getMonthlyBudget().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("月予算は0以上で入力してください。");
        }

        if (category.getDisplayOrder() == null || category.getDisplayOrder() < 0) {
            category.setDisplayOrder(0);
        }
    }

    private String normalizeRequired(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + "を入力してください。");
        }

        return value.trim();
    }
}
