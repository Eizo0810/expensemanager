package com.example.expensemanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.expensemanager.entity.ExpenseCategory;
import com.example.expensemanager.service.ExpenseCategoryService;

@Controller
public class CategoryController {

    private final ExpenseCategoryService categoryService;

    public CategoryController(ExpenseCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String index(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories/index";
    }

    @GetMapping("/categories/new")
    public String newForm(Model model) {
        model.addAttribute("category", new ExpenseCategory());
        return "categories/new";
    }

    @PostMapping("/categories")
    public String create(ExpenseCategory category, Model model) {
        try {
            categoryService.register(category);
        } catch (IllegalArgumentException e) {
            model.addAttribute("category", category);
            model.addAttribute("error", e.getMessage());
            return "categories/new";
        }

        return "redirect:/categories?registered";
    }

    @GetMapping("/categories/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "categories/edit";
    }

    @PostMapping("/categories/{id}")
    public String update(
            @PathVariable Long id,
            ExpenseCategory category,
            Model model) {
        try {
            categoryService.update(id, category);
        } catch (IllegalArgumentException e) {
            category.setId(id);
            model.addAttribute("category", category);
            model.addAttribute("error", e.getMessage());
            return "categories/edit";
        }

        return "redirect:/categories?updated";
    }

    @PostMapping("/categories/{id}/delete")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/categories";
        }

        return "redirect:/categories?deleted";
    }
}
