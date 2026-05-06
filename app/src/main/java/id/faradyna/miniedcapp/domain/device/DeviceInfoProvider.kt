package id.faradyna.miniedcapp.domain.device

/**
 * Interface untuk mengambil informasi terkait dari hardware/device
 */
interface DeviceInfoProvider {
    fun getSerialNumber(): String

    fun getDeviceModel(): String
}
