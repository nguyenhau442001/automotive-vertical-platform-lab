package com.automotivevertical.drivecore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.automotivevertical.drivecore.data.CockpitState
import com.automotivevertical.drivecore.ui.theme.CockpitAccent
import com.automotivevertical.drivecore.ui.theme.CockpitBackground
import com.automotivevertical.drivecore.ui.theme.CockpitCardBackground
import com.automotivevertical.drivecore.ui.theme.CockpitCardBorder
import com.automotivevertical.drivecore.ui.theme.CockpitDanger
import com.automotivevertical.drivecore.ui.theme.CockpitGearActive
import com.automotivevertical.drivecore.ui.theme.CockpitGearInactive
import com.automotivevertical.drivecore.ui.theme.CockpitSuccess
import com.automotivevertical.drivecore.ui.theme.CockpitTextPrimary
import com.automotivevertical.drivecore.ui.theme.CockpitTextSecondary
import com.automotivevertical.drivecore.ui.theme.CockpitWarning
import com.automotivevertical.drivecore.ui.theme.DriveCoreTheme

@Composable
fun DashboardScreen(state: CockpitState, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = CockpitBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "DriveCore HMI",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = CockpitAccent
            )
            Text(
                text = "Vehicle Signal to Android HMI Demo",
                fontSize = 13.sp,
                color = CockpitTextSecondary
            )
            Text(
                text = "🟢 Mock VHAL Adapter Active | Refreshed: ${state.lastUpdateTimestampMs} ms",
                fontSize = 12.sp,
                color = CockpitSuccess
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 1. Speedometer Card
            CockpitCard(title = "SPEEDOMETER") {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "%.1f".format(state.speedKmH),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = CockpitAccent
                    )
                    Text(
                        text = "km/h",
                        fontSize = 16.sp,
                        color = CockpitTextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // 2. Gear Selector Card
            CockpitCard(title = "GEAR POSITION") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("P", "R", "N", "D").forEach { gear ->
                        val isActive = (state.gear == "PARK" && gear == "P") ||
                                (state.gear == "REVERSE" && gear == "R") ||
                                (state.gear == "NEUTRAL" && gear == "N") ||
                                (state.gear == "DRIVE" && gear == "D")
                        Text(
                            text = gear,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isActive) CockpitGearActive else CockpitGearInactive
                        )
                    }
                }
            }

            // 3. Parking Brake Card
            CockpitCard(title = "PARKING BRAKE") {
                val text = if (state.parkingBrakeEngaged) "🔴 ENGAGED" else "🟢 RELEASED"
                val color = if (state.parkingBrakeEngaged) CockpitDanger else CockpitSuccess
                Text(
                    text = text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }

            // 4. Door State Card
            CockpitCard(title = "DOOR POSITION") {
                val text = if (state.doorBitmask == 0) "🟢 ALL CLOSED" else "⚠️ DRIVER DOOR OPEN"
                val color = if (state.doorBitmask == 0) CockpitSuccess else CockpitWarning
                Text(
                    text = text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }

            // 5. HVAC Climate Control Card
            CockpitCard(title = "CLIMATE CONTROL") {
                Text(
                    text = "%.1f °C".format(state.hvacTempCelsius),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = CockpitTextPrimary
                )
            }

            // 6. Turn Indicator Card
            CockpitCard(title = "TURN INDICATOR") {
                val turnText = when (state.turnSignalState) {
                    "LEFT" -> "◄ LEFT"
                    "RIGHT" -> "RIGHT ►"
                    "HAZARD" -> "◄ HAZARD ►"
                    else -> "OFF"
                }
                Text(
                    text = turnText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.turnSignalState != "NONE") CockpitWarning else CockpitTextSecondary
                )
            }

            // 7. Power State Card
            CockpitCard(title = "POWER STATE") {
                Text(
                    text = state.ignitionState,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CockpitTextPrimary
                )
            }
        }
    }
}

@Composable
private fun CockpitCard(
    title: String,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CockpitCardBackground)
            .border(1.dp, CockpitCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CockpitTextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DriveCoreTheme {
        DashboardScreen(
            state = CockpitState(
                speedKmH = 75.5f,
                gear = "DRIVE",
                parkingBrakeEngaged = false,
                doorBitmask = 0,
                hvacTempCelsius = 22.5f,
                turnSignalState = "LEFT",
                ignitionState = "ON",
                isConnected = true,
                lastUpdateTimestampMs = 123456789L
            )
        )
    }
}
