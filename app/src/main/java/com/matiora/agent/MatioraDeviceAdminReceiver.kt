package com.matiora.agent

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MatioraDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        logAdminState(context, "Device admin enabled")
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Log.i(LOG_TAG, "Device admin disabled")
        Log.i(LOG_TAG, "Device owner = false")
        Log.i(LOG_TAG, "Device admin = false")
    }

    override fun onProfileProvisioningComplete(context: Context, intent: Intent) {
        super.onProfileProvisioningComplete(context, intent)
        logAdminState(context, "PROFILE_PROVISIONING_COMPLETE")
    }

    private fun logAdminState(context: Context, event: String) {
        val controller = DeviceOwnerController(context)
        Log.i(LOG_TAG, event)
        Log.i(LOG_TAG, "Device owner = ${controller.isDeviceOwner()}")
        Log.i(LOG_TAG, "Device admin = ${controller.isAdminActive()}")
    }

    companion object {
        private const val LOG_TAG = "MATIORA_AGENT"
    }
}
