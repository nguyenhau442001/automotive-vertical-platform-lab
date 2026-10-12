package com.automotivevertical.drivecore.data

/**
 * Standard AOSP VehicleProperty mapping for DriveCore HMI.
 */
enum class VehicleProperty(
    val propIdHex: String,
    val propIdDecimal: Int,
    val vhalName: String,
    val unit: String
) {
    VEHICLE_SPEED("0x11600207", 291504647, "PERF_VEHICLE_SPEED", "km/h"),
    GEAR_SELECTION("0x11400400", 289408000, "GEAR_SELECTION", "enum"),
    PARKING_BRAKE("0x11200402", 287310850, "PARKING_BRAKE_ON", "boolean"),
    DOOR_STATE("0x16400b00", 373295872, "DOOR_POS", "bitmask"),
    HVAC_TEMPERATURE("0x12400505", 306185477, "HVAC_TEMPERATURE_SET", "°C"),
    TURN_SIGNAL("0x11400408", 289408008, "TURN_SIGNAL_STATE", "enum"),
    IGNITION_STATE("0x11400409", 289408009, "IGNITION_STATE", "enum")
}
