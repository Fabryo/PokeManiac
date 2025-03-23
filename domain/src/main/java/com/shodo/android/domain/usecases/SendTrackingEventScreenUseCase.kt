package com.shodo.android.domain.usecases

import com.shodo.android.domain.repositories.tracking.TrackingEventScreen
import com.shodo.android.domain.repositories.tracking.TrackingRepository

class SendTrackingEventScreenUseCase(private val trackingRepository: TrackingRepository): UseCase<TrackingEventScreen, Unit>{
    override suspend fun defer(arg: TrackingEventScreen) {
        trackingRepository.sendEventScreen(arg)
    }
}