package com.pactogg.app.core.di

import android.content.Context
import com.pactogg.app.BuildConfig
import com.pactogg.app.core.network.ApiClientFactory
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.data.EncryptedSessionStore
import com.pactogg.app.feature.auth.domain.SessionStore
import com.pactogg.app.feature.connected.data.ApiProductRepository
import com.pactogg.app.feature.connected.domain.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectedModule {
    @Provides
    @Singleton
    fun api(factory: ApiClientFactory): PactoApi {
        val client = ApiClientFactory.defaultClient().newBuilder().retryOnConnectionFailure(false).build()
        return factory.create(BuildConfig.API_BASE_URL, client).create(PactoApi::class.java)
    }

    @Provides
    @Singleton
    fun store(@ApplicationContext context: Context): SessionStore = EncryptedSessionStore(context)

    @Provides
    @Singleton
    fun sessions(api: PactoApi, store: SessionStore): SessionManager = SessionManager(api, store)

    @Provides
    @Singleton
    fun socials(api: PactoApi, sessions: SessionManager):
        com.pactogg.app.feature.connected.domain.SocialRepository =
        com.pactogg.app.feature.connected.data.ApiSocialRepository(api, sessions)

    @Provides
    @Singleton
    fun products(api: PactoApi, sessions: SessionManager): ProductRepository = ApiProductRepository(api, sessions)
}
