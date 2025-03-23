package com.shodo.android.data.myprofile

import com.shodo.android.domain.repositories.entities.NewActivity

interface MyActivitiesDataStore {
    suspend fun saveNewActivity(newActivity: NewActivity)
    suspend fun getAllActivities(): List<NewActivity>
}