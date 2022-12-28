
plugins {
    java
    application
}

application.mainClass.set("up.visulog.cli.CLILauncher")

dependencies {
    implementation(project(":webgen"))
    implementation(project(":analyzer"))
    implementation(project(":config"))
    implementation(project(":gitrawdata"))
    testImplementation("junit:junit:4.+")
    implementation("com.google.code.gson:gson:2.8.0")

}


