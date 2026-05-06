package id.faradyna.miniedcapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.faradyna.miniedcapp.data.service.PrinterServiceImpl
import id.faradyna.miniedcapp.domain.service.PrinterService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindPrinterService(
        printerServiceImpl: PrinterServiceImpl
    ): PrinterService
}
