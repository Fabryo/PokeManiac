package com.shodo.android.domain.usecases

import com.shodo.android.domain.repositories.tracking.TrackingEventClick
import com.shodo.android.domain.repositories.tracking.TrackingRepository

class SendTrackingEventClickUseCase(private val trackingRepository: TrackingRepository): UseCase<TrackingEventClick, Unit>{
    override suspend fun defer(arg: TrackingEventClick) {
        trackingRepository.sendEventClick(arg)
    }
}