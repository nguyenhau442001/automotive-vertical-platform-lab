/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "canbridge/VhalPropertyUtils.h"

#include <cmath>
#include <cstdio>

namespace blueprint::can {

VhalPropertyType getVhalPropertyType(int32_t propId) {
    int32_t typeMask = propId & 0x00FF0000;
    switch (typeMask) {
        case static_cast<int32_t>(VhalPropertyType::kBoolean):
            return VhalPropertyType::kBoolean;
        case static_cast<int32_t>(VhalPropertyType::kInt32):
            return VhalPropertyType::kInt32;
        case static_cast<int32_t>(VhalPropertyType::kFloat):
            return VhalPropertyType::kFloat;
        default:
            return VhalPropertyType::kUnknown;
    }
}

VhalPropValue toVhalPropValue(const PropertyUpdate& update) {
    VhalPropValue v;
    v.propId = update.propId;
    v.areaId = update.areaId;
    v.timestampNs = update.timestampNs;
    v.status = update.available ? VhalPropertyStatus::kAvailable
                                : VhalPropertyStatus::kUnavailable;

    VhalPropertyType type = getVhalPropertyType(update.propId);
    switch (type) {
        case VhalPropertyType::kFloat:
            v.value = static_cast<float>(update.value);
            break;
        case VhalPropertyType::kInt32:
            v.value = static_cast<int32_t>(std::llround(update.value));
            break;
        case VhalPropertyType::kBoolean:
            v.value = (update.value != 0.0);
            break;
        default:
            v.value = static_cast<float>(update.value);
            break;
    }
    return v;
}

std::string formatPropertyDump(const PropertyUpdate& update) {
    char buf[256];
    const char* statusStr = update.available ? "AVAILABLE" : "UNAVAILABLE";
    VhalPropertyType type = getVhalPropertyType(update.propId);

    if (type == VhalPropertyType::kBoolean) {
        std::snprintf(buf, sizeof(buf), "[0x%08X] area=%d name=%s val=%s status=%s ts=%lld",
                      update.propId, update.areaId, update.propName,
                      (update.value != 0.0) ? "true" : "false", statusStr,
                      static_cast<long long>(update.timestampNs));
    } else if (type == VhalPropertyType::kInt32) {
        std::snprintf(buf, sizeof(buf), "[0x%08X] area=%d name=%s val=%d status=%s ts=%lld",
                      update.propId, update.areaId, update.propName,
                      static_cast<int32_t>(std::llround(update.value)), statusStr,
                      static_cast<long long>(update.timestampNs));
    } else {
        std::snprintf(buf, sizeof(buf), "[0x%08X] area=%d name=%s val=%.2f status=%s ts=%lld",
                      update.propId, update.areaId, update.propName,
                      update.value, statusStr,
                      static_cast<long long>(update.timestampNs));
    }
    return std::string(buf);
}

}  // namespace blueprint::can
