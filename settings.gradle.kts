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

rootProject.name = "ISX Catalog Viewer"
include(":app")
include(":core:data")
include(":core:domain")
include(":core:navigation")
include(":core:common")
include(":features:catalog-list:api")
include(":features:catalog-list:impl")
include(":core:ui")
