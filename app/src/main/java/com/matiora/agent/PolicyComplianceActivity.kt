package com.matiora.agent

import android.app.Activity
import android.os.Bundle
import android.util.Log

class PolicyComplianceActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val controller = DeviceOwnerController(this)
        val isDeviceOwner = controller.isDeviceOwner()
        val isAdminActive = controller.isAdminActive()

        Log.i(LOG_TAG, "ADMIN_POLICY_COMPLIANCE received")
        Log.i(LOG_TAG, "Device owner = $isDeviceOwner")
        Log.i(LOG_TAG, "Device admin = $isAdminActive")

        setResult(RESULT_OK)
        finish()
    }

    companion object {
        private const val LOG_TAG = "MATIORA_AGENT"
    }
}
