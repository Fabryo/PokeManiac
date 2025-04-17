![pokemaniac](https://github.com/user-attachments/assets/a9b47eed-3174-41b6-8840-5ad9ea2f5c0b)

## Pokemaniac – Proof of Concept (POC)

Welcome to the Pokemaniac POC!
This document provides a complete overview of the app’s current state, including technical implementation, business logic, and what’s coming next.

The whole documentation is available in [the Github's Wiki](https://github.com/Fabryo/PokeManiac/wiki).


### How to Run the App

Instructions for installing and launching the app locally are available [here](https://github.com/Fabryo/PokeManiac/wiki/Setup).

### Architecture & Tech Stack

```mermaid
flowchart LR
  %% Layers with custom red colors
  subgraph Presentation_Layer
        direction TB
        App["App (Splash, SignIn/SignUp)"]
        Dashboard["Dashboard"]
        SearchFriends["SearchFriends"]
        MyFriends["MyFriends"]
        MyProfile["MyProfile"]
        NewPost["NewPost"]
        CoreUI["CoreUI (Shared Views & Utils)"]
    end

    subgraph Domain_Layer
        direction TB
        Domain["Domain (Repository Interfaces + Entities)"]
    end

    subgraph Data_Layer
        direction TB
        Data["Data (Repository Implementations)"]
        Api["API (Retrofit Services)"]
        Database["Database (Room Persistence)"]
    end

    subgraph Tracking_Layer
        direction TB
        Tracking["Tracking (Analytics & User Tracking)"]
    end

    App --> Dashboard
    App --> SearchFriends
    App --> MyFriends
    App --> MyProfile
    App --> NewPost

    Dashboard --> CoreUI
    SearchFriends --> CoreUI
    MyFriends --> CoreUI
    MyProfile --> CoreUI
    NewPost --> CoreUI

    App --> Domain
    Dashboard --> Domain
    SearchFriends --> Domain
    MyFriends --> Domain
    MyProfile --> Domain
    NewPost --> Domain

    Domain --> Data
    Data --> Api
    Data --> Database

    App --> Tracking

    %% Style for better visibility
    style Presentation_Layer fill:#FFCCCC,stroke:#800000,stroke-width:2px
    style Domain_Layer fill:#FFCCCC,stroke:#800000,stroke-width:2px
    style Data_Layer fill:#FFCCCC,stroke:#800000,stroke-width:2px
    style Tracking_Layer fill:#FFCCCC,stroke:#800000,stroke-width:2px
    linkStyle default stroke:#800000,stroke-width:2px

---

 * Modular architecture following Clean Architecture principles
 * Fully built with Jetpack Compose
 * Key patterns: MVVM, StateFlow, DI with Koin, etc.
 * Libraries: Retrofit, Room, Coil, Coroutines, Flow, Koin, etc.

The Architecture in detail is available [here](https://github.com/Fabryo/PokeManiac/wiki/Architecture-&-Tech-choices#architecture).

More information about the Tech stack is available [here](https://github.com/Fabryo/PokeManiac/wiki/Architecture-&-Tech-choices#tech-choices).

### Concept & Business Plan
 * A social network dedicated to Pokémon card collectors
 * Key features: card library, friend interactions, transaction sharing, and a future marketplace
 * Monetization options: subscriptions, ads, transaction fees

The whole Concept & Business Plan are available [here](https://github.com/Fabryo/PokeManiac/wiki/Concept-and-Business-Plan).

### Business Features Implemented
 * NewsFeed with friend transactions
 * Friend search and subscriptions
 * Profile screen with personal transaction history
 * Transaction posting flow (photo, Pokémon name, price)
 * Local data persistence

More information, screens and videos about the implemented Business Features are available [here](https://github.com/Fabryo/PokeManiac/wiki/Implemented-Business-Features).

### Technical Features Implemented
 * i18n: French 🇫🇷 and English 🇬🇧 supported
 * Dark Mode support
 * Tracking module (mocked, ready to connect to Firebase, Segment…)
 * Unit testing examples across all layers (Request, Repository, UseCase, ViewModel)
 * Jetpack Compose Previews for UI testing

More information about the implemented Tech Features are available [here](https://github.com/Fabryo/PokeManiac/wiki/Tech-Features#implemented-tech-features-).

### Technical Features To Be Added
 * Crash reporting & code quality tools: Crashlytics, Sonar, Lint…
 * Accessibility support
 * Proguard / R8 obfuscation for code security
More information about the backlogged Tech Features are available [here](https://github.com/Fabryo/PokeManiac/wiki/Tech-Features#technical-features-to-add).

### Key Business Features To Be Implemented
 * Full Sign In / Sign Up flow
 * Onboarding journey & subscription paywall
 * Bottom navigation with tabs (Home, Pokédex, Card Search, Marketplace)
 * Marketplace with secure transactions
 * Tablet layout & responsive design
 * AI card recognition and valuation
More information about the backlogged Business Features are available [here](https://github.com/Fabryo/PokeManiac/wiki/Remaining-Features-to-implement)
  
