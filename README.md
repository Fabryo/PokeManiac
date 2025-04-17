![pokemaniac](https://github.com/user-attachments/assets/a9b47eed-3174-41b6-8840-5ad9ea2f5c0b)

# Pokemaniac – Proof of Concept (POC)

Welcome to the Pokemaniac POC!
This document provides a complete overview of the app’s current state, including technical implementation, business logic, and what’s coming next.

The whole documentation is available in [the Github's Wiki](https://github.com/Fabryo/PokeManiac/wiki).


## How to Run the App

Instructions for installing and launching the app locally are available [here](https://github.com/Fabryo/PokeManiac/wiki/Setup).

## Architecture & Tech Stack

 * Modular architecture following Clean Architecture principles
 * Fully built with Jetpack Compose
 * Key patterns: MVVM, StateFlow, DI with Koin, etc.
 * Libraries: Retrofit, Room, Coil, Coroutines, Flow, Koin, etc.

#### Module Dependency Diagram
   
```mermaid
flowchart TD

    %% Global style for dark mode
    classDef darkMode fill:#000000,stroke:#FFFFFF,stroke-width:2px,color:#FFFFFF;
    
    subgraph Presentation_Layer
        direction TB
        App["App (Main entry)"]
        Dashboard["Dashboard Feature"]
        SearchFriends["SearchFriends Feature"]
        MyFriends["MyFriends Feature"]
        MyProfile["MyProfile Feature"]
        NewPost["NewPost Feature"]
        CoreUI["CoreUI Module (Shared Compose Views)"]
    end

    subgraph Domain_Layer
        direction TB
        Domain["Domain (Repository Interfaces + Entities)"]
    end

    subgraph Data_Layer
        direction TB
        Data["Data Module (Repository Implementations)"]
        Api["API Module (Networking Services)"]
        Database["Database Module (Room Persistence)"]
    end

    subgraph Tracking_Layer
        Tracking["Tracking Module (Analytics + Events Storage)"]
    end

    %% -- Flows inside Presentation Layer
    App --> Dashboard
    App --> SearchFriends
    App --> MyFriends
    App --> MyProfile
    App --> NewPost

    App --> CoreUI
    Dashboard --> CoreUI
    SearchFriends --> CoreUI
    MyFriends --> CoreUI
    MyProfile --> CoreUI
    NewPost --> CoreUI

    %% -- External Flows
    Presentation_Layer --> Domain_Layer
    Presentation_Layer --> Tracking_Layer
    Tracking_Layer --> Domain_Layer

    Domain_Layer --> Data_Layer
    Api --> Data
    Api --> Domain_Layer
    Database --> Data
    Database --> Domain_Layer

    %% Apply dark mode style
    linkStyle default stroke:#FFFFFF,stroke-width:1.5px
    class App,Dashboard,SearchFriends,MyFriends,MyProfile,NewPost,CoreUI,Domain,Data,Api,Database,Tracking darkMode
```

#### Architecture Data Flow


```mermaid
flowchart TD

    %% Global style for dark mode
    classDef darkMode fill:#000000,stroke:#FFFFFF,stroke-width:2px,color:#FFFFFF;

    subgraph Presentation_Module
        direction TB
        Activity["Activity / Composable"]
        ViewModel["ViewModel"]
    end

    subgraph Domain_Module
        direction TB
        RepositoryInterface["Repository Interface"]
    end

    subgraph Data_Module
        direction TB
        RepositoryImpl["Repository Implementation"]
        RequestInterface["Request Interface"]
        DataStoreInterface["DataStore Interface"]
    end

    subgraph Database_Module
        direction TB
        DataStoreImpl["DataStore Implementation"]
        RoomDatabase["Room Database"]
    end

    subgraph API_Module
        direction TB
        RequestImpl["Request Implementation"]
    end

    subgraph External_Services
        direction TB
        RemoteApi["Remote API"]
    end

    %% Calls (flow top-down)
    Activity -->|User Actions| ViewModel
    ViewModel -->|Call Repository| Domain_Module
    RepositoryInterface -->|Implementation| RepositoryImpl
    RepositoryImpl -->|Fetch Remote Data| RequestInterface
    RepositoryImpl -->|Fetch Local Data| DataStoreInterface
    RequestInterface -->|Implementation| RequestImpl
    DataStoreInterface --> |Implementation| DataStoreImpl
    RequestImpl -->|Call External API| RemoteApi
    DataStoreImpl -->|Fetch Local Data| RoomDatabase

    %% Responses (horizontal dotted lines, no "up")
    ViewModel -.->|StateFlow/SharedFlow| Activity
    Domain_Module -.->|Flow Data| ViewModel
    RemoteApi -.->|API Response| RequestImpl
    RoomDatabase -.->|Flow DB Entities| DataStoreImpl
    RequestInterface -.->|Remote Data| RepositoryImpl
    DataStoreInterface -.->|Flow Local Data| RepositoryImpl

    %% Styling for dark mode
    linkStyle default stroke:#FFFFFF,stroke-width:1.5px
    class Activity,ViewModel,RepositoryInterface,RepositoryImpl,RequestInterface,DataStoreInterface,RequestImpl,DataStoreImpl,RoomDatabase,RemoteApi darkMode
```

The Architecture in detail is available [here](https://github.com/Fabryo/PokeManiac/wiki/Architecture-&-Tech-choices#architecture).

More information about the Tech stack is available [here](https://github.com/Fabryo/PokeManiac/wiki/Architecture-&-Tech-choices#tech-choices).

## Concept & Business Plan
 * A social network dedicated to Pokémon card collectors
 * Key features: card library, friend interactions, transaction sharing, and a future marketplace
 * Monetization options: subscriptions, ads, transaction fees

The whole Concept & Business Plan are available [here](https://github.com/Fabryo/PokeManiac/wiki/Concept-and-Business-Plan).

## Business Features Implemented
 * NewsFeed with friend transactions
 * Friend search and subscriptions
 * Profile screen with personal transaction history
 * Transaction posting flow (photo, Pokémon name, price)
 * Local data persistence

More information, screens and videos about the implemented Business Features are available [here](https://github.com/Fabryo/PokeManiac/wiki/Implemented-Business-Features).

## Technical Features Implemented
 * i18n: French 🇫🇷 and English 🇬🇧 supported
 * Dark Mode support
 * Tracking module (mocked, ready to connect to Firebase, Segment…)
 * Unit testing examples across all layers (Request, Repository, UseCase, ViewModel)
 * Jetpack Compose Previews for UI testing

More information about the implemented Tech Features are available [here](https://github.com/Fabryo/PokeManiac/wiki/Tech-Features#implemented-tech-features-).

## Technical Features To Be Added
 * Crash reporting & code quality tools: Crashlytics, Sonar, Lint…
 * Accessibility support
 * Proguard / R8 obfuscation for code security
More information about the backlogged Tech Features are available [here](https://github.com/Fabryo/PokeManiac/wiki/Tech-Features#technical-features-to-add).

## Key Business Features To Be Implemented
 * Full Sign In / Sign Up flow
 * Onboarding journey & subscription paywall
 * Bottom navigation with tabs (Home, Pokédex, Card Search, Marketplace)
 * Marketplace with secure transactions
 * Tablet layout & responsive design
 * AI card recognition and valuation
More information about the backlogged Business Features are available [here](https://github.com/Fabryo/PokeManiac/wiki/Remaining-Features-to-implement)
  
