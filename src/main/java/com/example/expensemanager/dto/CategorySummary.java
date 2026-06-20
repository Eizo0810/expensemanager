package com.example.expensemanager.dto;

import java.math.BigDecimal;

public class CategorySummary {

    private Long categoryId;
    private String categoryName;
    private BigDecimal monthlyBudget;
    private BigDecimal totalAmount;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(BigDecimal monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getRemainingBudget() {
        if (monthlyBudget == null || totalAmount == null) {
            return BigDecimal.ZERO;
        }

        return monthlyBudget.subtract(totalAmount);
    }

    public boolean isOverBudget() {
        return monthlyBudget != null
                && totalAmount != null
                && totalAmount.compareTo(monthlyBudget) > 0;
    }
}
