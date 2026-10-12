# 🔄 Vehicle Signal Pipeline & Conversion Mechanics

Understanding how raw binary bits on a CAN bus wire transform into typed Kotlin `StateFlow` values in the Android HMI is fundamental to Automotive Platform Engineering.

---

## 🔢 Step-by-Step Signal Transformation

### 1. Raw CAN Bit Decoding
A vehicle sensor (e.g., speed sensor) transmits a CAN frame with ID `0x100`:

```
CAN Frame ID: 0x100 | DLC: 8
Payload Bytes: [ 0x3E, 0x1F, 0x01, 0x00, ... ]
```

The C++ codec (`libcansignal`) extracts the speed bitfield using the formula defined in `schemas/vehicle_signals.json`:
$$\text{Physical Value} = (\text{Raw Bit Value} \times \text{Factor}) + \text{Offset}$$

- **Start Bit**: 0, **Bit Length**: 16
- **Raw Bit Value**: `0x1F3E` = 7998
- **Factor**: `0.01`, **Offset**: `0.0`
- **Decoded Physical Speed**: $(7998 \times 0.01) + 0.0 = 79.98\text{ km/h}$

---

## ⚡ Property Change Modes: CONTINUOUS vs ON_CHANGE

Android Automotive VHAL categorizes properties by update semantics:

### 1. CONTINUOUS (`PERF_VEHICLE_SPEED`)
- Emits values at a fixed frequency (e.g., 10 Hz / 100 ms interval).
- Require sub-sampling in native C++ layer to prevent overwhelming Binder threadpools during rapid speed changes.

### 2. ON_CHANGE (`GEAR_SELECTION`, `PARKING_BRAKE_ON`, `DOOR_POS`)
- Emits values **only** when a state transition occurs (e.g., Park -> Drive).
- Uses latching logic in native daemon to suppress redundant duplicate broadcasts.

---

## 🛡️ Thread Safety & Latency Mitigation

1. **Lock-Free Signal Queues**: In high-throughput CAN environments (500 kbps / 1 Mbps), `CanBridge` uses double-buffering or ring buffers to separate CAN socket receive threads from Binder dispatch threads.
2. **Binder Thread Pool Sizing**: VHAL AIDL services configure Binder thread pools (`ABinderProcess_setThreadPoolMaxThreadCount(4)`) to prevent Binder thread starvation during burst traffic.
