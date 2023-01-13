dependencies {
    implementation(project(":hyuki-app:hyuki-application"))
    implementation(project(":hyuki-app:hyuki-domain"))
    implementation(project(":hyuki-app:hyuki-infrastructure"))
    implementation(project(":hyuki-app:hyuki-presentation"))
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
