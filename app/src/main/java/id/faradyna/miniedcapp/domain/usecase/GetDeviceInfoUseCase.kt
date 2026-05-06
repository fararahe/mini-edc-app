package id.faradyna.miniedcapp.domain.usecase

import id.faradyna.miniedcapp.domain.device.DeviceInfoProvider
import id.faradyna.miniedcapp.domain.model.DeviceInfo
import javax.inject.Inject

class GetDeviceInfoUseCase @Inject constructor(
    private val deviceInfoProvider: DeviceInfoProvider
) {
    operator fun invoke(): DeviceInfo {
        return DeviceInfo(
            serialNumber = deviceInfoProvider.getSerialNumber(),
            model = deviceInfoProvider.getDeviceModel()
        )
    }
}
