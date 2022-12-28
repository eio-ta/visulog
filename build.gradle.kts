plugins {
    java
}

version = "0.0.1"
group = "up"

allprojects {

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    repositories {
        mavenCentral()
    }

    plugins.apply("java")

    java.sourceCompatibility = JavaVersion.VERSION_1_10

}