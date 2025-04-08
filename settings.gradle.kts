pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PokeManiac"
include(":app")
include(":feature:searchfriend")
include(":feature:myfriends")
include(":feature:dashboard")
include(":feature:posttransaction")
include(":feature:myprofile")
include(":domain")
include(":data")
include(":api")
include(":coreui")
include(":database")
include(":tracking")
