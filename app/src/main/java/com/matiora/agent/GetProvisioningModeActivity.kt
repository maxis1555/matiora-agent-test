package com.matiora.agent

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.os.Bundle
import android.util.Log

class GetProvisioningModeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val allowedModes = intent.getIntegerArrayListExtra(
            DevicePolicyManager.EXTRA_PROVISIONING_ALLOWED_PROVISIONING_MODES
        )
        val fullyManaged = DevicePolicyManager.PROVISIONING_MODE_FULLY_MANAGED_DEVICE

        Log.i(LOG_TAG, "GET_PROVISIONING_MODE received")
        Log.i(LOG_TAG, "Allowed provisioning modes = $allowedModes")

        if (allowedModes?.contains(fullyManaged) == true) {
            val result = Intent()
            result.putExtra(DevicePolicyManager.EXTRA_PROVISIONING_MODE, fullyManaged)
            setResult(RESULT_OK, result)
            Log.i(LOG_TAG, "Provisioning mode = FULLY_MANAGED_DEVICE")
        } else {
            setResult(RESULT_CANCELED)
            Log.w(LOG_TAG, "Fully managed device mode is not allowed by Setup Wizard")
        }

        finish()
    }

    companion object {
        private const val LOG_TAG = "MATIORA_AGENT"
    }
}
