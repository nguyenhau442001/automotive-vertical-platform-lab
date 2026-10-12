# ⚙️ AOSP Integration & HAL Deployment

This directory contains configuration templates, build blueprints, init `.rc` scripts, and SELinux policies required to build and integrate the vehicle daemon and VHAL adapter into an **Android Open Source Project (AOSP)** / **Android Automotive OS (AAOS)** system image.

---

## 📂 Directory Contents

- `sepolicy/smartcabin.te`: SELinux domain definitions for vendor vehicle services.
- Native build configurations (`Android.bp`) are maintained in `native/` and `vhal/aosp/`.

---

## 📦 AOSP Build & Deployment Workflow

### 1. Vendor Manifest (`manifest.xml`)
Register the mock VHAL / SmartCabin AIDL interface in the vendor VINTF manifest:

```xml
<manifest version="1.0" type="device">
    <hal format="aidl">
        <name>android.hardware.smartcabin</name>
        <version>1</version>
        <interface>
            <name>ISmartCabin</name>
            <instance>default</instance>
        </interface>
    </hal>
</manifest>
```

### 2. Init Script (`smartcabin-service.rc`)
Launch the native daemon upon vendor partition initialization:

```rc
service smartcabin-service /vendor/bin/hw/smartcabin-service
    class hal
    user vehicle_network
    group system inet
    seclabel u:r:smartcabin:s0
```

### 3. Fast Iteration Target Build Commands
Rather than rebuilding the entire AOSP system image (`m`), build target modules independently:

```bash
# Build native HAL daemon
m smartcabin-service

# Build VHAL blueprint service
m android.hardware.automotive.vehicle-service.blueprint
```
