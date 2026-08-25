package com.matiora.agent

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var deviceOwnerController: DeviceOwnerController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        deviceOwnerController = DeviceOwnerController(this)

        Log.i(LOG_TAG, "Application started")
        logDeviceInfo()
        bindStaticDeviceInfo()
    }

    override fun onResume() {
        super.onResume()
        bindProvisioningStatus()
    }

    private fun bindStaticDeviceInfo() {
        findViewById<TextView>(R.id.packageValue).text = packageName
        findViewById<TextView>(R.id.applicationIdValue).text = packageName
        findViewById<TextView>(R.id.androidValue).text = Build.VERSION.RELEASE
        findViewById<TextView>(R.id.sdkValue).text = Build.VERSION.SDK_INT.toString()
        findViewById<TextView>(R.id.manufacturerValue).text = Build.MANUFACTURER
        findViewById<TextView>(R.id.modelValue).text = Build.MODEL
        findViewById<TextView>(R.id.adminComponentValue).text =
            deviceOwnerController.getAdminComponentFlattened()
    }

    private fun bindProvisioningStatus() {
        val isDeviceOwner = deviceOwnerController.isDeviceOwner()
        val isDeviceAdmin = deviceOwnerController.isAdminActive()
        val isDeviceProvisioned = deviceOwnerController.isDeviceProvisioned()
        val isUserSetupComplete = deviceOwnerController.isUserSetupComplete()
        val isProvisioningAllowed = deviceOwnerController.isManagedDeviceProvisioningAllowed()
        val restrictionKeys = deviceOwnerController.getActiveUserRestrictionKeys()

        bindYesNo(R.id.deviceOwnerValue, isDeviceOwner)
        bindYesNo(R.id.deviceAdminValue, isDeviceAdmin)
        bindYesNo(R.id.deviceProvisionedValue, isDeviceProvisioned)
        bindYesNo(R.id.userSetupCompleteValue, isUserSetupComplete)
        bindYesNo(R.id.provisioningAllowedValue, isProvisioningAllowed)

        val provisioningState = if (isDeviceOwner) {
            getString(R.string.provisioning_state_device_owner)
        } else {
            getString(R.string.provisioning_state_not_device_owner)
        }
        findViewById<TextView>(R.id.provisioningValue).text = provisioningState

        findViewById<TextView>(R.id.userRestrictionsValue).text = if (restrictionKeys.isEmpty()) {
            getString(R.string.value_none)
        } else {
            restrictionKeys.joinToString("\n")
        }

        Log.i(LOG_TAG, "Device owner = $isDeviceOwner")
        Log.i(LOG_TAG, "Device admin = $isDeviceAdmin")
        Log.i(LOG_TAG, "device_provisioned = $isDeviceProvisioned")
        Log.i(LOG_TAG, "user_setup_complete = $isUserSetupComplete")
        Log.i(LOG_TAG, "isProvisioningAllowed(MANAGED_DEVICE) = $isProvisioningAllowed")
    }

    private fun bindYesNo(viewId: Int, value: Boolean) {
        val view = findViewById<TextView>(viewId)
        view.text = yesNo(value)
        view.setTextColor(color(if (value) R.color.matiora_yes else R.color.matiora_no))
    }

    private fun logDeviceInfo() {
        Log.i(LOG_TAG, "Android version = ${Build.VERSION.RELEASE}")
        Log.i(LOG_TAG, "SDK = ${Build.VERSION.SDK_INT}")
        Log.i(LOG_TAG, "Model = ${Build.MANUFACTURER} ${Build.MODEL}")
    }

    private fun yesNo(value: Boolean): String {
        return getString(if (value) R.string.value_yes else R.string.value_no)
    }

    @Suppress("DEPRECATION")
    private fun color(colorResId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getColor(colorResId)
        } else {
            resources.getColor(colorResId)
        }
    }

    companion object {
        private const val LOG_TAG = "MATIORA_AGENT"
    }
}
