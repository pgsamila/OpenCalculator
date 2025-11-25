package com.pocketkit.simplecalculator

sealed class CalculatorOperation(val symbol: String) {
    object Add: CalculatorOperation("+")
    object Subtract: CalculatorOperation("-")
    object Multiply: CalculatorOperation("x")
    object Divide: CalculatorOperation("/")
    object Percentage: CalculatorOperation("%")
    object Power: CalculatorOperation("^")
    object Factorial: CalculatorOperation("!")
    object SquareRoot: CalculatorOperation("√")
    object Sin: CalculatorOperation("sin")
    object Cos: CalculatorOperation("cos")
    object Tan: CalculatorOperation("tan")
    object Ln: CalculatorOperation("ln")
    object Log: CalculatorOperation("log")
    object Pi: CalculatorOperation("π")
    object E: CalculatorOperation("e")
}
