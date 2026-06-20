package com.example.expensemanager.controller;

import java.time.LocalDate;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.expensemanager.entity.Expense;
import com.example.expensemanager.service.ExpenseCategoryService;
import com.example.expensemanager.service.ExpenseCsvService;
import com.example.expensemanager.service.ExpenseService;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseCategoryService categoryService;
    private final ExpenseCsvService expenseCsvService;

    public ExpenseController(
            ExpenseService expenseService,
            ExpenseCategoryService categoryService,
            ExpenseCsvService expenseCsvService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.expenseCsvService = expenseCsvService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/expenses";
    }

    @GetMapping("/expenses")
    public String index(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Model model) {
        model.addAttribute("expenses", expenseService.search(keyword, categoryId, startDate, endDate));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "expenses/index";
    }

    @GetMapping("/expenses/export")
    public ResponseEntity<byte[]> export(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        byte[] csv = expenseCsvService.export(
                expenseService.search(keyword, categoryId, startDate, endDate));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("expenses.csv")
                                .build()
                                .toString())
                .contentType(new MediaType("text", "csv"))
                .body(csv);
    }

    @GetMapping("/expenses/new")
    public String newForm(Model model) {
        model.addAttribute("expense", new Expense());
        model.addAttribute("categories", categoryService.findAll());
        return "expenses/new";
    }

    @PostMapping("/expenses")
    public String create(Expense expense, Model model) {
        try {
            expenseService.register(expense);
        } catch (IllegalArgumentException e) {
            model.addAttribute("expense", expense);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("error", e.getMessage());
            return "expenses/new";
        }

        return "redirect:/expenses?registered";
    }

    @GetMapping("/expenses/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("expense", expenseService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "expenses/edit";
    }

    @PostMapping("/expenses/{id}")
    public String update(
            @PathVariable Long id,
            Expense expense,
            Model model) {
        try {
            expenseService.update(id, expense);
        } catch (IllegalArgumentException e) {
            expense.setId(id);
            model.addAttribute("expense", expense);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("error", e.getMessage());
            return "expenses/edit";
        }

        return "redirect:/expenses?updated";
    }

    @PostMapping("/expenses/{id}/delete")
    public String delete(@PathVariable Long id) {
        expenseService.delete(id);
        return "redirect:/expenses?deleted";
    }
}
