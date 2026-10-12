# ⚙️ AOSP Integration & AIDL VHAL Service

This directory contains configuration templates, build blueprints, init `.rc` scripts, SELinux policies, and architectural integration notes for embedding the **DriveCore HMI VHAL Service** into an Android Open Source Project (AOSP) / Android Automotive OS (AAOS) platform build.

---

## 🏛️ VHAL Architecture & Layer Integration

```
   DefaultVehicleHal (AIDL IVehicle, Binder)
          │
   CanVehicleHardware  ── CAN-Owned Properties ──► CanBridge -> ICanTransport
          │
   FakeVehicleHardware (System Fallback Properties)
```

1. **`CanVehicleHardware` Decorator**: Wraps AOSP `FakeVehicleHardware`. Properties bound to CAN signals in `vehicle_signals.json` are served directly by `CanBridge`. Unbound system properties fall back to `FakeVehicleHardware`.
2. **RX Path (CAN -> HMI)**: Native `CanBridge` receives raw CAN frames via `SocketCAN` or `TCP`, decodes bitfields, and invokes `onCanUpdates()`.
3. **TX Path (HMI -> CAN)**: `CanVehicleHardware::setValues()` encodes target property values (e.g. `HVAC_TEMPERATURE_SET`) into physical CAN frames via `CanBridge::writeProperty()` and transmits them back onto the CAN bus.

---

## 📂 Directory Contents

- `sepolicy/smartcabin.te`: SELinux domain definitions for vendor vehicle services.
- `vhal/aosp/`: C++ AIDL VHAL Service implementation (`CanVehicleHardware`, `VehicleService`).
- Native build configurations (`Android.bp`) in `native/` and `vhal/aosp/`.

---

## 🔍 Dumpsys & Interactive Diagnostics

Query live CAN bridge status and inject synthetic frames directly via `dumpsys`:

```bash
# Dump CAN bridge stats and active property values
adb shell dumpsys android.hardware.automotive.vehicle.IVehicle/default --can-status

# Inject a synthetic CAN frame via dumpsys (e.g., speed frame 0x100)
adb shell dumpsys android.hardware.automotive.vehicle.IVehicle/default --can-inject 100#122F180200000000
```

---

## 📦 AOSP Build & Deployment Commands

```bash
# Build native HAL daemon
m smartcabin-service

# Build VHAL blueprint service
m android.hardware.automotive.vehicle-service.blueprint
```
