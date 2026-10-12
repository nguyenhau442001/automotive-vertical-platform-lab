package com.automotivevertical.drivecore.data

/**
 * Represents a strongly-typed vehicle signal value update received from the VHAL-like adapter.
 */
sealed class VehicleSignal<T>(
    open val property: VehicleProperty,
    open val value: T,
    open val timestampMs: Long
) {
    data class Speed(
        override val value: Float,
        override val timestampMs: Long = System.currentTimeMillis()
    ) : VehicleSignal<Float>(VehicleProperty.VEHICLE_SPEED, value, timestampMs)

    data class Gear(
        override val value: String,
        override val timestampMs: Long = System.currentTimeMillis()
    ) : VehicleSignal<String>(VehicleProperty.GEAR_SELECTION, value, timestampMs)

    data class ParkingBrake(
        override val value: Boolean,
        override val timestampMs: Long = System.currentTimeMillis()
    ) : VehicleSignal<Boolean>(VehicleProperty.PARKING_BRAKE, value, timestampMs)

    data class Door(
        override val value: Int,
        override val timestampMs: Long = System.currentTimeMillis()
    ) : VehicleSignal<Int>(VehicleProperty.DOOR_STATE, value, timestampMs)

    data class HvacTemperature(
        override val value: Float,
        override val timestampMs: Long = System.currentTimeMillis()
    ) : VehicleSignal<Float>(VehicleProperty.HVAC_TEMPERATURE, value, timestampMs)

    data class TurnSignal(
        override val value: String,
        override val timestampMs: Long = System.currentTimeMillis()
    ) : VehicleSignal<String>(VehicleProperty.TURN_SIGNAL, value, timestampMs)

    data class Ignition(
        override val value: String,
        override val timestampMs: Long = System.currentTimeMillis()
    ) : VehicleSignal<String>(VehicleProperty.IGNITION_STATE, value, timestampMs)
}

/**
 * Encapsulates full cockpit state for HMI rendering.
 */
data class CockpitState(
    val speedKmH: Float = 0.0f,
    val gear: String = "PARK",
    val parkingBrakeEngaged: Boolean = true,
    val doorBitmask: Int = 0,
    val hvacTempCelsius: Float = 22.0f,
    val turnSignalState: String = "NONE",
    val ignitionState: String = "OFF",
    val isConnected: Boolean = true,
    val lastUpdateTimestampMs: Long = System.currentTimeMillis()
)
