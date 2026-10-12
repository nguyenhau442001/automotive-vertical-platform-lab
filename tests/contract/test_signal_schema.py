#!/usr/bin/env python3
"""
Signal Schema Contract Test Suite
DriveCore HMI - Vehicle Signal to Android HMI Lab

Validates schemas/vehicle_signals.json for compliance, completeness,
and correctness of signal specifications and AOSP property constants.
"""

import json
import unittest
from pathlib import Path

SCHEMA_PATH = Path(__file__).parent.parent.parent / "schemas" / "vehicle_signals.json"

class TestSignalSchemaContract(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        if not SCHEMA_PATH.exists():
            raise FileNotFoundError(f"Schema file missing at {SCHEMA_PATH}")
        with open(SCHEMA_PATH, 'r', encoding='utf-8') as f:
            cls.schema = json.load(f)
        cls.signals = {s['signal_id']: s for s in cls.schema.get('signals', [])}

    def test_required_signals_exist(self):
        """Verifies that all 7 required signals are defined in the schema."""
        required = {
            "VEHICLE_SPEED",
            "GEAR_SELECTION",
            "PARKING_BRAKE",
            "DOOR_STATE",
            "HVAC_TEMPERATURE",
            "TURN_SIGNAL",
            "IGNITION_STATE"
        }
        missing = required - set(self.signals.keys())
        self.assertEqual(len(missing), 0, f"Missing required signals in schema: {missing}")

    def test_signal_vhal_property_ids(self):
        """Verifies that each signal specifies a valid hexadecimal VHAL property ID."""
        for sid, signal in self.signals.items():
            vhal_id = signal.get("vhal_property_id", "")
            self.assertTrue(vhal_id.startswith("0x"), f"Signal {sid} vhal_property_id must start with '0x'")
            self.assertIn("vhal_property_id_decimal", signal, f"Signal {sid} missing vhal_property_id_decimal")

    def test_signal_can_mapping(self):
        """Verifies that each signal includes complete CAN bit mapping metadata."""
        for sid, signal in self.signals.items():
            can = signal.get("can_mapping", {})
            self.assertIn("can_id", can, f"Signal {sid} missing CAN ID")
            self.assertIn("start_bit", can, f"Signal {sid} missing start_bit")
            self.assertIn("bit_length", can, f"Signal {sid} missing bit_length")
            self.assertGreater(can.get("bit_length", 0), 0, f"Signal {sid} bit_length must be > 0")

    def test_change_modes(self):
        """Verifies valid change mode declarations (CONTINUOUS or ON_CHANGE)."""
        valid_modes = {"CONTINUOUS", "ON_CHANGE"}
        for sid, signal in self.signals.items():
            mode = signal.get("change_mode")
            self.assertIn(mode, valid_modes, f"Signal {sid} invalid change_mode: {mode}")

if __name__ == "__main__":
    unittest.main()
