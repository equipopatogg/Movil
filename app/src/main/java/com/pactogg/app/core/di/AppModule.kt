package com.pactogg.app.core.di

import android.content.Context
import androidx.room.Room
import com.pactogg.app.core.database.PactoDatabase
import com.pactogg.app.core.environment.BuildEnvironmentRepository
import com.pactogg.app.core.environment.EnvironmentRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EnvironmentModule {
    @Binds
    abstract fun bindEnvironment(implementation: BuildEnvironmentRepository): EnvironmentRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PactoDatabase =
        Room.databaseBuilder(context, PactoDatabase::class.java, "pactogg.db").build()
}
