# 🔍 Diagnostics & Cross-Layer Debugging

In production Android Automotive OS (AAOS) development, resolving issues requires tracing signal data across multiple process boundaries:
1. CAN Bus Wire (`vcan0` / SocketCAN)
2. C++ Native Bridge Daemon (`canbridge`)
3. AOSP Vehicle HAL Service (`android.hardware.automotive.vehicle`)
4. Android Framework (`CarPropertyManager` / `CarService`)
5. HMI Application (`DriveCore HMI`)

---

## 🧰 Diagnostic Toolkit

### 1. Native Logcat Tracing
Filter logcat output for native HAL and VHAL binder transactions:
```bash
adb shell logcat -s CanBridge SmartCabin VHAL CarPropertyManager
```

### 2. Signal Inspection Utility
Use `diagnostics/signal_tracer.py` to inspect event streams and measure update frequency:
```bash
python3 diagnostics/signal_tracer.py
```

### 3. VHAL Property Dumpsys (AOSP)
Query live VHAL property states via `dumpsys`:
```bash
# Dump all car property states registered in CarService
adb shell dumpsys car_service --services CarPropertyService

# Direct VHAL property check (for speed property 291504647 / 0x11600207)
adb shell dumpsys activity service com.android.car get-property 291504647 0
```

---

## ⚡ Key Latency & Performance Targets

- **Vehicle Speed (`CONTINUOUS`)**: Target delivery rate = 10 Hz (100 ms period). Max end-to-end latency <= 15 ms.
- **Critical Safety Events (`ON_CHANGE`)**: Turn signals & parking brake transitions. Delivery latency <= 5 ms from native trigger to Binder dispatch.
