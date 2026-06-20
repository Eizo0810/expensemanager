package com.example.expensemanager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expensemanager.dto.CategorySummary;
import com.example.expensemanager.dto.MonthlySummary;
import com.example.expensemanager.entity.Expense;
import com.example.expensemanager.mapper.ExpenseMapper;

@Service
public class ExpenseService {

    private final ExpenseMapper expenseMapper;
    private final ExpenseCategoryService categoryService;

    public ExpenseService(
            ExpenseMapper expenseMapper,
            ExpenseCategoryService categoryService) {
        this.expenseMapper = expenseMapper;
        this.categoryService = categoryService;
    }

    public List<Expense> search(
            String keyword,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate) {
        return expenseMapper.findAll(
                normalizeKeyword(keyword),
                categoryId,
                startDate,
                endDate);
    }

    public Expense findById(Long id) {
        return expenseMapper.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("経費が見つかりません。"));
    }

    public MonthlySummary summarize(YearMonth targetMonth) {
        YearMonth month = targetMonth == null ? YearMonth.now() : targetMonth;
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();
        List<CategorySummary> summaries = expenseMapper.summarizeByCategory(startDate, endDate);

        BigDecimal totalAmount = summaries.stream()
                .map(CategorySummary::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalBudget = summaries.stream()
                .map(CategorySummary::getMonthlyBudget)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        MonthlySummary summary = new MonthlySummary();
        summary.setTargetMonth(month);
        summary.setTotalAmount(totalAmount);
        summary.setTotalBudget(totalBudget);
        summary.setCategorySummaries(summaries);
        return summary;
    }

    @Transactional
    public void register(Expense expense) {
        normalize(expense);
        expenseMapper.insert(expense);
    }

    @Transactional
    public void update(Long id, Expense expense) {
        findById(id);
        expense.setId(id);
        normalize(expense);
        expenseMapper.update(expense);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        expenseMapper.delete(id);
    }

    private void normalize(Expense expense) {
        if (expense.getCategoryId() == null) {
            throw new IllegalArgumentException("カテゴリを選択してください。");
        }
        categoryService.findById(expense.getCategoryId());

        if (expense.getExpenseDate() == null) {
            throw new IllegalArgumentException("利用日を入力してください。");
        }
        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("金額は1円以上で入力してください。");
        }

        expense.setDescription(normalizeRequired(expense.getDescription(), "内容"));
        expense.setPaymentMethod(normalizeOptional(expense.getPaymentMethod()));
        expense.setMemo(normalizeOptional(expense.getMemo()));
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return keyword.trim();
    }

    private String normalizeRequired(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + "を入力してください。");
        }

        return value.trim();
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        return value.trim();
    }
}
