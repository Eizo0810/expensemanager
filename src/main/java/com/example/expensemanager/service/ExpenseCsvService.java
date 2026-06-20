package com.example.expensemanager.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.expensemanager.entity.Expense;

@Service
public class ExpenseCsvService {

    public byte[] export(List<Expense> expenses) {
        StringBuilder csv = new StringBuilder();
        csv.append('\uFEFF');
        csv.append("利用日,カテゴリ,内容,金額,支払方法,メモ\r\n");

        for (Expense expense : expenses) {
            csv.append(escape(String.valueOf(expense.getExpenseDate()))).append(',')
                    .append(escape(expense.getCategoryName())).append(',')
                    .append(escape(expense.getDescription())).append(',')
                    .append(expense.getAmount()).append(',')
                    .append(escape(expense.getPaymentMethod())).append(',')
                    .append(escape(expense.getMemo()))
                    .append("\r\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        if (value.contains(",") || value.contains("\"")
                || value.contains("\r") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }
}
