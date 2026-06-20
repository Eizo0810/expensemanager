package com.example.expensemanager.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class MonthlySummary {

    private YearMonth targetMonth;
    private BigDecimal totalAmount;
    private BigDecimal totalBudget;
    private List<CategorySummary> categorySummaries;

    public YearMonth getTargetMonth() {
        return targetMonth;
    }

    public void setTargetMonth(YearMonth targetMonth) {
        this.targetMonth = targetMonth;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public List<CategorySummary> getCategorySummaries() {
        return categorySummaries;
    }

    public void setCategorySummaries(List<CategorySummary> categorySummaries) {
        this.categorySummaries = categorySummaries;
    }

    public BigDecimal getRemainingBudget() {
        if (totalBudget == null || totalAmount == null) {
            return BigDecimal.ZERO;
        }

        return totalBudget.subtract(totalAmount);
    }

    public boolean isOverBudget() {
        return totalBudget != null
                && totalAmount != null
                && totalAmount.compareTo(totalBudget) > 0;
    }
}
