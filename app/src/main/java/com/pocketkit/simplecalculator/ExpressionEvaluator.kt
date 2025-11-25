package com.pocketkit.simplecalculator

import java.util.Stack
import kotlin.math.*

object ExpressionEvaluator {

    fun evaluate(expression: String, isDegree: Boolean = true): Double {
        val tokens = tokenize(expression)
        val postfix = infixToPostfix(tokens)
        return evaluatePostfix(postfix, isDegree)
    }

    private fun tokenize(expression: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        while (i < expression.length) {
            val c = expression[i]
            when {
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < expression.length && (expression[i].isDigit() || expression[i] == '.')) {
                        sb.append(expression[i])
                        i++
                    }
                    tokens.add(sb.toString())
                    continue
                }
                c == '+' || c == '-' || c == 'x' || c == '/' || c == '(' || c == ')' || c == '%' || c == '^' || c == '!' -> {
                    // Implicit multiplication handling: 2(3) -> 2*3, )3 -> )*3
                    if (c == '(' && i > 0 && (expression[i-1].isDigit() || expression[i-1] == ')' || expression[i-1] == 'π' || expression[i-1] == 'e' || expression[i-1] == '!')) {
                        tokens.add("x")
                    }
                    tokens.add(c.toString())
                }
                c == 'π' || c == 'e' -> {
                     // Implicit multiplication: 2π -> 2*π
                    if (tokens.isNotEmpty() && (tokens.last().last().isDigit() || tokens.last() == ")" || tokens.last() == "π" || tokens.last() == "e" || tokens.last() == "!")) {
                         tokens.add("x")
                    }
                    tokens.add(c.toString())
                }
                c == '√' -> {
                     // Implicit multiplication: 2√ -> 2*√
                    if (tokens.isNotEmpty() && (tokens.last().last().isDigit() || tokens.last() == ")" || tokens.last() == "π" || tokens.last() == "e" || tokens.last() == "!")) {
                         tokens.add("x")
                    }
                    tokens.add(c.toString())
                }
                c.isLetter() -> {
                    val sb = StringBuilder()
                    while (i < expression.length && expression[i].isLetter()) {
                        sb.append(expression[i])
                        i++
                    }
                    val word = sb.toString()
                    // Implicit multiplication: 2sin -> 2*sin
                    if (tokens.isNotEmpty() && (tokens.last().last().isDigit() || tokens.last() == ")" || tokens.last() == "π" || tokens.last() == "e" || tokens.last() == "!")) {
                         tokens.add("x")
                    }
                    tokens.add(word)
                    continue
                }
            }
            i++
        }
        return tokens
    }

    private fun infixToPostfix(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val stack = Stack<String>()

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> output.add(token)
                token == "π" || token == "e" -> output.add(token)
                token == "(" -> stack.push(token)
                token == ")" -> {
                    while (stack.isNotEmpty() && stack.peek() != "(") {
                        output.add(stack.pop())
                    }
                    if (stack.isNotEmpty()) stack.pop() // Pop '('
                    // If top of stack is a function, pop it too
                    if (stack.isNotEmpty() && isFunction(stack.peek())) {
                        output.add(stack.pop())
                    }
                }
                isFunction(token) -> stack.push(token)
                else -> { // Operator
                    while (stack.isNotEmpty() && precedence(stack.peek()) >= precedence(token)) {
                        output.add(stack.pop())
                    }
                    stack.push(token)
                }
            }
        }
        while (stack.isNotEmpty()) {
            val top = stack.pop()
            if (top != "(") {
                output.add(top)
            }
        }
        return output
    }

    private fun evaluatePostfix(postfix: List<String>, isDegree: Boolean): Double {
        val stack = Stack<Double>()
        for (token in postfix) {
            if (token.toDoubleOrNull() != null) {
                stack.push(token.toDouble())
            } else if (token == "π") {
                stack.push(Math.PI)
            } else if (token == "e") {
                stack.push(Math.E)
            } else if (isFunction(token)) {
                 val val1 = if (stack.isNotEmpty()) stack.pop() else 0.0
                 when (token) {
                     "sin" -> stack.push(if (isDegree) sin(Math.toRadians(val1)) else sin(val1))
                     "cos" -> stack.push(if (isDegree) cos(Math.toRadians(val1)) else cos(val1))
                     "tan" -> stack.push(if (isDegree) tan(Math.toRadians(val1)) else tan(val1))
                     "ln" -> stack.push(ln(val1))
                     "log" -> stack.push(log10(val1))
                     "√" -> stack.push(sqrt(val1))
                 }
            } else if (token == "!") {
                 val val1 = if (stack.isNotEmpty()) stack.pop() else 0.0
                 stack.push(factorial(val1))
            } else if (token == "%") {
                 val val1 = if (stack.isNotEmpty()) stack.pop() else 0.0
                 stack.push(val1 / 100.0)
            } else {
                val val2 = if (stack.isNotEmpty()) stack.pop() else 0.0
                val val1 = if (stack.isNotEmpty()) stack.pop() else 0.0
                when (token) {
                    "+" -> stack.push(val1 + val2)
                    "-" -> stack.push(val1 - val2)
                    "x" -> stack.push(val1 * val2)
                    "/" -> stack.push(val1 / val2)
                    "^" -> stack.push(val1.pow(val2))
                }
            }
        }
        return if (stack.isNotEmpty()) stack.pop() else 0.0
    }

    private fun factorial(n: Double): Double {
        if (n < 0) return Double.NaN
        if (n == 0.0 || n == 1.0) return 1.0
        var result = 1.0
        for (i in 2..n.toInt()) {
            result *= i
        }
        return result
    }

    private fun isFunction(token: String): Boolean {
        return token == "sin" || token == "cos" || token == "tan" || token == "ln" || token == "log" || token == "√"
    }

    private fun precedence(op: String): Int {
        return when (op) {
            "sin", "cos", "tan", "ln", "log", "√" -> 4
            "^", "!" -> 3
            "%", "x", "/" -> 2
            "+", "-" -> 1
            else -> 0
        }
    }
}
