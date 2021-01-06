package com.fingerprintjs.android.fingerprint.signal_providers

import com.fingerprintjs.android.fingerprint.info_providers.PackageInfo
import com.fingerprintjs.android.fingerprint.info_providers.PackageManagerDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import junit.framework.Assert.assertEquals
import org.junit.Test


class InstalledAppsSignalGroupProviderTests {

    @Test
    fun `InstalledAppsFingerprinter v1 - success`() {
        val packageManagerDataSource = object :
            PackageManagerDataSource {
            override fun getApplicationsList() = listOf(
                PackageInfo(
                    "app3"
                ),
                PackageInfo(
                    "app2"
                ),
                PackageInfo(
                    "app4"
                ),
                PackageInfo(
                    "app1"
                )
            )

        }
        val fingerprinter =
            InstalledAppsSignalGroupProvider(
                packageManagerDataSource,
                EmptyHasher(),
                1
            )

        assertEquals("app1app2app3app4", fingerprinter.fingerprint())
    }
}