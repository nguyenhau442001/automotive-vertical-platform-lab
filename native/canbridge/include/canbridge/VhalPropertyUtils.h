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

#pragma once

#include <cstdint>
#include <string>
#include <variant>

#include "canbridge/SignalBinding.h"

namespace blueprint::can {

enum class VhalPropertyType : int32_t {
    kUnknown = 0,
    kBoolean = 0x00200000,
    kInt32 = 0x00400000,
    kFloat = 0x00600000
};

enum class VhalPropertyStatus : int32_t {
    kAvailable = 0,
    kUnavailable = 1,
    kError = 2
};

struct VhalPropValue {
    int32_t propId = 0;
    int32_t areaId = 0;
    int64_t timestampNs = 0;
    VhalPropertyStatus status = VhalPropertyStatus::kAvailable;
    std::variant<float, int32_t, bool> value;
};

// Returns property type from AOSP property id bitmask (bits 23..16).
VhalPropertyType getVhalPropertyType(int32_t propId);

// Converts PropertyUpdate from CanBridge into typed VhalPropValue.
VhalPropValue toVhalPropValue(const PropertyUpdate& update);

// Formats property update into dumpsys string representation.
std::string formatPropertyDump(const PropertyUpdate& update);

}  // namespace blueprint::can
