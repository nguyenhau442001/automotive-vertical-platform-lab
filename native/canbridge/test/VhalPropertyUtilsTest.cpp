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

#include <gtest/gtest.h>

#include "canbridge/VhalPropertyUtils.h"

namespace blueprint::can {
namespace {

constexpr int32_t kPerfVehicleSpeed = 0x11600207;
constexpr int32_t kGearSelection = 0x11400400;
constexpr int32_t kParkingBrakeOn = 0x11200402;
constexpr int32_t kDoorPos = 0x16400b00;
constexpr int32_t kHvacTemperatureSet = 0x12400505;

TEST(VhalPropertyUtilsTest, DetectsPropertyTypesCorrectly) {
    EXPECT_EQ(getVhalPropertyType(kPerfVehicleSpeed), VhalPropertyType::kFloat);
    EXPECT_EQ(getVhalPropertyType(kGearSelection), VhalPropertyType::kInt32);
    EXPECT_EQ(getVhalPropertyType(kParkingBrakeOn), VhalPropertyType::kBoolean);
    EXPECT_EQ(getVhalPropertyType(kDoorPos), VhalPropertyType::kInt32);
    EXPECT_EQ(getVhalPropertyType(kHvacTemperatureSet), VhalPropertyType::kInt32);
}

TEST(VhalPropertyUtilsTest, ConvertsPropertyUpdateToFloatValue) {
    PropertyUpdate update{
        .propId = kPerfVehicleSpeed,
        .areaId = 0,
        .value = 85.5,
        .available = true,
        .timestampNs = 1000000000LL,
        .propName = "PERF_VEHICLE_SPEED"
    };

    VhalPropValue propVal = toVhalPropValue(update);
    EXPECT_EQ(propVal.propId, kPerfVehicleSpeed);
    EXPECT_EQ(propVal.areaId, 0);
    EXPECT_EQ(propVal.status, VhalPropertyStatus::kAvailable);
    EXPECT_EQ(propVal.timestampNs, 1000000000LL);
    ASSERT_TRUE(std::holds_alternative<float>(propVal.value));
    EXPECT_FLOAT_EQ(std::get<float>(propVal.value), 85.5f);
}

TEST(VhalPropertyUtilsTest, ConvertsPropertyUpdateToInt32ValueWithRounding) {
    PropertyUpdate update{
        .propId = kGearSelection,
        .areaId = 0,
        .value = 7.9,
        .available = true,
        .timestampNs = 1000000000LL,
        .propName = "GEAR_SELECTION"
    };

    VhalPropValue propVal = toVhalPropValue(update);
    ASSERT_TRUE(std::holds_alternative<int32_t>(propVal.value));
    EXPECT_EQ(std::get<int32_t>(propVal.value), 8);
}

TEST(VhalPropertyUtilsTest, ConvertsPropertyUpdateToBooleanValue) {
    PropertyUpdate update{
        .propId = kParkingBrakeOn,
        .areaId = 0,
        .value = 1.0,
        .available = true,
        .timestampNs = 1000000000LL,
        .propName = "PARKING_BRAKE_ON"
    };

    VhalPropValue propVal = toVhalPropValue(update);
    ASSERT_TRUE(std::holds_alternative<bool>(propVal.value));
    EXPECT_TRUE(std::get<bool>(propVal.value));
}

TEST(VhalPropertyUtilsTest, HandlesUnavailableStatus) {
    PropertyUpdate update{
        .propId = kPerfVehicleSpeed,
        .areaId = 0,
        .value = 0.0,
        .available = false,
        .timestampNs = 2000000000LL,
        .propName = "PERF_VEHICLE_SPEED"
    };

    VhalPropValue propVal = toVhalPropValue(update);
    EXPECT_EQ(propVal.status, VhalPropertyStatus::kUnavailable);
}

TEST(VhalPropertyUtilsTest, FormatsPropertyDumpOutput) {
    PropertyUpdate update{
        .propId = kPerfVehicleSpeed,
        .areaId = 0,
        .value = 120.45,
        .available = true,
        .timestampNs = 123456789LL,
        .propName = "PERF_VEHICLE_SPEED"
    };

    std::string dump = formatPropertyDump(update);
    EXPECT_NE(dump.find("120.45"), std::string::npos);
    EXPECT_NE(dump.find("AVAILABLE"), std::string::npos);
}

}  // namespace
}  // namespace blueprint::can
