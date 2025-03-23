package com.shodo.android.data.myprofile

import com.shodo.android.domain.repositories.entities.NewActivity
import com.shodo.android.domain.repositories.myprofile.MyProfileRepository

class MyProfileRepositoryImpl(private val myActivitiesDataStore: MyActivitiesDataStore) : MyProfileRepository {
    override suspend fun getMyActivities(): List<NewActivity> {
        return myActivitiesDataStore.getAllActivities()
    }
}