package com.pocketkit.simplecalculator

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeThemeRepository : ThemeRepository {
    private val _theme = MutableStateFlow(AppTheme.System)
    override val theme: Flow<AppTheme> = _theme

    override suspend fun setTheme(theme: AppTheme) {
        _theme.value = theme
    }
}

class CalculatorViewModelTest {

    private val themeRepository = FakeThemeRepository()
    private val viewModel = CalculatorViewModel(themeRepository)

    @Test
    fun testEnterNumber() {
        viewModel.onAction(CalculatorAction.Number(1))
        viewModel.onAction(CalculatorAction.Number(2))
        assertEquals("12", viewModel.state.expression)
    }

    @Test
    fun testEnterOperation() {
        viewModel.onAction(CalculatorAction.Number(1))
        viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Add))
        viewModel.onAction(CalculatorAction.Number(2))
        assertEquals("1+2", viewModel.state.expression)
    }

    @Test
    fun testConsecutiveOperators() {
        viewModel.onAction(CalculatorAction.Number(2))
        viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Multiply))
        viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Multiply)) // Should replace previous
        assertEquals("2x", viewModel.state.expression)
        
        viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Add)) // Should replace x
        assertEquals("2+", viewModel.state.expression)
    }

    @Test
    fun testCalculate() {
        viewModel.onAction(CalculatorAction.Number(2))
        viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Add))
        viewModel.onAction(CalculatorAction.Number(2))
        viewModel.onAction(CalculatorAction.Calculate)
        
        assertEquals("4", viewModel.state.result)
        assertEquals("4", viewModel.state.expression)
        assertEquals(1, viewModel.state.history.size)
        assertEquals("2+2", viewModel.state.history[0].expression)
        assertEquals("4", viewModel.state.history[0].result)
    }

    @Test
    fun testClear() {
        viewModel.onAction(CalculatorAction.Number(5))
        viewModel.onAction(CalculatorAction.Clear)
        assertEquals("", viewModel.state.expression)
    }

    @Test
    fun testClearHistory() {
        viewModel.onAction(CalculatorAction.Number(2))
        viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Add))
        viewModel.onAction(CalculatorAction.Number(2))
        viewModel.onAction(CalculatorAction.Calculate)
        
        assertEquals(1, viewModel.state.history.size)
        
        viewModel.onAction(CalculatorAction.ClearHistory)
        assertEquals(0, viewModel.state.history.size)
    }

    @Test
    fun testScientificOperations() {
        viewModel.onAction(CalculatorAction.ToggleExpanded)
        assertTrue(viewModel.state.isExpanded)
        
        viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.Sin))
        assertEquals("sin(", viewModel.state.expression)
    }
}
