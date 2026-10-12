#!/usr/bin/env python3
"""
Vehicle Signal Generator & CAN Simulator Engine
DriveCore HMI - Vehicle Signal to Android HMI Lab

Reads schemas/vehicle_signals.json as the source of truth and generates
synthetic vehicle signal payloads and CAN frames for testing.
"""

import json
import math
import time
import os
import sys
from pathlib import Path

SCHEMA_PATH = Path(__file__).parent.parent / "schemas" / "vehicle_signals.json"

class SignalGenerator:
    def __init__(self, schema_file=SCHEMA_PATH):
        with open(schema_file, 'r', encoding='utf-8') as f:
            self.schema = json.load(f)
        self.signals = {s['signal_id']: s for s in self.schema['signals']}

    def generate_current_state(self, timestamp_sec: float) -> dict:
        """Calculates realistic dynamic values for all 7 vehicle signals based on time."""
        # Speed: Smooth sine wave between 0 km/h and 120 km/h
        speed = max(0.0, round(60.0 + 55.0 * math.sin(timestamp_sec * 0.2), 1))

        # Gear: Depends on speed
        if speed == 0.0:
            gear = "PARK"
        elif speed < 15.0:
            gear = "REVERSE" if math.sin(timestamp_sec * 0.05) < -0.8 else "DRIVE"
        else:
            gear = "DRIVE"

        # Parking brake: Engaged when PARK
        parking_brake = (gear == "PARK")

        # Door state: 0 (Closed) normally, driver door open if parked
        door_state = 1 if (gear == "PARK" and math.sin(timestamp_sec * 0.1) > 0.7) else 0

        # HVAC Temperature: Wobble between 18.0°C and 24.0°C
        hvac_temp = round(21.0 + 3.0 * math.cos(timestamp_sec * 0.05), 1)

        # Turn Signal: Flashes left or right periodically
        turn_phase = math.sin(timestamp_sec * 0.3)
        if turn_phase > 0.7:
            turn_signal = "LEFT"
        elif turn_phase < -0.7:
            turn_signal = "RIGHT"
        else:
            turn_signal = "NONE"

        # Ignition: Always ON during active simulation
        ignition = "ON"

        return {
            "timestamp_ms": int(timestamp_sec * 1000),
            "signals": {
                "VEHICLE_SPEED": {"value": speed, "unit": "km/h", "vhal_id": self.signals["VEHICLE_SPEED"]["vhal_property_id"]},
                "GEAR_SELECTION": {"value": gear, "unit": "enum", "vhal_id": self.signals["GEAR_SELECTION"]["vhal_property_id"]},
                "PARKING_BRAKE": {"value": parking_brake, "unit": "boolean", "vhal_id": self.signals["PARKING_BRAKE"]["vhal_property_id"]},
                "DOOR_STATE": {"value": door_state, "unit": "bitmask", "vhal_id": self.signals["DOOR_STATE"]["vhal_property_id"]},
                "HVAC_TEMPERATURE": {"value": hvac_temp, "unit": "celsius", "vhal_id": self.signals["HVAC_TEMPERATURE"]["vhal_property_id"]},
                "TURN_SIGNAL": {"value": turn_signal, "unit": "enum", "vhal_id": self.signals["TURN_SIGNAL"]["vhal_property_id"]},
                "IGNITION_STATE": {"value": ignition, "unit": "enum", "vhal_id": self.signals["IGNITION_STATE"]["vhal_property_id"]}
            }
        }

    def run_cli_stream(self, rate_hz: float = 2.0, duration_sec: float = 10.0):
        """Streams formatted signal state JSON to stdout."""
        print(f"=== DriveCore HMI Vehicle Signal Generator ===")
        print(f"Loaded schema version {self.schema.get('version')} with {len(self.signals)} signals.")
        print(f"Streaming vehicle state at {rate_hz} Hz for {duration_sec}s...\n")

        start_time = time.time()
        interval = 1.0 / rate_hz

        try:
            while (time.time() - start_time) < duration_sec:
                now = time.time()
                state = self.generate_current_state(now - start_time)
                print(json.dumps(state, indent=2))
                time.sleep(interval)
        except KeyboardInterrupt:
            print("\nSimulation stopped by user.")

if __name__ == "__main__":
    generator = SignalGenerator()
    duration = float(sys.argv[1]) if len(sys.argv) > 1 else 5.0
    generator.run_cli_stream(rate_hz=2.0, duration_sec=duration)
