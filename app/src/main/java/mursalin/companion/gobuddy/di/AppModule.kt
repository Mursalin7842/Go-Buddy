// ---------------------------------------------------------------------------------
// 📦 di (Dependency Injection)
// ---------------------------------------------------------------------------------
// This package contains Hilt modules that tell the DI framework how to provide
// instances of classes to other parts of the app.
// ---------------------------------------------------------------------------------

// FILE: app/src/main/java/mursalin/companion/gobuddy/di/AppModule.kt
package mursalin.companion.gobuddy.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Databases
import mursalin.companion.gobuddy.data.repository.AuthRepositoryImpl
import mursalin.companion.gobuddy.domain.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppwriteClient(@ApplicationContext context: Context): Client {
        return Client(context)
            .setEndpoint("https://nyc.cloud.appwrite.io/v1") // Your Appwrite Endpoint
            .setProject("68909080002fa8013fde")              // Your Project ID
            .setSelfSigned(true)
    }

    @Provides
    @Singleton
    fun provideAppwriteAccount(client: Client): Account {
        return Account(client)
    }

    @Provides
    @Singleton
    fun provideAppwriteDatabases(client: Client): Databases {
        return Databases(client)
    }

    // This function tells Hilt that whenever some part of the app asks for an
    // AuthRepository, it should provide an instance of AuthRepositoryImpl.
    @Provides
    @Singleton
    fun provideAuthRepository(account: Account): AuthRepository {
        return AuthRepositoryImpl(account)
    }
}