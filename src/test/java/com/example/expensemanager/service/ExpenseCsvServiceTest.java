package com.example.expensemanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.expensemanager.entity.Expense;

class ExpenseCsvServiceTest {

    private final ExpenseCsvService expenseCsvService = new ExpenseCsvService();

    @Test
    void exportCreatesUtf8CsvWithExpenseRows() {
        Expense expense = new Expense();
        expense.setExpenseDate(LocalDate.of(2026, 6, 20));
        expense.setCategoryName("食費");
        expense.setDescription("昼食,コーヒー");
        expense.setAmount(new BigDecimal("1280"));
        expense.setPaymentMethod("クレジットカード");
        expense.setMemo("外出先");

        String csv = new String(
                expenseCsvService.export(List.of(expense)),
                StandardCharsets.UTF_8);

        assertThat(csv).startsWith("\uFEFF利用日,カテゴリ,内容,金額,支払方法,メモ");
        assertThat(csv).contains("2026-06-20,食費,\"昼食,コーヒー\",1280,クレジットカード,外出先");
    }
}
