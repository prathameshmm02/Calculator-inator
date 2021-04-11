package com.example.calculator.Model;

public class History {
    String answer;
    String expr;

    public History(String expr2, String answer2) {
        this.expr = expr2;
        this.answer = answer2;
    }

    public String getExpr() {
        return this.expr;
    }

    public void setExpr(String expr2) {
        this.expr = expr2;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer2) {
        this.answer = answer2;
    }
}
