package com.pactogg.app.feature.assist

import com.pactogg.app.feature.assist.data.FirebaseIdeaGenerator
import com.pactogg.app.feature.assist.domain.ChallengeIdeaGenerator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AssistModule {
    @Binds abstract fun ideas(generator: FirebaseIdeaGenerator): ChallengeIdeaGenerator
}
