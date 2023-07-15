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

include(":core:model")
include(":core:network")
include(":core:preferences")
include(":feature:preferences")
include(":core:theme")
include(":core:media")