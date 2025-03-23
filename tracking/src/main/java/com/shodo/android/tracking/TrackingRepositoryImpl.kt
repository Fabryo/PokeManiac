package com.shodo.android.tracking

import com.shodo.android.data.tracking.TrackingDataStore
import com.shodo.android.domain.repositories.tracking.TrackingRepository

class TrackingRepositoryImpl(private val trackingDataStore: TrackingDataStore): TrackingRepository {

    override suspend fun sendEventScreen(trackingEventScreen: com.shodo.android.domain.repositories.tracking.TrackingEventScreen) {
        trackingDataStore.sendEventScreen(trackingEventScreen.eventName)
    }

    override suspend fun sendEventClick(trackingEventClick: com.shodo.android.domain.repositories.tracking.TrackingEventClick) {
        trackingDataStore.sendEventClick(trackingEventClick.eventName)
    }
}