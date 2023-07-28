pluginManagement {
    repositories {
        google()
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

rootProject.name = "prevail"
include(":app")

include(":feature:threadcatalog")
include(":feature:posts")
include(":feature:boards")
include(":feature:preferences")

include(":core:model")
include(":core:network")
include(":core:preferences")
include(":core:theme")
include(":core:media")
