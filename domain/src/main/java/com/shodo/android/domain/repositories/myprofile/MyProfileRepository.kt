package com.shodo.android.domain.repositories.myprofile

import com.shodo.android.domain.repositories.entities.NewActivity

interface MyProfileRepository {
//    suspend fun signIn(name: String, password: String)
//    suspend fun signUp(name: String, password: String)
//    suspend fun updateDescription(description: String)
//    suspend fun updateImage(image: String)
//    suspend fun signOut()

    suspend fun getMyActivities(): List<NewActivity>
}