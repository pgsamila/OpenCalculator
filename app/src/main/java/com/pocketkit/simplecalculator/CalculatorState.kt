package com.pocketkit.simplecalculator

data class CalculatorState(
    val expression: String = "",
    val result: String = "",
    val history: List<HistoryItem> = emptyList(),
    val isExpanded: Boolean = false,
    val isDegree: Boolean = true
)
