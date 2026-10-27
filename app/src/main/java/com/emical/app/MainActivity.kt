package com.emical.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

private val Ink = Color(0xFF101827)
private val Panel = Color(0xFF192438)
private val Muted = Color(0xFF9BA9BD)
private val Mint = Color(0xFF72F0C0)
private val Canvas = Color(0xFF0B1220)
private val White = Color(0xFFF6F8FC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EmiCalApp() }
    }
}

@Composable
private fun EmiCalApp() {
    var loanText by remember { mutableStateOf("1200000") }
    var rateText by remember { mutableStateOf("8.5") }
    var yearsText by remember { mutableStateOf("5") }
    var selectedTab by remember { mutableIntStateOf(0) }

    val principal = loanText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val rate = rateText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val years = yearsText.toIntOrNull()?.coerceIn(1, 40) ?: 1
    val months = years * 12
    val monthlyRate = rate / 1200.0
    val emi = if (monthlyRate == 0.0) principal / months else {
        val factor = (1.0 + monthlyRate).pow(months)
        principal * monthlyRate * factor / (factor - 1.0)
    }
    val total = emi * months
    val interest = (total - principal).coerceAtLeast(0.0)
    val money = remember { NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 } }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Mint, onPrimary = Ink, background = Canvas, onBackground = White,
            surface = Panel, onSurface = White, secondary = Mint
        )
    ) {
        Scaffold(
            containerColor = Canvas,
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF111B2B), contentColor = Muted) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Calculate, contentDescription = null) },
                        label = { Text("Calculator") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Mint, selectedTextColor = Mint, indicatorColor = Panel)
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.History, contentDescription = null) },
                        label = { Text("History") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Mint, selectedTextColor = Mint, indicatorColor = Panel)
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.Savings, contentDescription = null) },
                        label = { Text("Plan") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Mint, selectedTextColor = Mint, indicatorColor = Panel)
                    )
                }
            }
        ) { padding ->
            if (selectedTab == 0) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier.size(48.dp).background(Mint, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoGraph, contentDescription = "EmiCal", tint = Ink, modifier = Modifier.size(27.dp))
                        }
                        Column(Modifier.weight(1f)) {
                            Text("EmiCal", color = White, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                            Text("SMART MONEY. CLEAR PLANS.", color = Muted, fontSize = 10.sp, letterSpacing = 1.5.sp)
                        }
                        Surface(color = Panel, shape = CircleShape) {
                            Text("PRO", modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp), color = Mint, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Your loan, simplified.", color = White, fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 34.sp)
                        Text("Plan your next big move with confidence.", color = Muted, fontSize = 14.sp)
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Mint),
                        shape = RoundedCornerShape(26.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Ink, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("YOUR MONTHLY EMI", color = Ink.copy(alpha = .72f), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            }
                            Text(money.format(emi), color = Ink, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
                            HorizontalDivider(color = Ink.copy(alpha = .16f))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("Total interest", color = Ink.copy(alpha = .68f), fontSize = 12.sp)
                                    Text(money.format(interest), color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Total payment", color = Ink.copy(alpha = .68f), fontSize = 12.sp)
                                    Text(money.format(total), color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(24.dp)) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Loan details", modifier = Modifier.weight(1f), color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("EDIT VALUES", color = Mint, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            }
                            NumberInput("Loan amount", "₹", loanText, { loanText = it }, "e.g. 1200000")
                            NumberInput("Interest rate (per year)", "%", rateText, { rateText = it }, "e.g. 8.5", decimal = true)
                            NumberInput("Loan tenure", "Years", yearsText, { yearsText = it }, "e.g. 5")
                        }
                    }

                    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(24.dp)) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoGraph, contentDescription = null, tint = Mint)
                                Spacer(Modifier.width(8.dp))
                                Text("Payment breakdown", color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                LegendDot(Mint, "Principal")
                                LegendDot(Color(0xFF7083A3), "Interest")
                            }
                            Row(Modifier.fillMaxWidth().height(12.dp).background(Color(0xFF7083A3), CircleShape)) {
                                Box(Modifier.weight(if (total > 0) (principal / total).toFloat().coerceAtLeast(0.001f) else 1f).fillMaxHeight().background(Mint, CircleShape))
                            }
                            BreakdownRow("Loan principal", money.format(principal), Mint)
                            BreakdownRow("Total interest", money.format(interest), Color(0xFF7083A3))
                            HorizontalDivider(color = White.copy(alpha = .09f))
                            BreakdownRow("Total payable", money.format(total), White)
                        }
                    }
                    Text("Estimates only • Actual lender terms may vary.", color = Muted, fontSize = 11.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(Modifier.height(10.dp))
                }
            } else {
                Column(
                    Modifier.fillMaxSize().padding(padding).padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(if (selectedTab == 1) "Your history" else "Financial planner", color = White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text(
                        if (selectedTab == 1) "Your current calculation is ready to review." else "A clearer view of your borrowing power starts with a few simple numbers.",
                        color = Muted, fontSize = 14.sp
                    )
                    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(24.dp)) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(if (selectedTab == 1) "LATEST ESTIMATE" else "MONTHLY COMMITMENT", color = Mint, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text(money.format(emi), color = White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                            Text("${money.format(principal)} loan • $years years • $rate% p.a.", color = Muted, fontSize = 13.sp)
                            OutlinedButton(onClick = { selectedTab = 0 }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(contentColor = Mint)) {
                                Text("Edit calculation")
                            }
                        }
                    }
                    Text("Tip: Compare offers using the same loan amount and tenure.", color = Muted, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun NumberInput(label: String, suffix: String, value: String, onValueChange: (String) -> Unit, placeholder: String, decimal: Boolean = false) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, color = Muted, fontSize = 13.sp)
        OutlinedTextField(
            value = value,
            onValueChange = { input -> if (input.length <= 15 && (input.isEmpty() || input.matches(if (decimal) Regex("^\\d*\\.?\\d*$") else Regex("^\\d*$")))) onValueChange(input) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(placeholder, color = Muted.copy(alpha = .6f)) },
            trailingIcon = { Text(suffix, modifier = Modifier.padding(end = 12.dp), color = Mint, fontWeight = FontWeight.Bold) },
            keyboardOptions = KeyboardOptions(keyboardType = if (decimal) KeyboardType.Decimal else KeyboardType.Number),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White, unfocusedTextColor = White,
                focusedBorderColor = Mint, unfocusedBorderColor = Color(0xFF34435B),
                cursorColor = Mint, focusedContainerColor = Ink.copy(alpha = .35f),
                unfocusedContainerColor = Ink.copy(alpha = .35f)
            )
        )
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        Box(Modifier.size(9.dp).background(color, CircleShape))
        Text(label, color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun BreakdownRow(label: String, amount: String, color: Color) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Muted, fontSize = 13.sp)
        Text(amount, color = color, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
