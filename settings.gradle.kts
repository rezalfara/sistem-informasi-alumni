pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        maven(url = "https://jitpack.io") // Add this line to include JitPack
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io") // Add this line to include JitPack
    }
}

rootProject.name = "Sistem Informasi Alumni"
include(":app")
