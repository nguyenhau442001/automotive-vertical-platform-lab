package com.automotivevertical.drivecore.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.automotivevertical.drivecore.R
import com.automotivevertical.drivecore.data.CockpitState
import com.automotivevertical.drivecore.vhal.MockVehiclePropertyAdapter
import com.automotivevertical.drivecore.vhal.VehiclePropertyAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val vehicleAdapter: VehiclePropertyAdapter = MockVehiclePropertyAdapter()

    private lateinit var tvSpeed: TextView
    private lateinit var tvGear: TextView
    private lateinit var tvParkingBrake: TextView
    private lateinit var tvDoorState: TextView
    private lateinit var tvHvacTemp: TextView
    private lateinit var tvTurnSignal: TextView
    private lateinit var tvIgnition: TextView
    private lateinit var tvStatusBanner: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createDashboardLayout())

        lifecycleScope.launch {
            vehicleAdapter.cockpitState.collectLatest { state ->
                updateCockpitUI(state)
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

    private fun updateCockpitUI(state: CockpitState) {
        tvSpeed.text = "%.1f km/h".format(state.speedKmH)
        tvGear.text = "Gear: ${state.gear}"
        tvParkingBrake.text = "Brake: ${if (state.parkingBrakeEngaged) "ENGAGED" else "RELEASED"}"
        tvDoorState.text = "Door State: ${if (state.doorBitmask == 0) "ALL CLOSED" else "DRIVER OPEN"}"
        tvHvacTemp.text = "HVAC: %.1f °C".format(state.hvacTempCelsius)
        tvTurnSignal.text = "Turn Indicator: ${state.turnSignalState}"
        tvIgnition.text = "Ignition: ${state.ignitionState}"
        tvStatusBanner.text = "🟢 Mock VHAL Adapter Active | Refreshed: ${state.lastUpdateTimestampMs}"
    }

    private fun createDashboardLayout(): LinearLayout {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            setBackgroundColor(0xFF121418.toInt())
        }

        val title = TextView(this).apply {
            text = getString(R.string.app_name)
            textSize = 28f
            setTextColor(0xFF00E5FF.toInt())
            setPadding(0, 0, 0, 8)
        }

        val subtitle = TextView(this).apply {
            text = getString(R.string.app_subtitle)
            textSize = 14f
            setTextColor(0xFF8E99A8.toInt())
            setPadding(0, 0, 0, 32)
        }

        tvStatusBanner = TextView(this).apply {
            textSize = 12f
            setTextColor(0xFF00E676.toInt())
            setPadding(0, 0, 0, 24)
        }

        tvSpeed = TextView(this).apply {
            textSize = 42f
            setTextColor(0xFFFFFFFF.toInt())
            setPadding(0, 16, 0, 16)
        }

        tvGear = TextView(this).apply {
            textSize = 20f
            setTextColor(0xFFFFFFFF.toInt())
            setPadding(0, 8, 0, 8)
        }

        tvParkingBrake = TextView(this).apply {
            textSize = 18f
            setTextColor(0xFF8E99A8.toInt())
            setPadding(0, 8, 0, 8)
        }

        tvDoorState = TextView(this).apply {
            textSize = 18f
            setTextColor(0xFF8E99A8.toInt())
            setPadding(0, 8, 0, 8)
        }

        tvHvacTemp = TextView(this).apply {
            textSize = 18f
            setTextColor(0xFF8E99A8.toInt())
            setPadding(0, 8, 0, 8)
        }

        tvTurnSignal = TextView(this).apply {
            textSize = 18f
            setTextColor(0xFF8E99A8.toInt())
            setPadding(0, 8, 0, 8)
        }

        tvIgnition = TextView(this).apply {
            textSize = 18f
            setTextColor(0xFF8E99A8.toInt())
            setPadding(0, 8, 0, 8)
        }

        root.addView(title)
        root.addView(subtitle)
        root.addView(tvStatusBanner)
        root.addView(tvSpeed)
        root.addView(tvGear)
        root.addView(tvParkingBrake)
        root.addView(tvDoorState)
        root.addView(tvHvacTemp)
        root.addView(tvTurnSignal)
        root.addView(tvIgnition)

        return root
    }
}
