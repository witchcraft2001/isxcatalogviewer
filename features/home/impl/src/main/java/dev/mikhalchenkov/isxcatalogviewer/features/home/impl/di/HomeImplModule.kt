package dev.mikhalchenkov.isxcatalogviewer.features.home.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mikhalchenkov.isxcatalogviewer.features.home.impl.HomeImpl
import dev.mikhalchenkov.isxcatalogviewer.features.home_screen.HomeApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeImplModule {
    @Binds
    @Singleton
    abstract fun bindHomeFeatureImpl(
        impl: HomeImpl
    ): HomeApi
}