package com.pocketkit.simplecalculator

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class CalculatorUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testBasicCalculation() {
        // 2 + 2 = 4
        composeTestRule.onNode(hasText("2") and hasClickAction()).performClick()
        composeTestRule.onNode(hasText("+") and hasClickAction()).performClick()
        composeTestRule.onNode(hasText("2") and hasClickAction()).performClick()
        composeTestRule.onNode(hasText("=") and hasClickAction()).performClick()

        // Check result
        composeTestRule.onNodeWithTag("Result").assertTextContains("4")
    }

    @Test
    fun testScientificModeToggle() {
        // Initially, scientific buttons should not be visible (e.g., "sin")
        composeTestRule.onNodeWithText("sin").assertDoesNotExist()

        // Click expand button (using content description)
        composeTestRule.onNodeWithContentDescription("Expand").performClick()

        // Now scientific buttons should be visible
        composeTestRule.onNode(hasText("sin") and hasClickAction()).assertExists()
        composeTestRule.onNode(hasText("cos") and hasClickAction()).assertExists()
        composeTestRule.onNode(hasText("π") and hasClickAction()).assertExists()

        // Click collapse
        composeTestRule.onNodeWithContentDescription("Collapse").performClick()
        
        // Scientific buttons should be gone
        composeTestRule.onNodeWithText("sin").assertDoesNotExist()
    }

    @Test
    fun testScientificCalculation() {
        // Expand first
        composeTestRule.onNodeWithContentDescription("Expand").performClick()

        // sqrt(4) -> 2
        composeTestRule.onNode(hasText("√") and hasClickAction()).performClick()
        composeTestRule.onNode(hasText("4") and hasClickAction()).performClick()
        composeTestRule.onNode(hasText("=") and hasClickAction()).performClick()

        // Check result (2)
        composeTestRule.onNodeWithTag("Result").assertTextContains("2")
    }

    @Test
    fun testClearButton() {
        composeTestRule.onNode(hasText("5") and hasClickAction()).performClick()
        composeTestRule.onNode(hasText("AC") and hasClickAction()).performClick()
        
        // Should show empty or 0
        composeTestRule.onNodeWithTag("Display").assertTextEquals("0")
    }

    @Test
    fun testDeleteButton() {
        composeTestRule.onNode(hasText("1") and hasClickAction()).performClick()
        composeTestRule.onNode(hasText("2") and hasClickAction()).performClick()
        
        // Click Delete
        composeTestRule.onNodeWithContentDescription("Delete").performClick()
        
        // Verify 2 is gone, 1 remains
        composeTestRule.onNodeWithTag("Display").assertTextEquals("1")
    }
    
    @Test
    fun testMenuAndClearHistory() {
        // Open Menu
        composeTestRule.onNodeWithContentDescription("Menu").performClick()
        
        // Click Clear History
        composeTestRule.onNodeWithText("Clear History").performClick()
        
        // Verify history is cleared (hard to verify visually without history items, but we can check if menu closed)
        composeTestRule.onNodeWithText("Clear History").assertDoesNotExist()
    }

    @Test
    fun testAboutScreen() {
        // Open Menu
        composeTestRule.onNodeWithContentDescription("Menu").performClick()

        // Click About App
        composeTestRule.onNodeWithText("About App").performClick()

        // Verify About Screen content
        composeTestRule.onNodeWithText("Open Calculator v1.0").assertExists()
        composeTestRule.onNodeWithContentDescription("App Icon").assertExists()
    }
}
