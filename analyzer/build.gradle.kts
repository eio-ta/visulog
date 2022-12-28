
plugins {
    `java-library`
}

dependencies {
    implementation(project(":webgen"))
    implementation(project(":config"))
    implementation(project(":gitrawdata"))
    testImplementation("junit:junit:4.+")
    implementation("com.google.code.gson:gson:2.8.0")

}


