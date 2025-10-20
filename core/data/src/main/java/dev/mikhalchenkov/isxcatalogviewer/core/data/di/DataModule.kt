package dev.mikhalchenkov.isxcatalogviewer.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mikhalchenkov.isxcatalogviewer.core.data.datasources.CatalogDataSource
import dev.mikhalchenkov.isxcatalogviewer.core.data.datasources.CatalogDataSourceImpl
import dev.mikhalchenkov.isxcatalogviewer.core.data.datasources.FavoritesDataSource
import dev.mikhalchenkov.isxcatalogviewer.core.data.datasources.FavoritesDataSourceImpl
import dev.mikhalchenkov.isxcatalogviewer.core.data.repositories.CatalogRepositoryImpl
import dev.mikhalchenkov.isxcatalogviewer.core.data.repositories.FavoriteRepositoryImpl
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCatalogRepository(impl: CatalogRepositoryImpl): CatalogRepository

    @Binds
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindCatalogDataSource(impl: CatalogDataSourceImpl): CatalogDataSource

    @Binds
    @Singleton
    abstract fun bindFavoritesDataSource(impl: FavoritesDataSourceImpl): FavoritesDataSource
}