package com.shodo.android.domain.usecases

interface BaseUseCase<I, O>

interface UseCase<I, O> : BaseUseCase<I, O> {
    suspend fun defer(arg: I): O

    companion object {
        suspend fun <E> UseCase<Nothing?, E>.await() = defer(null)
    }
}