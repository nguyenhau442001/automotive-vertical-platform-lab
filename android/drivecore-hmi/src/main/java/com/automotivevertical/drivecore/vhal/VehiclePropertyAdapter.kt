package com.automotivevertical.drivecore.vhal

import com.automotivevertical.drivecore.data.CockpitState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * Interface representing a VHAL-like adapter that abstracts physical vehicle signal sources.
 * In production AAOS, this wraps CarPropertyManager / IVehicle AIDL interface.
 */
interface VehiclePropertyAdapter {
    val cockpitState: StateFlow<CockpitState>
    fun startListening()
    fun stopListening()
    fun setHvacTemperature(targetCelsius: Float)
}

/**
 * Mock VHAL-like adapter that generates realistic synthetic signal streams
 * for local development and demonstration.
 */
class MockVehiclePropertyAdapter(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : VehiclePropertyAdapter {

    private val _cockpitState = MutableStateFlow(CockpitState())
    override val cockpitState: StateFlow<CockpitState> = _cockpitState.asStateFlow()

    private var simulationJob: Job? = null

    override fun startListening() {
        if (simulationJob?.isActive == true) return

        simulationJob = scope.launch {
            var tick = 0L
            while (true) {
                delay(200) // 5 Hz update rate
                tick++
                val timeSec = tick * 0.2f

                // Dynamic simulation math matching schemas/vehicle_signals.json rules
                val speed = ((60.0f + 55.0f * sin(timeSec * 0.2f)).coerceAtLeast(0.0f) * 10).toInt() / 10.0f
                val gear = when {
                    speed == 0.0f -> "PARK"
                    speed < 15.0f -> if (sin(timeSec * 0.05f) < -0.8f) "REVERSE" else "DRIVE"
                    else -> "DRIVE"
                }
                val parkingBrake = (gear == "PARK")
                val door = if (gear == "PARK" && sin(timeSec * 0.1f) > 0.7f) 1 else 0
                val hvac = ((21.0f + 3.0f * cos(timeSec * 0.05f)) * 10).toInt() / 10.0f
                val turnPhase = sin(timeSec * 0.3f)
                val turnSignal = when {
                    turnPhase > 0.7f -> "LEFT"
                    turnPhase < -0.7f -> "RIGHT"
                    else -> "NONE"
                }

                _cockpitState.value = _cockpitState.value.copy(
                    speedKmH = speed,
                    gear = gear,
                    parkingBrakeEngaged = parkingBrake,
                    doorBitmask = door,
                    hvacTempCelsius = hvac,
                    turnSignalState = turnSignal,
                    ignitionState = "ON",
                    isConnected = true,
                    lastUpdateTimestampMs = System.currentTimeMillis()
                )
            }
        }
    }

    override fun stopListening() {
        simulationJob?.cancel()
        simulationJob = null
    }

    override fun setHvacTemperature(targetCelsius: Float) {
        val clamped = targetCelsius.coerceIn(16.0f, 30.0f)
        _cockpitState.value = _cockpitState.value.copy(
            hvacTempCelsius = clamped,
            lastUpdateTimestampMs = System.currentTimeMillis()
        )
    }
}
