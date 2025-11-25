package com.pocketkit.simplecalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pocketkit.simplecalculator.ui.theme.*

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    modifier: Modifier = Modifier,
    buttonSpacing: Dp = 8.dp,
    onAction: (CalculatorAction) -> Unit,
    onHistoryClick: (HistoryItem) -> Unit = {},
    onThemeChange: (AppTheme) -> Unit = {},
    onAboutClick: () -> Unit = {},
    currentTheme: AppTheme = AppTheme.System
) {
    val listState = rememberLazyListState()
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (currentTheme) {
        AppTheme.Light -> false
        AppTheme.Dark -> true
        AppTheme.System -> isSystemDark
    }
    var showMenu by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    // Auto-scroll to bottom of history when it changes
    LaunchedEffect(state.history.size) {
        if (state.history.isNotEmpty()) {
            listState.animateScrollToItem(state.history.size - 1)
        }
    }

    val gradientColors = if (isDark) {
        listOf(DarkBackgroundTop, DarkBackgroundMiddle, DarkBackgroundBottom)
    } else {
        listOf(LightBackgroundTop, LightBackgroundMiddle, LightBackgroundBottom)
    }

    val textColor = MaterialTheme.colorScheme.onBackground
    val historyColor = textColor.copy(alpha = 0.6f)
    val resultColor = textColor

    val buttonShape = if (state.isExpanded) RoundedCornerShape(16.dp) else androidx.compose.foundation.shape.CircleShape
    val buttonAspectRatio = if (state.isExpanded) 2f else 1f

    val uriHandler = LocalUriHandler.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = gradientColors))
            .padding(16.dp)
    ) {
        // Menu Icon
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp)
        ) {
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = textColor
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Choose Theme") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.colors),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = {
                        showMenu = false
                        showThemeDialog = true
                    }
                )
                DropdownMenuItem(
                    text = { Text("Clear History") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.delete_sweep),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = {
                        showMenu = false
                        onAction(CalculatorAction.ClearHistory)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Privacy and Policy") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.info), // Using info icon as placeholder
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = {
                        showMenu = false
                        uriHandler.openUri("https://github.com/pgsamila/OpenCalculator/wiki/Privacy-Policy")
                    }
                )
                DropdownMenuItem(
                    text = { Text("About App") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.info),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = {
                        showMenu = false
                        onAboutClick()
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // History List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 48.dp), // Add top padding for menu
                verticalArrangement = Arrangement.Bottom,
                reverseLayout = false
            ) {
                items(state.history) { historyItem ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onHistoryClick(historyItem) },
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = historyItem.expression,
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            color = historyColor
                        )
                        Text(
                            text = "= ${historyItem.result}",
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp,
                            color = resultColor.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Current Expression / Result
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                if (state.isExpanded) {
                    Text(
                        text = if (state.isDegree) "DEG" else "RAD",
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor.copy(alpha = 0.5f),
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                }
                
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    if (state.result.isNotEmpty()) {
                        Text(
                            text = state.result,
                            style = MaterialTheme.typography.headlineMedium,
                            color = textColor.copy(alpha = 0.7f),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth().testTag("Result")
                        )
                    }
                    Text(
                        text = state.expression.ifEmpty { "0" },
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth().testTag("Display"),
                        fontWeight = FontWeight.Light,
                        fontSize = 60.sp,
                        color = resultColor,
                        maxLines = 2,
                        lineHeight = 70.sp
                    )
                }
            }

            // Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                // Expand/Collapse Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { onAction(CalculatorAction.ToggleExpanded) }) {
                        Icon(
                            imageVector = if (state.isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (state.isExpanded) "Collapse" else "Expand",
                            tint = textColor.copy(alpha = 0.7f),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Scientific Buttons (Visible if expanded)
                if (state.isExpanded) {
                    // Row S1: Deg/Rad, sin, cos, tan
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                    ) {
                        CalculatorButton(
                            symbol = if (state.isDegree) "Rad" else "Deg",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                            onClick = { onAction(CalculatorAction.ToggleDegree) }
                        )
                        CalculatorButton(
                            symbol = "sin",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                            onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Sin)) }
                        )
                        CalculatorButton(
                            symbol = "cos",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                            onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Cos)) }
                        )
                        CalculatorButton(
                            symbol = "tan",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                            onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Tan)) }
                        )
                    }
                    // Row S2: Inv, ln, log, e
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                    ) {
                        CalculatorButton(
                            symbol = "Inv",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                            onClick = { onAction(CalculatorAction.Inverse) }
                        )
                        CalculatorButton(
                            symbol = "ln",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                            onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Ln)) }
                        )
                        CalculatorButton(
                            symbol = "log",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                            onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Log)) }
                        )
                        CalculatorButton(
                            symbol = "e",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                            onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.E)) }
                        )
                    }
                    // Row S3: π, ^, !, √
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                    ) {
                        CalculatorButton(
                            symbol = "π",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                            onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Pi)) }
                        )
                        CalculatorButton(
                            symbol = "^",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp),
                            onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Power)) }
                        )
                        CalculatorButton(
                            symbol = "!",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp),
                            onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Factorial)) }
                        )
                        CalculatorButton(
                            symbol = "√",
                            modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                            color = MaterialTheme.colorScheme.secondary,
                            isDark = isDark,
                            shape = buttonShape,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp),
                            onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.SquareRoot)) }
                        )
                    }
                }
                // Row 1: AC, (), %, /
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    CalculatorButton(
                        symbol = "AC",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = MaterialTheme.colorScheme.primary,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Clear) }
                    )
                    CalculatorButton(
                        symbol = "( )",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = MaterialTheme.colorScheme.primary,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Parentheses) }
                    )
                    CalculatorButton(
                        symbol = "%",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = MaterialTheme.colorScheme.primary,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Percentage)) }
                    )
                    CalculatorButton(
                        symbol = "/",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = MaterialTheme.colorScheme.primary,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Divide)) }
                    )
                }
                // Row 2: 7, 8, 9, x
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    CalculatorButton(
                        symbol = "7",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Number(7)) }
                    )
                    CalculatorButton(
                        symbol = "8",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Number(8)) }
                    )
                    CalculatorButton(
                        symbol = "9",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Number(9)) }
                    )
                    CalculatorButton(
                        symbol = "x",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = MaterialTheme.colorScheme.primary,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Multiply)) }
                    )
                }
                // Row 3: 4, 5, 6, -
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    CalculatorButton(
                        symbol = "4",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Number(4)) }
                    )
                    CalculatorButton(
                        symbol = "5",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Number(5)) }
                    )
                    CalculatorButton(
                        symbol = "6",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Number(6)) }
                    )
                    CalculatorButton(
                        symbol = "-",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = MaterialTheme.colorScheme.primary,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Subtract)) }
                    )
                }
                // Row 4: 1, 2, 3, +
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    CalculatorButton(
                        symbol = "1",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Number(1)) }
                    )
                    CalculatorButton(
                        symbol = "2",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Number(2)) }
                    )
                    CalculatorButton(
                        symbol = "3",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Number(3)) }
                    )
                    CalculatorButton(
                        symbol = "+",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = MaterialTheme.colorScheme.primary,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Add)) }
                    )
                }
                // Row 5: 0, ., Del, =
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    CalculatorButton(
                        symbol = "0",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Number(0)) }
                    )
                    CalculatorButton(
                        symbol = ".",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Decimal) }
                    )
                    CalculatorButton(
                        icon = com.pocketkit.simplecalculator.R.drawable.delete,
                        contentDescription = "Delete",
                        modifier = Modifier.aspectRatio(buttonAspectRatio).weight(1f),
                        color = textColor,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Delete) }
                    )
                    CalculatorButton(
                        symbol = "=",
                        modifier = Modifier
                            .aspectRatio(buttonAspectRatio)
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), buttonShape),
                        color = MaterialTheme.colorScheme.primary,
                        isDark = isDark,
                        shape = buttonShape,
                        onClick = { onAction(CalculatorAction.Calculate) }
                    )
                }
            }
        }
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentTheme,
            onThemeSelected = {
                onThemeChange(it)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}

@Composable
fun ThemeSelectionDialog(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTheme by remember { mutableStateOf(currentTheme) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose Theme",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ThemeOption(
                    theme = AppTheme.Light,
                    icon = R.drawable.light_mode,
                    text = "Light Mode",
                    selected = selectedTheme == AppTheme.Light,
                    onSelect = { selectedTheme = AppTheme.Light }
                )
                ThemeOption(
                    theme = AppTheme.Dark,
                    icon = R.drawable.dark_mode,
                    text = "Dark Mode",
                    selected = selectedTheme == AppTheme.Dark,
                    onSelect = { selectedTheme = AppTheme.Dark }
                )
                ThemeOption(
                    theme = AppTheme.System,
                    icon = R.drawable.system_mode,
                    text = "System Default",
                    selected = selectedTheme == AppTheme.System,
                    onSelect = { selectedTheme = AppTheme.System }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = { onThemeSelected(selectedTheme) }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeOption(
    theme: AppTheme,
    icon: Int,
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}
