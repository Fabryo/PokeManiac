![pokemaniac](https://github.com/user-attachments/assets/a9b47eed-3174-41b6-8840-5ad9ea2f5c0b)

# PokeManiac
A Social Network for Pokemon Cards' collectors

App developed for job interview purpose.
Author : Fabrice Rakotonarivo from Bryo.

The application can be used with Android 8+.
It is developed in kotlin.
Choice was made not to develop the app for Tablet for time-related reasons. 

## Setup

The application fetches a list of characters from the SuperheroAPI https://superheroapi.com/.
To compile and run the app, you need to obtain a token from the API and add it to your zshrc or bash_profile file:

- add in your `~/.bash_profile` or `~/.profile` or `~/.zshrc` : 
```
export SUPER_HERO_API_TOKEN="your_token"
```

- run in terminal :

`source ~/.profile` OR `source ~/.bash_profile` OR `source ~/.zshrc`

- invalidate/cache Android Studio (not necessarily needed)

OR in the file `api/build.gradle.kts`, add your token line 6

## Architecture

The application is developed using a modular architecture that follows Clean Architecture principles.
This structure ensures the scalability of the app when new features need to be added.

The implemented modules are:
- App module: the main entry point of the application. It also contains the Splash and SignIn/SignUp screens.
- One module per Dynamic Feature, such as:
   - Dashboard: the news feed where all transactions from the user’s friends (and the user) appear.
   - SearchFriends: the feature for finding friends.
   - MyFriends: the feature to browse your friend list and view detailed one-to-one transaction history.
   - MyProfile: the user’s profile feature.
   - NewPost: the feature to create and share new posts.
   - CoreUI module: contains shared Compose Views, resources, and utilities used across feature modules.
   
All UI modules follow the MVVM design pattern, with ViewModels exposing state objects. All screens are implemented using Jetpack Compose.

Additional layers include:
- Domain module, containing:
  - Use Cases
  - Repository interfaces
  - Model Entities
- Data module, containing:
  - Request interfaces
  - Repository implementations
- Api module: introduced to separate networking logic from the rest of the data layer. It includes:
  - The HTTP client and API services
  - API request implementations
- Database module: separates local persistence logic (implemented with Room) from the rest of the data layer.
- Tracking module: allows integration of third-party libraries for analytics and user tracking. The goal is to monitor feature usage and leverage this data to make meaningful decisions about future app evolution.


## Tech choices :

The application is built using the following libraries:
- Jetpack Compose for the UI
- Material and AndroidX for design components, views, icons, and lifecycle management
- Coroutines and Flow to handle the data flow from the data-api-database modules to the ViewModels and UI subscriptions
- Flow (StateFlow and SharedFlow) to expose states and events from ViewModels to screens
- Coil for image loading
- Koin for dependency injection
- SquareUp Retrofit & OkHttp3 for remote API calls
- Room for local database implementation
- JUnit, Mockito, Koin-Test, and Espresso for testing


  
