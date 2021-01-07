package com.fingerprintjs.android.fingerprint.info_providers


import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import java.security.Security


interface DeviceSecurityInfoProvider {
    fun encryptionStatus(): String
    fun securityProvidersData(): List<Pair<String, String>>
    fun isPinSecurityEnabled(): Boolean
}

class DeviceSecurityInfoProviderImpl(
    private val devicePolicyManager: DevicePolicyManager,
    private val keyguardManager: KeyguardManager
) : DeviceSecurityInfoProvider {
    override fun encryptionStatus(): String {
        return executeSafe({
            stringDescriptionForEncryptionStatus(devicePolicyManager.storageEncryptionStatus)
        }, "")
    }

    override fun securityProvidersData(): List<Pair<String, String>> {
        return executeSafe({
            Security.getProviders().map {
                Pair(it.name, it.info ?: "")
            }
        }, emptyList())
    }

    override fun isPinSecurityEnabled() = executeSafe(
        { keyguardManager.isKeyguardSecure }, false
    )
}

private fun stringDescriptionForEncryptionStatus(status: Int): String {
    return when (status) {
        DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED -> UNSUPPORTED
        DevicePolicyManager.ENCRYPTION_STATUS_INACTIVE -> INACTIVE
        DevicePolicyManager.ENCRYPTION_STATUS_ACTIVATING -> ACTIVATING
        DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE -> ACTIVE
        DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE_PER_USER ->
            ACTIVE_PER_USER
        else -> ""
    }
}

private const val UNSUPPORTED = "unsupported"
private const val INACTIVE = "inactive"
private const val ACTIVATING = "activating"
private const val ACTIVE = "active"
private const val ACTIVE_PER_USER = "active_per_user"