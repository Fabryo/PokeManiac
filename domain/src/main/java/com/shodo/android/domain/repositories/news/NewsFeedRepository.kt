package com.shodo.android.domain.repositories.news

import com.shodo.android.domain.repositories.entities.NewActivity

interface NewsFeedRepository {
    suspend fun getNewActivities(): List<NewActivity>
    suspend fun saveNewActivity(newActivity: NewActivity)
}