package id.faradyna.miniedcapp.data.device

import android.os.Build
import id.faradyna.miniedcapp.domain.device.DeviceInfoProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Simulasi implementasi dari DeviceInfoProvider.
 *
 * Dipakai untuk development atau testing di emulator / device non-EDC,
 * supaya aplikasi tetap bisa jalan tanpa SDK bawaan EDC.
 */
@Singleton
class MockDeviceInfoProvider @Inject constructor() : DeviceInfoProvider {
    /**
     * Mengembalikan nomor serial dari terminal / device
     */
    override fun getSerialNumber(): String {
        return "SN-MOCK-123456"
    }

    /**
     * Mengembalikan nama model terminal / device
     */
    override fun getDeviceModel(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL} (Mock EDC)"
    }
}
