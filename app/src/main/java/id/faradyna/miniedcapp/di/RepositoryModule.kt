package id.faradyna.miniedcapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.faradyna.miniedcapp.data.irepository.AuthRepositoryImpl
import id.faradyna.miniedcapp.data.irepository.PaymentRepositoryImpl
import id.faradyna.miniedcapp.data.irepository.TransactionRepositoryImpl
import id.faradyna.miniedcapp.domain.repository.AuthRepository
import id.faradyna.miniedcapp.domain.repository.PaymentRepository
import id.faradyna.miniedcapp.domain.repository.TransactionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImpl: PaymentRepositoryImpl
    ): PaymentRepository
}
