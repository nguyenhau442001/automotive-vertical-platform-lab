# 🧪 Tests: Multi-Layer Verification Suite

The `tests/` directory houses automated contract tests, native GoogleTest execution wrappers, and test harness definitions across the vertical stack.

---

## 🚦 Test Suites Overview

| Test Suite | Layer | Scope | Executed Via |
|------------|-------|-------|--------------|
| **Schema Contract Tests** | Schema (`schemas/`) | Validates `vehicle_signals.json` against required signals and VHAL ID constraints. | `python3 -m unittest tests/contract/test_signal_schema.py` |
| **Native Signal Codec Tests** | Native C++ (`native/cansignal/`) | Validates CAN frame bit unpacking, float scaling, offset handling. | `ctest` / GoogleTest in `native/build` |
| **Transport Tests** | Native C++ (`native/cantransport/`) | Validates SocketCAN and TCP packet parsing under load. | GoogleTest in `native/build` |
| **Android HMI Unit Tests** | Android (`android/signal-cockpit`) | Tests `VehiclePropertyAdapter` StateFlow updates and viewmodel mappings. | `./gradlew test` |

---

## 🏃 Running All Tests

You can run the entire host test suite using the top-level Makefile:

```bash
make test
```
