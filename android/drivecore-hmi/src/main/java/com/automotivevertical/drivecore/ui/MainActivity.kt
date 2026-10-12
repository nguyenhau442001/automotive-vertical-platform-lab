package com.automotivevertical.drivecore.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.automotivevertical.drivecore.ui.theme.DriveCoreTheme
import com.automotivevertical.drivecore.vhal.MockVehiclePropertyAdapter
import com.automotivevertical.drivecore.vhal.VehiclePropertyAdapter

class MainActivity : ComponentActivity() {

    private val vehicleAdapter: VehiclePropertyAdapter = MockVehiclePropertyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by vehicleAdapter.cockpitState.collectAsStateWithLifecycle()
            DriveCoreTheme {
                DashboardScreen(state = state)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        vehicleAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        vehicleAdapter.stopListening()
    }
}
