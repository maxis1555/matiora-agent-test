package com.matiora.agent

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.UserManager
import android.provider.Settings

class DeviceOwnerController(context: Context) {

    private val appContext = context.applicationContext

    private val devicePolicyManager: DevicePolicyManager =
        appContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

    private val userManager: UserManager =
        appContext.getSystemService(Context.USER_SERVICE) as UserManager

    private val adminComponentName: ComponentName =
        ComponentName(appContext, MatioraDeviceAdminReceiver::class.java)

    fun isDeviceOwner(): Boolean {
        return devicePolicyManager.isDeviceOwnerApp(PACKAGE_NAME)
    }

    fun isDeviceAdmin(): Boolean {
        return isAdminActive()
    }

    fun isAdminActive(): Boolean {
        return devicePolicyManager.isAdminActive(adminComponentName)
    }

    fun getAdminComponentFlattened(): String {
        return adminComponentName.flattenToShortString()
    }

    fun isDeviceProvisioned(): Boolean {
        return Settings.Global.getInt(
            appContext.contentResolver,
            Settings.Global.DEVICE_PROVISIONED,
            0
        ) == 1
    }

    fun isUserSetupComplete(): Boolean {
        return Settings.Secure.getInt(
            appContext.contentResolver,
            USER_SETUP_COMPLETE,
            0
        ) == 1
    }

    @Suppress("DEPRECATION")
    fun isManagedDeviceProvisioningAllowed(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            devicePolicyManager.isProvisioningAllowed(
                DevicePolicyManager.ACTION_PROVISION_MANAGED_DEVICE
            )
        } else {
            false
        }
    }

    fun getActiveUserRestrictionKeys(): List<String> {
        val restrictions = userManager.userRestrictions
        return restrictions.keySet()
            .filter { key -> restrictions.getBoolean(key) }
            .sorted()
    }

    companion object {
        const val PACKAGE_NAME = "com.matiora.agent"

        // Settings.Secure.USER_SETUP_COMPLETE is @hide in the public SDK.
        private const val USER_SETUP_COMPLETE = "user_setup_complete"
    }
}
