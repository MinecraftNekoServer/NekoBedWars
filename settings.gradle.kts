pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.screamingsandals.org/public/")
        maven("https://maven.aliyun.com/repository/public") // 国内镜像
        maven("https://maven.minecraftforge.net")           // Forge 镜像
        maven("https://maven.neoforged.net/releases") {
            content {
                includeGroup("net.neoforged.licenser")
            }
        }
    }
}

rootProject.name = "NekoBedWars"
setupProject("BedWars-API", "api")
setupProject("BedWars-protocol", "protocol")
setupProject("BedWars-common", "plugin/common")
setupProject("BedWars-bukkit", "plugin/bukkit")
setupProject("BedWars", "plugin/universal", mkdir=true)

fun setupProject(name: String, folder: String, mkdir: Boolean = false) {
    include(name)
    project(":$name").let {
        it.projectDir = file(folder)
        if (mkdir) {
            it.projectDir.mkdirs()
        }
    }
}