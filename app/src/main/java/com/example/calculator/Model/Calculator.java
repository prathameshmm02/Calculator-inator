package com.example.calculator.Model;

import android.util.Log;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Calculator {
    final List<Character> basicOperators = Arrays.asList('%', '÷', '×', '+', '-');
    final List<Character> operators = Arrays.asList('√', '!', '^', '%', '÷', '×', '+', '-');
    final List<Character> numbers = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.');
    final BigDecimal PI = BigDecimal.valueOf(Math.PI);
    final BigDecimal E = BigDecimal.valueOf(Math.E);
    final BigDecimal ROUNDER = new BigDecimal("1000000000000000");
    boolean addToOperand;

    public Calculator() {
    }

    public static boolean[] validateInput(int currentPosition, String buttonText, String oldInput) {
        final List<String> operators2 = Arrays.asList("^", "%", "÷", "×", "+", "-");
        final List<String> extraOperators = Arrays.asList("sin", "cos", "tan", "log", "ln", "sin⁻¹", "cos⁻¹", "tan⁻¹");
        final List<String> powOperators = Arrays.asList("^2", "e^", "10^");
        final List<String> numbers2 = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        boolean[] validations = new boolean[2];
        char lastChar = 0;
        if (!oldInput.isEmpty()) {
            if (currentPosition != 0) {
                lastChar = oldInput.charAt(currentPosition - 1);
            }
            if (extraOperators.contains(buttonText)) {
                validations[0] = true;
                validations[1] = true;
                return validations;
            } else if (powOperators.contains(buttonText)) {
                validations[0] = true;
                return validations;
            } else if (!operators2.contains(buttonText) || (!operators2.contains(String.valueOf(lastChar)) && lastChar != '.')) {
                validations[0] = true;
                return validations;
            } else if (!buttonText.equals("-") || (lastChar != 215 && lastChar != 247)) {
                return validations;
            } else {
                validations[0] = true;
                return validations;
            }
        } else if (extraOperators.contains(buttonText)) {
            validations[0] = true;
            validations[1] = true;
            return validations;
        } else if (powOperators.contains(buttonText)) {
            validations[0] = true;
            return validations;
        } else if (!buttonText.equals("-") && !buttonText.equals("e") && !buttonText.equals("π") && !buttonText.equals("(") && !buttonText.equals("√") && !numbers2.contains(buttonText)) {
            return validations;
        } else {
            validations[0] = true;
            return validations;
        }
    }


    public BigDecimal solve(String expr, boolean isDegree) {
        List<Character> operatorList = new ArrayList<>();
        List<BigDecimal> operandList = new ArrayList<>();
        String num = "";
        // Filtering Operands and Operators
        boolean isNegative = false;
        for (int i = 0; i < expr.length(); i++) {
            char previous = '|', next = '|';
            if (i != 0) {
                previous = expr.charAt(i - 1);
            }
            char current = expr.charAt(i);
            if (i != expr.length() - 1) {
                next = expr.charAt(i + 1);
            }
            if (numbers.contains(current)) {
                num = num + current;
            } else if (current == '√' || current == '!') {
                if (current == '√') {
                    addToOperand = true;
                }
                operatorList.add(current);
            } else if (current == 'π') {
                addToOperand = true;
                operandList.add(PI);
            } else if (current == 'e') {
                addToOperand = true;
                operandList.add(E);
            } else if (expr.startsWith("sin⁻¹", i)) {
                addToOperand = true;
                int index = expr.indexOf(")", i);
                double value = Double.parseDouble(expr.substring(i + 6, index));
                operandList.add(convertAngle(BigDecimal.valueOf(Math.asin(value)), isDegree));
                i = index;
            } else if (expr.startsWith("cos⁻¹", i)) {
                addToOperand = true;
                int index = expr.indexOf(")", i);
                double value = convertAngle(expr.substring(i + 6, index), isDegree);
                operandList.add(convertAngle(BigDecimal.valueOf(Math.acos(value)), isDegree));
                i = index;
            } else if (expr.startsWith("tan⁻¹", i)) {
                addToOperand = true;
                int index = expr.indexOf(")", i);
                double value = convertAngle(expr.substring(i + 6, index), isDegree);
                operandList.add(convertAngle(BigDecimal.valueOf(Math.atan(value)), isDegree));
                i = index;
            } else if (expr.startsWith("sin", i)) {
                addToOperand = true;
                int index = expr.indexOf(")", i);
                double angle = convertAngle(expr.substring(i + 4, index), isDegree);
                operandList.add(BigDecimal.valueOf(Math.sin(angle)));
                i = index;
            } else if (expr.startsWith("cos", i)) {
                addToOperand = true;
                int index = expr.indexOf(")", i);
                double angle = convertAngle(expr.substring(i + 4, index), isDegree);
                operandList.add(BigDecimal.valueOf(Math.cos(angle)));
                i = index;
            } else if (expr.startsWith("tan", i)) {
                addToOperand = true;
                int index = expr.indexOf(")", i);
                double angle = convertAngle(expr.substring(i + 4, index), isDegree);
                operandList.add(BigDecimal.valueOf(Math.tan(angle)));
                i = index;
            } else if (expr.startsWith("log", i)) {
                addToOperand = true;
                int index = expr.indexOf(")", i);
                operandList.add(BigDecimal.valueOf(Math.log10(Double.parseDouble(expr.substring(i + 4, index)))));
                i = index;
            } else if (expr.startsWith("ln", i)) {
                addToOperand = true;
                int index = expr.indexOf(")", i);
                operandList.add(BigDecimal.valueOf(Math.log(Double.parseDouble(expr.substring(i + 3, index)))));
                i = index;
            } else if (current == '(' || current == ')') {
                addToOperand = true;
            } else {
                if (!num.isEmpty()) {
                    if (!isNegative) {
                        operandList.add(BigDecimal.valueOf(Double.parseDouble(num)));
                    } else {
                        isNegative = false;
                        operandList.add(BigDecimal.valueOf(Double.parseDouble(num)).negate());
                    }
                }
                if (current == '-') {
                    isNegative = true;
                    //char lastOperation = operatorList.get(operatorList.size()-1);
                    if (i != 0) {
                        char lastChar = expr.charAt(i - 1);
                        if (lastChar != '×' && lastChar != '÷') {
                            operatorList.add('+');
                        }
                    }
                } else {
                    if (operators.contains(current)) {
                        operatorList.add(current);
                    }
                }
                num = "";
            }
            if (addToOperand) {
                if (!num.isEmpty()) {
                    if (!isNegative) {
                        operandList.add(BigDecimal.valueOf(Double.parseDouble(num)));
                    } else {
                        isNegative = false;
                        operandList.add(BigDecimal.valueOf(Double.parseDouble(num)).negate());
                    }
                    num = "";
                }
                if (current != ')' && (!(basicOperators.contains(previous) || basicOperators.contains(next) || previous == '|' || next == '|'))) {
                    operatorList.add('×');
                }
                addToOperand = false;
            }
        }
        // Adding last operand
        if (!num.isEmpty()) {
            if (!isNegative) {
                Log.i("Num", num);
                operandList.add(new BigDecimal(num));
            } else {
                operandList.add(new BigDecimal(num).negate());
            }
        }

        // Solving (Actually)
        for (char operator : operators) {
            BigDecimal answer;
            BigDecimal num1;
            BigDecimal num2;
            while (operatorList.contains(operator)) {
                int index = operatorList.indexOf(operator);
                num1 = operandList.get(index);
                if (operator == '√') {
                    answer = BigDecimal.valueOf(Math.sqrt(num1.doubleValue()));
                } else if (operator == '!') {
                    answer = new BigDecimal(factorial(num1.toBigInteger()));
                } else {
                    try {
                        num2 = operandList.get(index + 1);
                    } catch (IndexOutOfBoundsException e) {
                        return num1;
                    }
                    if (operator == '÷') {
                        answer = num1.divide(num2, MathContext.DECIMAL128);
                    } else if (operator == '×') {
                        answer = num1.multiply(num2);
                    } else if (operator == '+') {
                        answer = num1.add(num2);
                    } else if (operator == '-') {
                        answer = num1.subtract(num2);
                    } else if (operator == '^') {

                        answer = BigDecimal.valueOf(Math.pow(num1.doubleValue(), num2.doubleValue()));
                    } else if (operator == '%') {
                        answer = num1.multiply(num2).divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);
                    } else {
                        answer = num1;
                    }
                    operandList.remove(index);
                }
                operatorList.remove(index);
                operandList.remove(index);
                operandList.add(index, answer);
            }
        }
        return (operandList.get(0).multiply(ROUNDER)).setScale(0, RoundingMode.HALF_UP).divide(ROUNDER);
    }

    //This will solve all brackets for ya
    private String solveBrackets(String expr, boolean isDegree) {
        Stack<Integer> indices = new Stack<>();
        BigDecimal result;
        int index = 0;
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '(') {
                indices.push(i);
            } else if (expr.charAt(i) == ')') {
                if (!indices.isEmpty()) {
                    index = indices.pop();
                }

                if (expr.contains(")")) {
                    result = eval(expr.substring(index + 1, i), isDegree);
                } else {
                    result = new BigDecimal(expr.substring(index + 1, i));
                }
                if (indices.isEmpty()) {
                    expr = expr.substring(0, index + 1) + result.toString() + expr.substring(i);
                } else {
                    expr = expr.substring(0, index) + result.toString() + expr.substring(i + 1);
                }
            }
            if (i == expr.length() - 1 && !indices.isEmpty()) {
                index = indices.pop();
                result = eval(expr.substring(index + 1), isDegree);
                expr = expr.substring(0, index) + result.toString();
            }
        }
        return expr;
    }

    //this is actually like a Javascript eval function
    public BigDecimal eval(String expr, boolean isDegree) {
        return solve(solveBrackets(expr, isDegree), isDegree);
    }

    public BigInteger factorial(BigInteger number) {
        BigInteger fact = BigInteger.ONE;
        for (long factor = 2; factor <= number.longValue(); factor++) {
            fact = fact.multiply(BigInteger.valueOf(factor));
        }
        return fact;
    }

    private double convertAngle(String angleString, boolean isDegree) {
        double angle = Double.parseDouble(angleString);
        if (isDegree) {
            return Math.toRadians(angle);
        } else {
            return angle;
        }
    }

    private BigDecimal convertAngle(BigDecimal angleBig, boolean isDegree) {
        double angle = angleBig.doubleValue();
        if (isDegree) {
            return BigDecimal.valueOf(Math.toRadians(angle));
        } else {
            return BigDecimal.valueOf(angle);
        }
    }

}