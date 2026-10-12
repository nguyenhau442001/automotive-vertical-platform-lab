# 🎙️ Interview Discussion Guide: AAOS Engineering Deep Dive

This guide is curated for technical discussions with recruiters, hiring managers, and Android Automotive Staff/Lead Engineers.

---

## 💡 Top Interview Questions & Technical Explanations

### Q1: How does a vehicle property update travel from C++ VHAL to an Android App?
**Answer**:
1. Native C++ vendor daemon receives raw CAN frame via `SocketCAN` or `TCP`.
2. `libcansignal` unpacks bitfields and calculates physical value.
3. Native daemon calls `IVehicleCallback.onPropertyEvent()` over **Binder IPC**.
4. Android Framework `CarService` (`CarPropertyService`) receives event, checks caller permissions (`android.car.permission.CAR_SPEED`).
5. `CarPropertyService` dispatches update to registered app via `CarPropertyManager.CarPropertyEventCallback`.
6. HMI application updates UI state reactively on main thread.

---

### Q2: Why is VHAL isolated in vendor partition (`/vendor`)?
**Answer**:
Android's Project Treble separates the core Android framework (`/system`) from vendor-specific HAL implementations (`/vendor`). This allows OEMs and Tier-1 suppliers to upgrade Android OS versions (e.g., Android 14 -> 15) without rewriting or re-certifying hardware driver code. The boundary is enforced via AIDL interfaces and SELinux domain isolation.

---

### Q3: How do you handle high-frequency signals like vehicle speed in AAOS?
**Answer**:
- Declare change mode as `CONTINUOUS` with target sample rate (e.g., 10 Hz).
- Implement rate-limiting and noise filtering in native C++ layer before Binder dispatch.
- Use atomic primitives and lock-free thread queues to prevent mutex contention on CAN receive loops.

---

### Q4: How does Audio Focus work in Android Automotive OS?
**Answer**:
AAOS replaces phone audio focus with `CarAudioService`, supporting physical zone routing (Driver, Passenger, Rear) and automatic Ducking / Muting. High-priority safety audio (e.g., ADAS chime or reverse camera warning) uses `AUDIO_USAGE_EMERGENCY` or `AUDIO_USAGE_SAFETY` and preempts media playback instantly.

---

### Q5: How do you debug cross-layer latency in AAOS?
**Answer**:
- Logcat filtering with tags (`adb shell logcat -s CanBridge SmartCabin VHAL CarPropertyManager`).
- Perfetto system tracing (`atrace`) with custom ATrace markers in C++ (`ATRACE_NAME`) and Java (`Trace.beginSection`).
- `dumpsys car_service --services CarPropertyService` to verify property values registered in memory.
