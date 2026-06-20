package com.example.expensemanager.controller;

import java.time.YearMonth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.expensemanager.service.ExpenseService;

@Controller
public class SummaryController {

    private final ExpenseService expenseService;

    public SummaryController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/summary")
    public String monthly(
            @RequestParam(required = false) YearMonth month,
            Model model) {
        model.addAttribute("summary", expenseService.summarize(month));
        return "summary/monthly";
    }
}
