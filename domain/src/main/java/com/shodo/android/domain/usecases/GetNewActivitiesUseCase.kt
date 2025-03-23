package com.shodo.android.domain.usecases

import com.shodo.android.domain.repositories.entities.NewActivity
import com.shodo.android.domain.repositories.news.NewsFeedRepository

class GetNewActivitiesUseCase(private val newsFeedRepository: NewsFeedRepository): UseCase<Nothing?, List<NewActivity>> {
    override suspend fun defer(arg: Nothing?): List<NewActivity> {
        return newsFeedRepository.getNewActivities()
    }
}