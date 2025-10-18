package dev.mikhalchenkov.isxcatalogviewer.core.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mikhalchenkov.isxcatalogviewer.core.data.repositories.CatalogRepositoryImpl
import dev.mikhalchenkov.isxcatalogviewer.core.data.repositories.FavoriteRepositoryImpl
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideJsonDecoder(): Json = Json { ignoreUnknownKeys = true }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCatalogRepository(impl: CatalogRepositoryImpl): CatalogRepository

    @Binds
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository
}