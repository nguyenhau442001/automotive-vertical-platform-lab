# 📐 Schemas: Vehicle Signal Specification (Source of Truth)

In an Android Automotive (AAOS) platform architecture, signal consistency across native C++ daemons, AIDL HAL interfaces, and Android HMI applications is paramount. 

This directory contains `vehicle_signals.json` — the **Single Source of Truth (SSOT)** schema for the DriveCore HMI project.

---

## 🚘 Required Signal Catalog

The schema standardizes 7 primary vehicle signals according to AOSP `VehicleProperty` specifications:

| Signal ID | VHAL Property Name | VHAL Prop ID (Hex) | Data Type | Change Mode | Access | Unit |
|-----------|-------------------|-------------------|-----------|-------------|--------|------|
| `VEHICLE_SPEED` | `PERF_VEHICLE_SPEED` | `0x11600207` | `FLOAT` | `CONTINUOUS` | READ | km/h |
| `GEAR_SELECTION` | `GEAR_SELECTION` | `0x11400400` | `ENUM` | `ON_CHANGE` | READ | enum |
| `PARKING_BRAKE` | `PARKING_BRAKE_ON` | `0x11200402` | `BOOLEAN` | `ON_CHANGE` | READ | boolean |
| `DOOR_STATE` | `DOOR_POS` | `0x16400b00` | `INT32` | `ON_CHANGE` | READ | bitmask |
| `HVAC_TEMPERATURE` | `HVAC_TEMPERATURE_SET` | `0x12400505` | `FLOAT` | `ON_CHANGE` | READ_WRITE | °C |
| `TURN_SIGNAL` | `TURN_SIGNAL_STATE` | `0x11400408` | `ENUM` | `ON_CHANGE` | READ | enum |
| `IGNITION_STATE` | `IGNITION_STATE` | `0x11400409` | `ENUM` | `ON_CHANGE` | READ | enum |

---

## 🔄 Cross-Layer Mapping Architecture

Each entry in `vehicle_signals.json` bridges three architectural boundaries:

1. **CAN Frame Bit Definition** (`can_mapping`): Specifies CAN ID, start bit, bit length, endianness, factor, and offset for native bit unpacking (`libcansignal`).
2. **AOSP VHAL AIDL Definition** (`vhal_property_id`): Aligns with standard Android Automotive VHAL property integer constants used in `IVehicle` AIDL interfaces.
3. **Android HMI Model** (`data_type`, `unit`): Dictates Kotlin type bindings and UI formatting in `DriveCore HMI`.

---

## 🛠️ Validation

Schema integrity is validated automatically via automated contract tests:
```bash
python3 -m unittest tests/contract/test_signal_schema.py
```
