package com.example.comthupohaircut.di

import com.example.comthupohaircut.data.repository.CategoryRepositoryImpl
import com.example.comthupohaircut.domain.repository.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Báo cho Hilt biết: Khi cần CategoryRepository -> Cung cấp CategoryRepositoryImpl
    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository
}