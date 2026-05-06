package id.faradyna.miniedcapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.faradyna.miniedcapp.data.device.MockDeviceInfoProvider
import id.faradyna.miniedcapp.domain.device.DeviceInfoProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceModule {

    @Binds
    @Singleton
    abstract fun bindDeviceInfoProvider(
        mockDeviceInfoProvider: MockDeviceInfoProvider
    ): DeviceInfoProvider
}
