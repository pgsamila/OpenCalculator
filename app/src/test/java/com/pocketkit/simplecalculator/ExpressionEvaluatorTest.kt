package com.pocketkit.simplecalculator

import org.junit.Assert.assertEquals
import org.junit.Test

class ExpressionEvaluatorTest {

    @Test
    fun testBasicArithmetic() {
        assertEquals(4.0, ExpressionEvaluator.evaluate("2+2", true), 0.001)
        assertEquals(0.0, ExpressionEvaluator.evaluate("2-2", true), 0.001)
        assertEquals(6.0, ExpressionEvaluator.evaluate("2x3", true), 0.001)
        assertEquals(2.0, ExpressionEvaluator.evaluate("6/3", true), 0.001)
    }

    @Test
    fun testOrderOfOperations() {
        assertEquals(14.0, ExpressionEvaluator.evaluate("2+3x4", true), 0.001)
        assertEquals(20.0, ExpressionEvaluator.evaluate("(2+3)x4", true), 0.001)
    }

    @Test
    fun testScientificFunctions() {
        // Degree Mode
        assertEquals(0.5, ExpressionEvaluator.evaluate("sin(30)", true), 0.001)
        assertEquals(0.5, ExpressionEvaluator.evaluate("cos(60)", true), 0.001)
        assertEquals(1.0, ExpressionEvaluator.evaluate("tan(45)", true), 0.001)

        // Radian Mode
        assertEquals(0.0, ExpressionEvaluator.evaluate("sin(0)", false), 0.001)
        assertEquals(1.0, ExpressionEvaluator.evaluate("cos(0)", false), 0.001)
    }

    @Test
    fun testConstants() {
        assertEquals(Math.PI, ExpressionEvaluator.evaluate("π", true), 0.001)
        assertEquals(Math.E, ExpressionEvaluator.evaluate("e", true), 0.001)
    }

    @Test
    fun testPowerAndFactorial() {
        assertEquals(8.0, ExpressionEvaluator.evaluate("2^3", true), 0.001)
        assertEquals(120.0, ExpressionEvaluator.evaluate("5!", true), 0.001)
    }

    @Test
    fun testComplexExpressions() {
        assertEquals(2.0, ExpressionEvaluator.evaluate("√4", true), 0.001)
        assertEquals(1.0, ExpressionEvaluator.evaluate("ln(e)", true), 0.001)
        assertEquals(2.0, ExpressionEvaluator.evaluate("log(100)", true), 0.001)
    }
    
    @Test
    fun testImplicitMultiplication() {
        assertEquals(6.0, ExpressionEvaluator.evaluate("2(3)", true), 0.001)
        assertEquals(6.0, ExpressionEvaluator.evaluate("(2)(3)", true), 0.001)
    }
}
