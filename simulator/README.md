# 🕹️ Simulator: Vehicle Signal Generation Engine

The `simulator/` directory contains tools and scripts for generating synthetic vehicle signals and driving simulated CAN traffic into the native C++ layer (`canbridge`) and mock VHAL adapter.

---

## 🚀 Features

- **Schema-Driven**: Reads signal definitions directly from `schemas/vehicle_signals.json`.
- **Realistic Dynamic Simulation**: Generates synchronous, multi-signal physics curves (e.g., speed acceleration curves, gear shifting, climate control fluctuations, turn indicators).
- **Dual Transport Support**: Can broadcast raw SocketCAN frames (`vcan0`) or JSON telemetry over TCP sockets to local native daemons or host test harnesses.

---

## 💻 Usage

Run the vehicle signal generator stream:

```bash
# Run simulator for 10 seconds
python3 simulator/signal_generator.py 10.0
```

Sample output:
```json
{
  "timestamp_ms": 2000,
  "signals": {
    "VEHICLE_SPEED": { "value": 81.4, "unit": "km/h", "vhal_id": "0x11600207" },
    "GEAR_SELECTION": { "value": "DRIVE", "unit": "enum", "vhal_id": "0x11400400" },
    "PARKING_BRAKE": { "value": false, "unit": "boolean", "vhal_id": "0x11200402" },
    "DOOR_STATE": { "value": 0, "unit": "bitmask", "vhal_id": "0x16400b00" },
    "HVAC_TEMPERATURE": { "value": 22.2, "unit": "celsius", "vhal_id": "0x12400505" },
    "TURN_SIGNAL": { "value": "LEFT", "unit": "enum", "vhal_id": "0x11400408" },
    "IGNITION_STATE": { "value": "ON", "unit": "enum", "vhal_id": "0x11400409" }
  }
}
```

For advanced CAN frame level simulation (bit-by-bit differential wave encoding), see `tools/cansim`.
