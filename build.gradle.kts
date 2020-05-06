import com.opentable.db.postgres.embedded.EmbeddedPostgres
import dev.bombinating.gradle.jooq.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging

val genDir = "$buildDir/generated/src/main/java"
val jooqUrl: String = "jdbc:postgresql://localhost:5433/coffee"
val jooqUsername: String = "postgres"
val jooqPassword: String = "password"
lateinit var postgres: EmbeddedPostgres

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"
    id("dev.bombinating.jooq-codegen") version "1.7.0"
    id("org.flywaydb.flyway") version "6.0.8"
}

buildscript {
    dependencies {
        classpath(group = "com.opentable.components", name = "otj-pg-embedded", version = "0.13.3")
        classpath("org.flywaydb:flyway-core:6.0.8")
    }
}

group = "name.nepavel"
version = "0.0.1-SNAPSHOT"
//java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/src/main/java")
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
// https://mvnrepository.com/artifact/com.opentable.components/otj-pg-embedded
    implementation("com.opentable.components:otj-pg-embedded:0.13.3")
    jooqRuntime("org.postgresql:postgresql")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

jooq {
    edition = JooqEdition.OpenSource
    version = "3.13.1"
    runConfig { jvmArgs = listOf("-Xmx2g") }
    resultHandler { println("The exit value of the code generation was: $this") }
    logging = Logging.DEBUG
    jdbc {
        url = jooqUrl
        username = jooqUsername
        password = jooqPassword
    }
    generator {
        generate {
            isJavaTimeTypes = true
            isDaos = true
            isPojos = true
            isNullableAnnotation = true
            nullableAnnotationType = "javax.annotation.Nullable"
            isNonnullAnnotation = true
            nonnullAnnotationType = "javax.annotation.Nonnull"
        }
        database {
            inputSchema = "public"
            excludes = "^BIN\\$.*|flyway_schema_history"
        }
        target {
            directory = genDir
            packageName = "name.nepavel.coffeeinbot"
        }
    }
}

flyway {
    url = jooqUrl
    user = jooqUsername
    password = jooqPassword
    baselineOnMigrate = true
}

tasks["flywayMigrate"].doFirst {
    try {
        postgres = EmbeddedPostgres.builder().setPort(5433).start()
        postgres.postgresDatabase.connection.use {
            it.createStatement().execute("CREATE DATABASE coffee")
        }
    } catch (e: Exception) {
        postgres.close()
        throw e;
    }
}

tasks["jooq"].dependsOn(tasks["flywayMigrate"])

tasks["jooq"].doLast {
    postgres.close()
}

tasks["compileKotlin"].dependsOn(tasks["jooq"])