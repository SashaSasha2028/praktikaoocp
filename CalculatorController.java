package controller;

import model.CalculatorModel;
import view.CalculatorView;

public class CalculatorController {
    private CalculatorModel model;
    private CalculatorView view;

    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;
    }

    public void process() {
        String expression = view.getExpression();
        
        try {
            validateExpression(expression);
            double result = model.calculate(expression);
            view.displayResult(result);
        } catch (IllegalArgumentException e) {
            view.displayError(e.getMessage());
        }
    }

    private void validateExpression(String expression) throws IllegalArgumentException {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Выражение не может быть пустым");
        }
        
        // Проверка, что выражение начинается и заканчивается числом
        String trimmed = expression.replaceAll("\\s+", "");
        if (!trimmed.matches("^-?\\d+.*\\d+$")) {
            throw new IllegalArgumentException("Уравнение должно начинаться и заканчиваться числом");
        }
        
        // Проверка на количество операторов (не более 99 для 100 слагаемых)
        long operatorCount = trimmed.chars()
            .filter(c -> "+-*/^".indexOf(c) != -1)
            .count();
        
        if (operatorCount > 99) {
            throw new IllegalArgumentException("Количество операций превышает допустимый лимит (100 слагаемых)");
        }
        
        // Проверка скобок
        int balance = 0;
        for (char c : trimmed.toCharArray()) {
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) break;
        }
        
        if (balance != 0) {
            throw new IllegalArgumentException("Несбалансированные скобки в выражении");
        }
    }
}
