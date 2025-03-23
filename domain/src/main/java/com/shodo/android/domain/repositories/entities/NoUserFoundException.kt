package com.shodo.android.domain.repositories.entities

data class NoUserFoundException(val friend: String) : Exception()