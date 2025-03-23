package com.shodo.android.domain.usecases

import com.shodo.android.domain.repositories.entities.NewActivity
import com.shodo.android.domain.repositories.news.NewsFeedRepository

class SaveNewActivityUseCase(private val newsFeedRepository: NewsFeedRepository): UseCase<NewActivity, Unit> {
    override suspend fun defer(arg: NewActivity) {
        newsFeedRepository.saveNewActivity(arg)
    }
}