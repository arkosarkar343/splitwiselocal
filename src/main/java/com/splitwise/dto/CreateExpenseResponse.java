package com.splitwise.dto;

import java.util.List;

public record CreateExpenseResponse (List<Expense> expenses, Errors errors) {}


