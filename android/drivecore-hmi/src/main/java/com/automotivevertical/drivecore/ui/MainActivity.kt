package com.automotivevertical.drivecore.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.automotivevertical.drivecore.R
import com.automotivevertical.drivecore.data.CockpitState
import com.automotivevertical.drivecore.vhal.MockVehiclePropertyAdapter
import com.automotivevertical.drivecore.vhal.VehiclePropertyAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val vehicleAdapter: VehiclePropertyAdapter = MockVehiclePropertyAdapter()

    private lateinit var tvStatusBanner: TextView
    private lateinit var tvSpeedValue: TextView
    private lateinit var tvGearP: TextView
    private lateinit var tvGearR: TextView
    private lateinit var tvGearN: TextView
    private lateinit var tvGearD: TextView

    private lateinit var tvBrakeStatus: TextView
    private lateinit var tvDoorStatus: TextView
    private lateinit var tvHvacValue: TextView
    private lateinit var tvTurnValue: TextView
    private lateinit var tvIgnitionValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(buildCockpitDashboardView())

        lifecycleScope.launch {
            vehicleAdapter.cockpitState.collectLatest { state ->
                renderCockpitState(state)
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

    private fun renderCockpitState(state: CockpitState) {
        // Speedometer
        tvSpeedValue.text = "%.1f".format(state.speedKmH)

        // Gear Selector
        updateGearHighlight(state.gear)

        // Parking Brake
        if (state.parkingBrakeEngaged) {
            tvBrakeStatus.text = "🔴 ENGAGED"
            tvBrakeStatus.setTextColor(ContextCompat.getColor(this, R.color.cockpit_danger))
        } else {
            tvBrakeStatus.text = "🟢 RELEASED"
            tvBrakeStatus.setTextColor(ContextCompat.getColor(this, R.color.cockpit_success))
        }

        // Door State
        val doorText = when (state.doorBitmask) {
            0 -> "🟢 ALL CLOSED"
            1 -> "⚠️ DRIVER DOOR OPEN"
            2 -> "⚠️ PASSENGER DOOR OPEN"
            else -> "⚠️ DOORS OPEN (%d)".format(state.doorBitmask)
        }
        tvDoorStatus.text = doorText
        tvDoorStatus.setTextColor(
            if (state.doorBitmask == 0) ContextCompat.getColor(this, R.color.cockpit_success)
            else ContextCompat.getColor(this, R.color.cockpit_warning)
        )

        // HVAC Temperature
        tvHvacValue.text = "%.1f °C".format(state.hvacTempCelsius)

        // Turn Signal
        val turnText = when (state.turnSignalState) {
            "LEFT" -> "◄ LEFT"
            "RIGHT" -> "RIGHT ►"
            "HAZARD" -> "◄ HAZARD ►"
            else -> "OFF"
        }
        tvTurnValue.text = turnText
        tvTurnValue.setTextColor(
            if (state.turnSignalState != "NONE") ContextCompat.getColor(this, R.color.cockpit_warning)
            else ContextCompat.getColor(this, R.color.cockpit_text_secondary)
        )

        // Ignition
        tvIgnitionValue.text = state.ignitionState

        // Status Banner
        tvStatusBanner.text = "🟢 Mock VHAL Adapter Active | Refreshed: ${state.lastUpdateTimestampMs} ms"
    }

    private fun updateGearHighlight(activeGear: String) {
        val activeColor = ContextCompat.getColor(this, R.color.cockpit_gear_active)
        val inactiveColor = ContextCompat.getColor(this, R.color.cockpit_gear_inactive)

        tvGearP.setTextColor(if (activeGear == "PARK") activeColor else inactiveColor)
        tvGearR.setTextColor(if (activeGear == "REVERSE") activeColor else inactiveColor)
        tvGearN.setTextColor(if (activeGear == "NEUTRAL") activeColor else inactiveColor)
        tvGearD.setTextColor(if (activeGear == "DRIVE") activeColor else inactiveColor)
    }

    private fun buildCockpitDashboardView(): ScrollView {
        val scrollView = ScrollView(this).apply {
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_bg))
            isFillViewport = true
        }

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        // Header
        val tvTitle = TextView(this).apply {
            text = getString(R.string.app_name)
            textSize = 26f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_accent))
        }

        val tvSubtitle = TextView(this).apply {
            text = getString(R.string.app_subtitle)
            textSize = 13f
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_text_secondary))
            setPadding(0, 4, 0, 16)
        }

        tvStatusBanner = TextView(this).apply {
            textSize = 12f
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_success))
            setPadding(0, 0, 0, 24)
        }

        rootLayout.addView(tvTitle)
        rootLayout.addView(tvSubtitle)
        rootLayout.addView(tvStatusBanner)

        // Speedometer & Gear Row
        rootLayout.addView(createSpeedometerCard())
        rootLayout.addView(createGearSelectorCard())

        // Vehicle Safety Status Cards
        rootLayout.addView(createParkingBrakeCard())
        rootLayout.addView(createDoorStateCard())

        // Climate & Signals
        rootLayout.addView(createHvacCard())
        rootLayout.addView(createTurnSignalCard())
        rootLayout.addView(createIgnitionCard())

        scrollView.addView(rootLayout)
        return scrollView
    }

    private fun createCardContainer(titleRes: Int): Pair<LinearLayout, TextView> {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 24, 32, 24)
            background = ContextCompat.getDrawable(this@MainActivity, R.drawable.card_bg_rounded)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24)
            }
            layoutParams = params
        }

        val title = TextView(this).apply {
            text = getString(titleRes)
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_text_secondary))
            setPadding(0, 0, 0, 12)
        }

        container.addView(title)
        val valueView = TextView(this)
        container.addView(valueView)
        return Pair(container, valueView)
    }

    private fun createSpeedometerCard(): LinearLayout {
        val (card, valueView) = createCardContainer(R.string.card_speed)
        valueView.apply {
            textSize = 44f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_accent))
            text = "0.0"
        }
        tvSpeedValue = valueView

        val unitLabel = TextView(this).apply {
            text = "km/h"
            textSize = 14f
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_text_secondary))
        }
        card.addView(unitLabel)
        return card
    }

    private fun createGearSelectorCard(): LinearLayout {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 24, 32, 24)
            background = ContextCompat.getDrawable(this@MainActivity, R.drawable.card_bg_rounded)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24)
            }
            layoutParams = params
        }

        val title = TextView(this).apply {
            text = getString(R.string.card_gear)
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_text_secondary))
            setPadding(0, 0, 0, 16)
        }
        card.addView(title)

        val gearRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
        }

        fun makeGearBadge(label: String): TextView {
            return TextView(this).apply {
                text = label
                textSize = 28f
                typeface = Typeface.DEFAULT_BOLD
                setPadding(24, 0, 24, 0)
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_gear_inactive))
            }
        }

        tvGearP = makeGearBadge("P")
        tvGearR = makeGearBadge("R")
        tvGearN = makeGearBadge("N")
        tvGearD = makeGearBadge("D")

        gearRow.addView(tvGearP)
        gearRow.addView(tvGearR)
        gearRow.addView(tvGearN)
        gearRow.addView(tvGearD)

        card.addView(gearRow)
        return card
    }

    private fun createParkingBrakeCard(): LinearLayout {
        val (card, valueView) = createCardContainer(R.string.card_parking_brake)
        valueView.apply {
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            text = "RELEASED"
        }
        tvBrakeStatus = valueView
        return card
    }

    private fun createDoorStateCard(): LinearLayout {
        val (card, valueView) = createCardContainer(R.string.card_door)
        valueView.apply {
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD
            text = "ALL CLOSED"
        }
        tvDoorStatus = valueView
        return card
    }

    private fun createHvacCard(): LinearLayout {
        val (card, valueView) = createCardContainer(R.string.card_hvac)
        valueView.apply {
            textSize = 24f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_text_primary))
            text = "22.0 °C"
        }
        tvHvacValue = valueView
        return card
    }

    private fun createTurnSignalCard(): LinearLayout {
        val (card, valueView) = createCardContainer(R.string.card_turn_signal)
        valueView.apply {
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            text = "OFF"
        }
        tvTurnValue = valueView
        return card
    }

    private fun createIgnitionCard(): LinearLayout {
        val (card, valueView) = createCardContainer(R.string.card_ignition)
        valueView.apply {
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.cockpit_text_primary))
            text = "OFF"
        }
        tvIgnitionValue = valueView
        return card
    }
}
