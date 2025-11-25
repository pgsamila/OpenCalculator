package com.pocketkit.simplecalculator

sealed class CalculatorAction {
    data class Number(val number: Int): CalculatorAction()
    object Clear: CalculatorAction()
    object Delete: CalculatorAction()
    object Decimal: CalculatorAction()
    object Calculate: CalculatorAction()
    data class Operation(val operation: CalculatorOperation): CalculatorAction()
    object Parentheses: CalculatorAction()
    object ToggleExpanded: CalculatorAction()
    object ToggleDegree: CalculatorAction()
    object Inverse: CalculatorAction()
    object ClearHistory: CalculatorAction()
}
