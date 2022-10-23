import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.13"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21" // JPA 사용을 위한 플러그인 추가 옵션입니다.

    id ("org.jetbrains.kotlin.plugin.allopen") version "1.5.21" // allOpen에 지정한 어노테이션으로 만든 클래스에 open 키워드를 적용해줍니다.
    id ("org.jetbrains.kotlin.plugin.noarg") version "1.5.21" // 자동으로 Entity, Embeddable, MappedSuperClass 어노테이션이 붙어있는 클래스에 자동으로 no-arg 생성자를 생성해줍니다.
}

noArg {
    annotation("javax.persistence.Entity") // Entity 애노테이션이 붙은 코틀린 클래스의 NoArgument 생성자 자동 생성을 위한 설정입니다.
}

allOpen {
    annotation("javax.persistence.Entity") // Entity 애노테이션이 붙은 코틀린의 클래스를 open 클래스로 만들어주는 설정입니다.
    annotation("javax.persistence.MappedSuperclass") // MappedSuperclass 애노테이션이 붙은 코틀린의 클래스를 open 클래스로 만들어주는 설정입니다.
    annotation("javax.persistence.Embeddable") // Embeddable 애노테이션이 붙은 코틀린 클래스를 open 클래스로 만들어주는 설정입니다.
}

group = "com.byeoru"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    // twilio
    implementation(group = "com.twilio.sdk", name = "twilio", version = "8.27.0")
    // AWS S3
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    // hibernate Spatial Type 의존성
    implementation(group = "org.hibernate", name = "hibernate-spatial")
    // firebase sdk
    implementation("com.google.firebase:firebase-admin:9.1.0")
    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    // spring security
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-security")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
