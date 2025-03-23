package com.shodo.android.domain.usecases

import com.shodo.android.domain.repositories.entities.NewActivity
import com.shodo.android.domain.repositories.myprofile.MyProfileRepository

class GetMyActivitiesUseCase(private val myProfileRepository: MyProfileRepository): UseCase<Nothing?, List<NewActivity>> {
    override suspend fun defer(arg: Nothing?): List<NewActivity> {
        return myProfileRepository.getMyActivities()
    }
}