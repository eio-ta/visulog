
plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":config"))
    implementation(project(":gitrawdata"))
    testImplementation("junit:junit:4.+")
    implementation("com.github.xmlet:htmlflow:3.5")
}


