package com.automotivevertical.drivecore

import com.automotivevertical.drivecore.data.VehicleProperty
import com.automotivevertical.drivecore.vhal.MockVehiclePropertyAdapter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class VehiclePropertyAdapterTest {

    @Test
    fun testVehiclePropertiesMapping() {
        assertEquals("PERF_VEHICLE_SPEED", VehicleProperty.VEHICLE_SPEED.vhalName)
        assertEquals("0x11600207", VehicleProperty.VEHICLE_SPEED.propIdHex)
        assertEquals(291504647, VehicleProperty.VEHICLE_SPEED.propIdDecimal)

        assertEquals("GEAR_SELECTION", VehicleProperty.GEAR_SELECTION.vhalName)
        assertEquals("PARKING_BRAKE_ON", VehicleProperty.PARKING_BRAKE.vhalName)
        assertEquals("DOOR_POS", VehicleProperty.DOOR_STATE.vhalName)
        assertEquals("HVAC_TEMPERATURE_SET", VehicleProperty.HVAC_TEMPERATURE.vhalName)
        assertEquals("TURN_SIGNAL_STATE", VehicleProperty.TURN_SIGNAL.vhalName)
        assertEquals("IGNITION_STATE", VehicleProperty.IGNITION_STATE.vhalName)
    }

    @Test
    fun testMockAdapterState() {
        val adapter = MockVehiclePropertyAdapter()
        val initialState = adapter.cockpitState.value
        assertNotNull(initialState)
        assertEquals(true, initialState.isConnected)
    }
}
