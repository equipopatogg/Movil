package com.pactogg.app.feature.onboarding

import com.pactogg.app.core.preferences.OnboardingPreferences
import com.pactogg.app.core.preferences.TutorialStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TutorialModule {
    @Binds abstract fun store(preferences: OnboardingPreferences): TutorialStore
}
