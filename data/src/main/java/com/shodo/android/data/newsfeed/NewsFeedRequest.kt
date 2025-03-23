package com.shodo.android.data.newsfeed

import com.shodo.android.domain.repositories.entities.NewActivity

interface NewsFeedRequest {
    fun getNewActivities(): List<NewActivity>
}