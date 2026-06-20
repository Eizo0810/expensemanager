package com.example.expensemanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.expensemanager.dto.CategorySummary;
import com.example.expensemanager.dto.MonthlySummary;
import com.example.expensemanager.entity.Expense;
import com.example.expensemanager.entity.ExpenseCategory;
import com.example.expensemanager.mapper.ExpenseMapper;

class ExpenseServiceTest {

    private final ExpenseMapper expenseMapper = mock(ExpenseMapper.class);
    private final ExpenseCategoryService categoryService = mock(ExpenseCategoryService.class);
    private final ExpenseService expenseService = new ExpenseService(expenseMapper, categoryService);

    @Test
    void registerInsertsExpenseWhenInputIsValid() {
        Expense expense = expense();
        when(categoryService.findById(1L)).thenReturn(new ExpenseCategory());

        expenseService.register(expense);

        verify(expenseMapper).insert(expense);
    }

    @Test
    void registerThrowsExceptionWhenAmountIsZero() {
        Expense expense = expense();
        expense.setAmount(BigDecimal.ZERO);
        when(categoryService.findById(1L)).thenReturn(new ExpenseCategory());

        assertThatThrownBy(() -> expenseService.register(expense))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("金額は1円以上で入力してください。");
    }

    @Test
    void summarizeReturnsMonthlyTotals() {
        CategorySummary food = new CategorySummary();
        food.setCategoryName("食費");
        food.setMonthlyBudget(new BigDecimal("50000"));
        food.setTotalAmount(new BigDecimal("12000"));

        CategorySummary transport = new CategorySummary();
        transport.setCategoryName("交通費");
        transport.setMonthlyBudget(new BigDecimal("15000"));
        transport.setTotalAmount(new BigDecimal("3000"));

        when(expenseMapper.summarizeByCategory(
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30)))
                .thenReturn(List.of(food, transport));

        MonthlySummary summary = expenseService.summarize(YearMonth.of(2026, 6));

        assertThat(summary.getTotalAmount()).isEqualByComparingTo("15000");
        assertThat(summary.getTotalBudget()).isEqualByComparingTo("65000");
        assertThat(summary.getRemainingBudget()).isEqualByComparingTo("50000");
    }

    private Expense expense() {
        Expense expense = new Expense();
        expense.setCategoryId(1L);
        expense.setExpenseDate(LocalDate.of(2026, 6, 20));
        expense.setAmount(new BigDecimal("1280"));
        expense.setDescription(" 昼食 ");
        expense.setPaymentMethod(" クレジットカード ");
        expense.setMemo(" サンプル ");
        return expense;
    }
}
