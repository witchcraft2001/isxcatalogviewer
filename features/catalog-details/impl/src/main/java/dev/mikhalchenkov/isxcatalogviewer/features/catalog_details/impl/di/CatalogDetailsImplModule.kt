package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.api.CatalogDetailsApi
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.CatalogDetailsImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CatalogDetailsImplModule {
    @Binds
    @Singleton
    abstract fun bindCatalogDetailsFeatureImpl(
        impl: CatalogDetailsImpl
    ): CatalogDetailsApi
}