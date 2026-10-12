// AUTO-GENERATED from vehicle/dbc/blueprint.dbc by `python3 -m cansim codegen`. DO NOT EDIT.
// Edit the DBC, then run: make codegen

#pragma once

#include "canbridge/SignalBinding.h"

namespace blueprint::can::generated {

// VEHICLE_DYNAMICS: id 0x100, 8 bytes, sender EngineECU
inline constexpr SignalSpec kVEHICLE_DYNAMICS_VehicleSpeed{"VehicleSpeed", 0, 16, ByteOrder::kIntel, false, 0.01, 0.0, 0.0, 250.0};
inline constexpr SignalSpec kVEHICLE_DYNAMICS_GearSelection{"GearSelection", 16, 4, ByteOrder::kIntel, false, 1.0, 0.0, 0.0, 15.0};
inline constexpr SignalSpec kVEHICLE_DYNAMICS_ParkingBrake{"ParkingBrake", 20, 1, ByteOrder::kIntel, false, 1.0, 0.0, 0.0, 1.0};
inline constexpr SignalSpec kVEHICLE_DYNAMICS_IgnitionState{"IgnitionState", 24, 4, ByteOrder::kIntel, false, 1.0, 0.0, 0.0, 15.0};

// BODY_STATUS: id 0x200, 8 bytes, sender BodyECU
inline constexpr SignalSpec kBODY_STATUS_DoorPos{"DoorPos", 0, 8, ByteOrder::kIntel, false, 1.0, 0.0, 0.0, 255.0};
inline constexpr SignalSpec kBODY_STATUS_TurnSignal{"TurnSignal", 8, 4, ByteOrder::kIntel, false, 1.0, 0.0, 0.0, 15.0};

// HVAC_STATUS: id 0x300, 8 bytes, sender ClimateECU
inline constexpr SignalSpec kHVAC_STATUS_HvacTempSet{"HvacTempSet", 0, 8, ByteOrder::kIntel, false, 0.5, 10.0, 16.0, 30.0};

// THERMAL_STATUS: id 0x3E8, 8 bytes, sender ClimateECU
inline constexpr SignalSpec kTHERMAL_STATUS_OutsideTemp{"OutsideTemp", 0, 16, ByteOrder::kIntel, false, 0.1, -40.0, -40.0, 125.0};
inline constexpr SignalSpec kTHERMAL_STATUS_CabinTempRow1Left{"CabinTempRow1Left", 16, 8, ByteOrder::kIntel, false, 0.5, -10.0, -10.0, 117.5};
inline constexpr SignalSpec kTHERMAL_STATUS_CabinTempRow1Right{"CabinTempRow1Right", 24, 8, ByteOrder::kIntel, false, 0.5, -10.0, -10.0, 117.5};
inline constexpr SignalSpec kTHERMAL_STATUS_EvaporatorTemp{"EvaporatorTemp", 39, 16, ByteOrder::kMotorola, true, 0.01, 0.0, -50.0, 150.0};
inline constexpr SignalSpec kTHERMAL_STATUS_ThermalCounter{"ThermalCounter", 56, 4, ByteOrder::kIntel, false, 1.0, 0.0, 0.0, 15.0};

// BATTERY_THERMAL: id 0x3E9, 8 bytes, sender BMS
inline constexpr SignalSpec kBATTERY_THERMAL_CellTempMax{"CellTempMax", 0, 8, ByteOrder::kIntel, false, 1.0, -40.0, -40.0, 125.0};
inline constexpr SignalSpec kBATTERY_THERMAL_CellTempMin{"CellTempMin", 8, 8, ByteOrder::kIntel, false, 1.0, -40.0, -40.0, 125.0};
inline constexpr SignalSpec kBATTERY_THERMAL_CoolantInletTemp{"CoolantInletTemp", 16, 8, ByteOrder::kIntel, false, 1.0, -40.0, -40.0, 125.0};
inline constexpr SignalSpec kBATTERY_THERMAL_BatteryThermalCounter{"BatteryThermalCounter", 60, 4, ByteOrder::kIntel, false, 1.0, 0.0, 0.0, 15.0};

inline constexpr SignalBinding kBindings[] = {
        // 0x11600207 = SYSTEM | GLOBAL | FLOAT | 0x0207
        {0x100, 8, &kVEHICLE_DYNAMICS_VehicleSpeed, 0x11600207, 0, "PERF_VEHICLE_SPEED", ChangeMode::kContinuous, 1.0f, 10.0f},
        // 0x11400400 = SYSTEM | GLOBAL | INT32 | 0x0400
        {0x100, 8, &kVEHICLE_DYNAMICS_GearSelection, 0x11400400, 0, "GEAR_SELECTION", ChangeMode::kOnChange, 0.0f, 0.0f},
        // 0x11200402 = SYSTEM | GLOBAL | BOOLEAN | 0x0402
        {0x100, 8, &kVEHICLE_DYNAMICS_ParkingBrake, 0x11200402, 0, "PARKING_BRAKE_ON", ChangeMode::kOnChange, 0.0f, 0.0f},
        // 0x11400409 = SYSTEM | GLOBAL | INT32 | 0x0409
        {0x100, 8, &kVEHICLE_DYNAMICS_IgnitionState, 0x11400409, 0, "IGNITION_STATE", ChangeMode::kOnChange, 0.0f, 0.0f},
        // 0x16400b00 = SYSTEM | DOOR | INT32 | 0x0b00
        {0x200, 8, &kBODY_STATUS_DoorPos, 0x16400B00, 0, "DOOR_POS", ChangeMode::kOnChange, 0.0f, 0.0f},
        // 0x11400408 = SYSTEM | GLOBAL | INT32 | 0x0408
        {0x200, 8, &kBODY_STATUS_TurnSignal, 0x11400408, 0, "TURN_SIGNAL_STATE", ChangeMode::kOnChange, 0.0f, 0.0f},
        // 0x12400505 = SYSTEM | 0x2000000 | INT32 | 0x0505
        {0x300, 8, &kHVAC_STATUS_HvacTempSet, 0x12400505, 0, "HVAC_TEMPERATURE_SET", ChangeMode::kOnChange, 0.0f, 0.0f},
        // 0x11600703 = SYSTEM | GLOBAL | FLOAT | 0x0703
        {0x3E8, 8, &kTHERMAL_STATUS_OutsideTemp, 0x11600703, 0, "ENV_OUTSIDE_TEMPERATURE", ChangeMode::kContinuous, 1.0f, 2.0f},
        // 0x15600502 = SYSTEM | SEAT | FLOAT | 0x0502
        {0x3E8, 8, &kTHERMAL_STATUS_CabinTempRow1Left, 0x15600502, 49, "HVAC_TEMPERATURE_CURRENT", ChangeMode::kOnChange, 0.0f, 0.0f},
        // 0x15600502 = SYSTEM | SEAT | FLOAT | 0x0502
        {0x3E8, 8, &kTHERMAL_STATUS_CabinTempRow1Right, 0x15600502, 68, "HVAC_TEMPERATURE_CURRENT", ChangeMode::kOnChange, 0.0f, 0.0f},
        // 0x21600101 = VENDOR | GLOBAL | FLOAT | 0x0101
        {0x3E9, 8, &kBATTERY_THERMAL_CellTempMax, 0x21600101, 0, "VENDOR_BATTERY_CELL_TEMP_MAX", ChangeMode::kOnChange, 0.0f, 0.0f},
};

}  // namespace blueprint::can::generated
