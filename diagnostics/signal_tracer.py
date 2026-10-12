#!/usr/bin/env python3
"""
Signal Tracer & Cross-Layer Latency Diagnostic Utility
DriveCore HMI - Vehicle Signal to Android HMI Lab

Inspects, validates, and logs vehicle signal transitions and estimates end-to-end
propagation latency from CAN/Native payload generation to Android HMI subscription.
"""

import sys
import json
import time
from pathlib import Path

SCHEMA_PATH = Path(__file__).parent.parent / "schemas" / "vehicle_signals.json"

class SignalTracer:
    def __init__(self, schema_file=SCHEMA_PATH):
        with open(schema_file, 'r', encoding='utf-8') as f:
            self.schema = json.load(f)
        self.signal_ids = {s['signal_id'] for s in self.schema['signals']}
        self.signal_stats = {sid: {"updates": 0, "last_seen": None, "last_val": None} for sid in self.signal_ids}

    def process_telemetry_event(self, event_json: str):
        """Processes a single telemetry event payload and tracks update frequencies."""
        try:
            data = json.loads(event_json)
            ts = data.get("timestamp_ms", int(time.time() * 1000))
            signals = data.get("signals", {})

            for sid, payload in signals.items():
                if sid in self.signal_stats:
                    stats = self.signal_stats[sid]
                    stats["updates"] += 1
                    stats["last_seen"] = ts
                    stats["last_val"] = payload.get("value")

            print(f"[{ts} ms] Processed {len(signals)} signals. Active signals: {list(signals.keys())}")
        except Exception as e:
            print(f"[ERROR] Failed to parse event: {e}", file=sys.stderr)

    def print_summary(self):
        """Prints a summary diagnostic table of captured signals."""
        print("\n=== DriveCore HMI Signal Diagnostics Summary ===")
        print(f"{'Signal ID':<20} | {'Updates':<8} | {'Last Value':<15}")
        print("-" * 50)
        for sid, stats in self.signal_stats.items():
            val_str = str(stats["last_val"]) if stats["last_val"] is not None else "N/A"
            print(f"{sid:<20} | {stats['updates']:<8} | {val_str:<15}")

if __name__ == "__main__":
    tracer = SignalTracer()
    sample_data = {
        "timestamp_ms": int(time.time() * 1000),
        "signals": {
            "VEHICLE_SPEED": {"value": 72.5, "unit": "km/h"},
            "GEAR_SELECTION": {"value": "DRIVE", "unit": "enum"},
            "PARKING_BRAKE": {"value": False, "unit": "boolean"},
            "DOOR_STATE": {"value": 0, "unit": "bitmask"},
            "HVAC_TEMPERATURE": {"value": 21.5, "unit": "celsius"},
            "TURN_SIGNAL": {"value": "NONE", "unit": "enum"},
            "IGNITION_STATE": {"value": "ON", "unit": "enum"}
        }
    }
    tracer.process_telemetry_event(json.dumps(sample_data))
    tracer.print_summary()
