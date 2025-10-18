package dev.mikhalchenkov.isxcatalogviewer.domain.usecases

import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    // Usually we don't make use cases as proxy functions,
    // but in this test example I've left it for abstraction purposes
    suspend operator fun invoke(itemId: String) = favoriteRepository.toggle(itemId)
}