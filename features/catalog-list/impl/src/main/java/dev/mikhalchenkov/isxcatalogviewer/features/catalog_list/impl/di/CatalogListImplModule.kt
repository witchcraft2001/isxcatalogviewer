package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.api.CatalogListApi
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.CatalogListImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CatalogListImplModule {
    @Binds
    @Singleton
    abstract fun bindCatalogListFeatureImpl(
        impl: CatalogListImpl
    ): CatalogListApi
}