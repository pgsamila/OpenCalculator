package com.pocketkit.simplecalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val themeManager: ThemeRepository
): ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    val theme: StateFlow<AppTheme> = themeManager.theme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.System)

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            themeManager.setTheme(theme)
        }
    }

    fun onAction(action: CalculatorAction) {
        when(action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterString(".")
            is CalculatorAction.Clear -> state = CalculatorState(history = state.history, isExpanded = state.isExpanded, isDegree = state.isDegree) // Clear expression but keep history and settings
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Delete -> performDeletion()
            is CalculatorAction.Parentheses -> performParentheses()
            is CalculatorAction.ToggleExpanded -> state = state.copy(isExpanded = !state.isExpanded)
            is CalculatorAction.ToggleDegree -> state = state.copy(isDegree = !state.isDegree)
            is CalculatorAction.Inverse -> enterString("^(-1)")
            is CalculatorAction.ClearHistory -> state = state.copy(history = emptyList())
        }
    }

    fun onHistoryClick(item: HistoryItem) {
        // Replace current expression with history item's expression
        state = state.copy(
            expression = item.expression,
            result = "" // Clear result as we are editing the expression
        )
    }

    private fun performParentheses() {
        val openCount = state.expression.count { it == '(' }
        val closeCount = state.expression.count { it == ')' }
        
        if (openCount > closeCount && state.expression.isNotEmpty() && 
            (state.expression.last().isDigit() || state.expression.last() == ')' || state.expression.last() == 'π' || state.expression.last() == 'e' || state.expression.last() == '!')) {
            enterString(")")
        } else {
            enterString("(")
        }
    }

    private fun performDeletion() {
        if (state.expression.isNotEmpty()) {
            // Handle multi-char deletions (sin, cos, etc.)
            val funcs = listOf("sin(", "cos(", "tan(", "ln(", "log(", "nan", "inf", "error")
            var deleted = false
            for (f in funcs) {
                if (state.expression.endsWith(f)) {
                    state = state.copy(expression = state.expression.dropLast(f.length))
                    deleted = true
                    break
                }
            }
            if (!deleted) {
                state = state.copy(expression = state.expression.dropLast(1))
            }
        }
    }

    private fun performCalculation() {
        if (state.expression.isBlank()) return
        
        try {
            val result = ExpressionEvaluator.evaluate(state.expression, state.isDegree)
            val resultString = if (result.isNaN()) {
                "Error"
            } else if (result % 1.0 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }
            
            // Add current expression and result to history
            val historyItem = HistoryItem(state.expression, resultString)
            val newHistory = state.history + historyItem
            
            state = state.copy(
                expression = resultString, // Result becomes the new expression start
                result = resultString,
                history = newHistory
            )
        } catch (e: Exception) {
            state = state.copy(
                result = "Error"
            )
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.expression.isNotEmpty()) {
            val lastChar = state.expression.last()
            if (isBinaryOperator(lastChar)) {
                // Replace the last operator
                state = state.copy(
                    expression = state.expression.dropLast(1) + operation.symbol
                )
                return
            }
        }

        when (operation) {
            CalculatorOperation.Sin, CalculatorOperation.Cos, CalculatorOperation.Tan, 
            CalculatorOperation.Ln, CalculatorOperation.Log, CalculatorOperation.SquareRoot -> {
                enterString(operation.symbol + "(")
            }
            else -> {
                enterString(operation.symbol)
            }
        }
    }

    private fun isBinaryOperator(c: Char): Boolean {
        return c == '+' || c == '-' || c == 'x' || c == '/' || c == '^'
    }

    private fun enterString(str: String) {
        state = state.copy(
            expression = state.expression + str
        )
    }

    private fun enterNumber(number: Int) {
        state = state.copy(
            expression = state.expression + number
        )
    }
}
