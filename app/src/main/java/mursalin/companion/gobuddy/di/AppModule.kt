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
import mursalin.companion.gobuddy.data.repository.*
import mursalin.companion.gobuddy.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppwriteClient(@ApplicationContext context: Context): Client {
        return Client(context)
            .setEndpoint("https://nyc.cloud.appwrite.io/v1")
            .setProject("68909080002fa8013fde") // IMPORTANT: Replace with your Project ID
           // .setSelfSigned(true)
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

    @Provides
    @Singleton
    fun provideAuthRepository(account: Account): AuthRepository {
        return AuthRepositoryImpl(account)
    }

    @Provides
    @Singleton
    fun provideProjectRepository(databases: Databases): ProjectRepository {
        return ProjectRepositoryImpl(databases)
    }

    @Provides
    @Singleton
    fun provideChatRepository(databases: Databases): ChatRepository {
        return ChatRepositoryImpl(databases)
    }

    @Provides
    @Singleton
    fun provideAchievementRepository(databases: Databases): AchievementRepository {
        return AchievementRepositoryImpl(databases)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(databases: Databases): TaskRepository {
        return TaskRepositoryImpl(databases)
    }

    @Provides
    @Singleton
    fun provideReminderRepository(): ReminderRepository {
        return ReminderRepositoryImpl()
    }
}
