package com.example.expensemanager.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.example.expensemanager.entity.ExpenseCategory;
import com.example.expensemanager.mapper.ExpenseCategoryMapper;
import com.example.expensemanager.mapper.ExpenseMapper;

class ExpenseCategoryServiceTest {

    private final ExpenseCategoryMapper categoryMapper = mock(ExpenseCategoryMapper.class);
    private final ExpenseMapper expenseMapper = mock(ExpenseMapper.class);
    private final ExpenseCategoryService service =
            new ExpenseCategoryService(categoryMapper, expenseMapper);

    @Test
    void registerInsertsCategoryWhenInputIsValid() {
        ExpenseCategory category = new ExpenseCategory();
        category.setName(" 食費 ");
        category.setMonthlyBudget(new BigDecimal("50000"));
        category.setDisplayOrder(10);

        when(categoryMapper.findByName("食費")).thenReturn(Optional.empty());

        service.register(category);

        verify(categoryMapper).insert(category);
    }

    @Test
    void registerThrowsExceptionWhenNameIsDuplicated() {
        ExpenseCategory category = new ExpenseCategory();
        category.setName("食費");
        category.setMonthlyBudget(BigDecimal.ZERO);

        when(categoryMapper.findByName("食費")).thenReturn(Optional.of(new ExpenseCategory()));

        assertThatThrownBy(() -> service.register(category))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("このカテゴリ名はすでに登録されています。");
    }

    @Test
    void deleteThrowsExceptionWhenExpenseExists() {
        ExpenseCategory category = new ExpenseCategory();
        category.setId(1L);

        when(categoryMapper.findById(1L)).thenReturn(Optional.of(category));
        when(expenseMapper.countByCategoryId(1L)).thenReturn(1);

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("経費が登録されているカテゴリは削除できません。");
    }
}
