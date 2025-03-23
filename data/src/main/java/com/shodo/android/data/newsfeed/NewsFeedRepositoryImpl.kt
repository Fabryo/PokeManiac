package com.shodo.android.data.newsfeed

import com.shodo.android.data.myfriends.FriendsDataStore
import com.shodo.android.data.myprofile.MyActivitiesDataStore
import com.shodo.android.domain.repositories.entities.ImageSource
import com.shodo.android.domain.repositories.entities.UserPokemonCard
import com.shodo.android.domain.repositories.entities.NewActivity
import com.shodo.android.domain.repositories.entities.NewActivityType
import com.shodo.android.domain.repositories.news.NewsFeedRepository
import java.time.LocalDateTime

// This one is mocked, sorry !
class NewsFeedRepositoryImpl(
    private val friendsDataStore: FriendsDataStore,
    private val myActivitiesDataStore: MyActivitiesDataStore
): NewsFeedRepository {
    override suspend fun getNewActivities(): List<NewActivity> {
        return mutableListOf<NewActivity>().apply {
            addAll(myActivitiesDataStore.getAllActivities())
            friendsDataStore.getFriendById("69")?.let { batman69 ->
                add(NewActivity(
                    userName = batman69.name,
                    userImageUrl = batman69.imageUrl,
                    date = LocalDateTime.of(2025, 2, 20, 12, 34),
                    pokemonCard = batman69.pokemonCards[2],
                    activityType = NewActivityType.Purchase,
                    price = 59
                ))
                add(NewActivity(
                    userName = batman69.name,
                    userImageUrl = batman69.imageUrl,
                    date = LocalDateTime.of(2025, 3, 10, 2, 4),
                    pokemonCard = batman69.pokemonCards[3],
                    activityType = NewActivityType.Purchase,
                    price = 190
                ))
                add(NewActivity(
                    userName = batman69.name,
                    userImageUrl = batman69.imageUrl,
                    date = LocalDateTime.of(2025, 3, 10, 2, 4),
                    pokemonCard = UserPokemonCard(
                        pokemonId = 42,
                        totalVotes = 1,
                        hasMyVote = false,
                        name = "Golbat",
                        imageSource = ImageSource.UrlSource("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/42.png")
                    ),
                    activityType = NewActivityType.Sale,
                    price = 2338
                ))
            }

            friendsDataStore.getFriendById("70")?.let { batman70 ->
                add(NewActivity(
                    userName = batman70.name,
                    userImageUrl = batman70.imageUrl,
                    date = LocalDateTime.of(2025, 1, 12, 3, 34),
                    pokemonCard = batman70.pokemonCards[0],
                    activityType = NewActivityType.Purchase,
                    price = 59
                ))
                add(NewActivity(
                    userName = batman70.name,
                    userImageUrl = batman70.imageUrl,
                    date = LocalDateTime.of(2025, 3, 10, 2, 4),
                    pokemonCard = UserPokemonCard(
                        pokemonId = 150,
                        totalVotes = 193,
                        hasMyVote = false,
                        name = "Mewtwo",
                        imageSource = ImageSource.UrlSource("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/150.png")
                    ),
                    activityType = NewActivityType.Sale,
                    price = 10000
                ))
            }

            friendsDataStore.getFriendById("720")?.let { superman720 ->
                add(NewActivity(
                    userName = superman720.name,
                    userImageUrl = superman720.imageUrl,
                    date = LocalDateTime.of(2024, 12, 30, 13, 50),
                    pokemonCard = superman720.pokemonCards[0],
                    activityType = NewActivityType.Purchase,
                    price = 59
                ))
                add(NewActivity(
                    userName = superman720.name,
                    userImageUrl = superman720.imageUrl,
                    date = LocalDateTime.of(2025, 2, 14, 19, 4),
                    pokemonCard = UserPokemonCard(
                        pokemonId = 12,
                        totalVotes = 20,
                        hasMyVote = false,
                        name = "Butterfree",
                        imageSource = ImageSource.UrlSource("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/12.png")
                    ),
                    activityType = NewActivityType.Sale,
                    price = 80
                ))
            }
        }.sortedByDescending { it.date }.toList()
    }

    override suspend fun saveNewActivity(newActivity: NewActivity) {
        myActivitiesDataStore.saveNewActivity(newActivity)
    }
}